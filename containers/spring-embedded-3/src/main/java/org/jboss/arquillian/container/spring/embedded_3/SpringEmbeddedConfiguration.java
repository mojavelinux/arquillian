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
package org.jboss.arquillian.container.spring.embedded_3;

import org.jboss.arquillian.spi.ContainerConfiguration;
import org.jboss.arquillian.spi.ContainerProfile;
import org.jboss.arquillian.testenricher.spring.AutowireMode;

/**
 * Spring Embedded container configuration
 *
 * @author Dan Allen
 * @version $Revision: $
 */
public class SpringEmbeddedConfiguration implements ContainerConfiguration
{
   private String configLocation = "applicationContext.xml";
   
   private String autowire = "byType";
   
   private String scanBasePackage = "";

   /**
    * @see org.jboss.arquillian.spi.ContainerConfiguration#getContainerProfile()
    */
   public ContainerProfile getContainerProfile()
   {
      return ContainerProfile.STANDALONE;
   }

   /**
    * Gets the location of the configuration file within the ShrinkWrap archive.
    * 
    * @see SpringEmbeddedConfiguration#setConfigLocation(String)
    */
   public String getConfigLocation()
   {
      return configLocation;
   }

   /**
    * Sets the location <strong>within the ShinkWrap archive</strong> to look for the Spring
    * configuration file.
    * 
    * <p>The archives are searched recursively, if applicable (e.g., WAR and EAR). The default
    * location is "applicationContext.xml" at the root of the archives. If the configuration
    * file is not found, enrichment is disabled.</p>
    * 
    * @param configLocation Archive path where the Spring configuration file is located
    */
   public void setConfigLocation(String configLocation)
   {
      this.configLocation = configLocation;
   }

   /**
    * Gets the wiring strategy used to inject into the test class.
    * 
    * @see SpringEmbeddedConfiguration#setAutowire(String)
    */
   public String getAutowire()
   {
      return autowire;
   }

   /**
    * Sets the wiring strategy used to inject into the test class.
    * 
    * <p>
    * Possible values are:
    * </p>
    * 
    * <ul>
    * <li>byName - injects a bean with the id equivalent to the name of the bean
    * property</li>
    * <li>byType - injects a bean with the same type as the bean property</li>
    * <li>byAnnotated - injects a bean based on the @Autowired or @Inject
    * annotations</li>
    * <li>disabled - dependency injection disabled</li>
    * </ul>
    * 
    * <p>
    * Defaults to "byType". An empty Spring configuration file in the archive
    * automatically changes the strategy to "byAnnotated".
    * </p>
    * 
    * <p>
    * When using the byType or byName strategies, the test class must have write
    * methods (setters) which accept the injections.
    * </p>
    * 
    * @param autowire Key of the autowire strategy
    */
   public void setAutowire(String autowire)
   {
      this.autowire = autowire;
   }
   
   /**
    * Gets the base package to scan recursively for components.
    * 
    * @see SpringEmbeddedConfiguration#setScanBasePackage(String)
    */
   public String getScanBasePackage()
   {
      return scanBasePackage;
   }

   /**
    * Sets the base package to scan recursively for components and injection
    * points.
    * 
    * <p>
    * Only relevant when the value of <code>autowire</code> is "byAnnotated".
    * Defaults to empty string, which scans all packages in archive.
    * </p>
    * 
    * @param scanBasePackage Package to scan recursively for components and
    *           injection points
    */
   public void setScanBasePackage(String scanBasePackage)
   {
      this.scanBasePackage = scanBasePackage;
   }
   
   /**
    * Convert the <code>autowire</code> property to an {@link AutowireMode} value.
    */
   public AutowireMode getAutowireMode()
   {
      return AutowireMode.fromKey(autowire);
   }
}
