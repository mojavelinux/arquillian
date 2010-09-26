/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.framework.spring.shrinkwrap;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;

/**
 * Extension that will create a ClassLoader based on a Array of Archives
 * 
 * NOTE
 * Had to remove the cache of input streams as it was failing in a multi-threaded environment.
 * JUnit must kick off a thread to invoke the tests or something and the InputStream buf gets
 * lost because it's volatile.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @author <a href="mailto:dan.allen@mojavelinux.com">Dan Allen</a>
 */
public class StrictShrinkWrapClassLoader extends URLClassLoader implements Closeable
{
   //-------------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(StrictShrinkWrapClassLoader.class.getName());

   //-------------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Map of all streams opened, such that they may be closed in {@link StrictShrinkWrapClassLoader#close()}.
    * Guarded by "this".
    */
   private final Queue<InputStream> openedStreams = new ConcurrentLinkedQueue<InputStream>();

   private final Map<String, Boolean> checkedClassNames;
   
   private final Archive<?>[] archives;
   
   //-------------------------------------------------------------------------------------||
   // Constructors -----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Constructs a new ShrinkWrapClassLoader for the specified {@link Archive}s using the
    * default delegation parent <code>ClassLoader</code>. The {@link Archive}s will
    * be searched in the order specified for classes and resources after
    * first searching in the parent class loader.
    * 
    * @param archives the {@link Archive}s from which to load classes and resources
    */
   public StrictShrinkWrapClassLoader(final Archive<?>... archives)
   {
      super(new URL[] {});

      if (archives == null)
      {
         throw new IllegalArgumentException("Archives must be specified");
      }
      
      this.archives = archives;
      this.checkedClassNames = new HashMap<String, Boolean>();
      addArchives(archives);
   }

   /**
    * Constructs a new ShrinkWrapClassLoader for the given {@link Archive}s. The {@link Archive}s will be
    * searched in the order specified for classes and resources after first
    * searching in the specified parent class loader. 
    * 
    * @param parent the parent class loader for delegation
    * @param archives the {@link Archive}s from which to load classes and resources
    */
   public StrictShrinkWrapClassLoader(final ClassLoader parent, final Archive<?>... archives)
   {
      super(new URL[] {}, parent);

      if (archives == null)
      {
         throw new IllegalArgumentException("Archives must be specified");
      }
      this.archives = archives;
      this.checkedClassNames = new HashMap<String, Boolean>();
      addArchives(archives);
   }

   private void addArchives(final Archive<?>[] archives)
   {
      for (final Archive<?> archive : archives)
      {
         addArchive(archive);
      }
   }

   private void addArchive(final Archive<?> archive)
   {
      try
      {
         addURL(new URL(null, "archive:" + archive.getName() + "/", new URLStreamHandler()
         {
            @Override
            protected URLConnection openConnection(final URL u) throws IOException
            {
               return new URLConnection(u)
               {
                  @Override
                  public void connect() throws IOException
                  {
                  }

                  @Override
                  public InputStream getInputStream() throws IOException
                  {
                     synchronized (this)
                     {
                        ArchivePath path = convertToArchivePath(u);
                        InputStream input = archive.get(path).getAsset().openStream();
                        openedStreams.add(input);
                        return input;
                     }
                  }

                  private ArchivePath convertToArchivePath(URL url)
                  {
                     String path = url.getPath();
                     path = path.replace(archive.getName(), "");

                     return ArchivePaths.create(path);
                  }
               };
            }
         }));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not create URL for archive: " + archive.getName(), e);
      }
   }
   
   @Override
   public Class<?> loadClass(String name) throws ClassNotFoundException
   {
      Boolean presentInArchive = checkedClassNames.get(name);
      if (presentInArchive == null)
      {
         ArchivePath path = ArchivePaths.create(name.replace('.', '/') + ".class");
         presentInArchive = false;
         for (Archive<?> candidate : archives)
         {
            if (candidate.contains(path))
            {
               presentInArchive = true;
               break;
            }
         }
         checkedClassNames.put(name, presentInArchive);
      }
      
      if (!presentInArchive)
      {
         throw new ClassNotFoundException("Class not found in ShrinkWrap archive: " + name);
      }
      return super.loadClass(name);
   }

   @Override
   public URL getResource(String name)
   {
      // bypass parent; the only place we should look for resources is in the archives
      return findResource(name);
   }
   
   public void close() throws IOException
   {
      synchronized (this)
      {
         for (InputStream stream : openedStreams)
         {
            try
            {
               stream.close();
            }
            catch (Exception e)
            {
               log.warning("Could not close opened inputstream: " + e);
            }
         }
         openedStreams.clear();
      }
   }
}
