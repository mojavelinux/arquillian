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
package org.jboss.arquillian.framework.spring;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipFile;

import org.jboss.arquillian.spi.AuxiliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * An auxiliary archive appender that merges all the jars in directory specified
 * by the arquillian.springLibrariesPath system properties (with a fallback of
 * target/spring-libs) into a single archive.
 * 
 * @author Dan Allen
 */
public class SpringLibrariesAuxiliaryArchiveAppender implements AuxiliaryArchiveAppender
{
   public static final String SPRING_FRAMEWORK_BASE_PACKAGE = "org.springframework";
   
   public static final String SPRING_LIBRARIES_PATH_PROPERTY = "arquillian.springLibrariesPath";
   
   public static final String DEFAULT_SPRING_LIBRARIES_PATH = "target/spring-libs";
   
   public Archive<?> createAuxiliaryArchive()
   {
      final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "arquillian-spring-libs.jar");
      // Play some tricks to get ShrinkWrap to transfer classes w/o attempting to load them
      archive.addPackages(true, new Filter<ArchivePath>() {

         public boolean include(ArchivePath classNamePath)
         {
            String classResource = classNamePath.get().substring(1);
            URL resource = Thread.currentThread().getContextClassLoader().getResource(classResource);
            // not sure why it's null sometimes
            if (resource != null)
            {
               archive.addResource(resource, classResource);
            }
            return false;
         }
         
      }, Package.getPackage(SPRING_FRAMEWORK_BASE_PACKAGE));
      // Another approach to transferring classes
      //mergeJars(archive, getSpringLibrariesPath());
      return archive;
   }
   
   protected void mergeJars(JavaArchive archive, String location)
   {
      File libDir = new File(location);
      if (!libDir.isDirectory())
      {
         throw new IllegalArgumentException("Expecting " + location + " to be a directory");
      }
      
      for (File jar : libDir.listFiles(new JarFileFilter()))
      {
         archive.merge(importJar(jar));
      }
   }
   
   protected JavaArchive importJar(File location)
   {
      try
      {
         return ShrinkWrap.create(ZipImporter.class, "import.jar").importZip(new ZipFile(location)).as(JavaArchive.class);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   protected String getSpringLibrariesPath()
   {
      String path = System.getProperty(SPRING_LIBRARIES_PATH_PROPERTY);
      if (path == null)
      {
         path = DEFAULT_SPRING_LIBRARIES_PATH;
      }
      
      return path;
   }
   
   protected static class JarFileFilter implements FileFilter
   {
      public boolean accept(File file)
      {
         return file.getName().endsWith(".jar");
      }
   }
}
