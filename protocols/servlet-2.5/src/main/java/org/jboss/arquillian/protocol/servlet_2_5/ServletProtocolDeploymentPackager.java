/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.protocol.servlet_2_5;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.arquillian.spi.DeploymentPackager;
import org.jboss.arquillian.spi.TestDeployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.asset.ArchiveAsset;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * ServletProtocolDeploymentPackager
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @author Dan Allen
 * @version $Revision: $
 */
public class ServletProtocolDeploymentPackager implements DeploymentPackager
{
   // NOTE we don't want to merge just any web fragment because that could mislead the test writer
   private static final ArchivePath AUXILARY_WEB_XML_JAR_PATH = ArchivePaths.create("META-INF/auxiliary-web.xml");
   
   private static final ArchivePath WEB_XML_WAR_PATH = ArchivePaths.create("WEB-INF/web.xml");
   
   /* (non-Javadoc)
    * @see org.jboss.arquillian.spi.DeploymentPackager#generateDeployment(org.jboss.arquillian.spi.TestDeployment)
    */
   public Archive<?> generateDeployment(TestDeployment testDeployment)
   {
      Archive<?> protocol = new ProtocolDeploymentAppender().createAuxiliaryArchive();
      
      Archive<?> applicationArchive = testDeployment.getApplicationArchive();
      Collection<Archive<?>> auxiliaryArchives = testDeployment.getAuxiliaryArchives();
      
      if (EnterpriseArchive.class.isInstance(applicationArchive))
      {
         return handleArchive(EnterpriseArchive.class.cast(applicationArchive), auxiliaryArchives, protocol);
      }

      if (WebArchive.class.isInstance(applicationArchive))
      {
         return handleArchive(WebArchive.class.cast(applicationArchive), auxiliaryArchives, protocol);
      }

      if (JavaArchive.class.isInstance(applicationArchive))
      {
         return handleArchive(JavaArchive.class.cast(applicationArchive), auxiliaryArchives, protocol);
      }

      throw new IllegalArgumentException(ServletProtocolDeploymentPackager.class.getName()  + 
            " can not handle archive of type " +  applicationArchive.getClass().getName());
   }

   private Archive<?> handleArchive(WebArchive applicationArchive, Collection<Archive<?>> auxiliaryArchives, Archive<?> protocol) 
   {
      return importProtocolArchive(applicationArchive, protocol).addLibraries(auxiliaryArchives.toArray(new Archive<?>[0]));
   }

   private Archive<?> handleArchive(JavaArchive applicationArchive, Collection<Archive<?>> auxiliaryArchives, Archive<?> protocol)
   {
      return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
            .addModule(applicationArchive)
            .addModule(importProtocolArchive(ShrinkWrap.create(WebArchive.class, "test.war"), protocol))
            .addLibraries(auxiliaryArchives.toArray(new Archive[0]));
   }

   private Archive<?> handleArchive(EnterpriseArchive applicationArchive, Collection<Archive<?>> auxiliaryArchives, Archive<?> protocol)
   {
      WebArchive applicationWebArchive = null;
      Map<ArchivePath, Node> rootNodes = applicationArchive.getContent(new Filter<ArchivePath>()
      {
         public boolean include(ArchivePath object)
         {
            return object.getParent() == null || object.getParent().get().equals("/");
         }
      });
      
      for (Map.Entry<ArchivePath, Node> c : rootNodes.entrySet())
      {
         Asset a = c.getValue().getAsset();
         if (a instanceof ArchiveAsset && ((ArchiveAsset) a).getArchive() instanceof WebArchive)
         {
            applicationWebArchive = importProtocolArchive((WebArchive) ((ArchiveAsset) a).getArchive(), protocol);
            break;
         }
      }
      
      if (applicationWebArchive == null)
      {
         applicationArchive.addModule(importProtocolArchive(ShrinkWrap.create(WebArchive.class, "test.war"), protocol));
      }
      
      return applicationArchive.addLibraries(auxiliaryArchives.toArray(new Archive<?>[0]));
   }
   
   private WebArchive importProtocolArchive(WebArchive to, Archive<?> protocol)
   {
      if (protocol.contains(AUXILARY_WEB_XML_JAR_PATH))
      {
         if (to.contains(WEB_XML_WAR_PATH))
         {
            to.setWebXML(mergeWebXmlFragment(to.get(WEB_XML_WAR_PATH).getAsset(),
                  protocol.get(AUXILARY_WEB_XML_JAR_PATH).getAsset()));
         }
         else
         {
            to.setWebXML(protocol.get(AUXILARY_WEB_XML_JAR_PATH).getAsset());
         }
         protocol.delete(AUXILARY_WEB_XML_JAR_PATH);
      }
      to.addLibrary(protocol);
      return to;
   }
   
   private Asset mergeWebXmlFragment(Asset master, Asset fragment)
   {
      try
      {
         DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document masterDoc = b.parse(master.openStream());
         masterDoc.setXmlStandalone(true);
         Document fragmentDoc = b.parse(fragment.openStream());
         NodeList children = fragmentDoc.getDocumentElement().getChildNodes();
         for (int i = 0, len = children.getLength(); i < len; i++)
         {
            masterDoc.getDocumentElement().appendChild(masterDoc.importNode(children.item(i), true));
         }
         
         Transformer t = TransformerFactory.newInstance().newTransformer();
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         t.transform(new DOMSource(masterDoc), new StreamResult(os));
         return new ByteArrayAsset(os.toByteArray());
      }
      catch (Exception e)
      {
         throw new RuntimeException("Failed to merge protocol web-fragment.xml into application web.xml: ", e);
      }
   }
}
