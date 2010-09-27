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

import org.jboss.arquillian.spi.AuxiliaryArchiveAppender;
import org.jboss.arquillian.spi.ClassContextAppender;
import org.jboss.arquillian.spi.TestEnricher;
import org.jboss.arquillian.testenricher.spring.SpringTestEnricher;
import org.jboss.arquillian.testenricher.spring.AutowireMode;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * An auxiliary archive appender that bundles a web-fragment.xml resource, to
 * activate Spring, and two service providers, the Spring test enricher and a
 * class context appender that makes the Spring application context available to
 * the test enricher.
 * 
 * @author Dan Allen
 */
public class SpringIntegrationAuxiliaryArchiveAppender implements AuxiliaryArchiveAppender
{
   public Archive<?> createAuxiliaryArchive()
   {
      return ShrinkWrap.create(JavaArchive.class, "arquillian-spring-int.jar")
         .addClasses(SpringTestEnricher.class, AutowireMode.class, SpringConfigurableApplicationContextAppender.class)
         .addServiceProvider(TestEnricher.class, SpringTestEnricher.class)
         .addServiceProvider(ClassContextAppender.class, SpringConfigurableApplicationContextAppender.class)
         .addManifestResource("web-fragment.xml");
   }
}
