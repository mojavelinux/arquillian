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

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Checks that the Spring embedded container cannot see classes and resources
 * that are outside of the ShrinkWrap archive. Also ensures that the
 * applicationContext.xml being loaded is the one from the ShrinkWrap archive.
 * 
 * @author Dan Allen
 */
@RunWith(Arquillian.class)
public class SpringEmbeddedFailureTestCase implements ApplicationContextAware
{
   /**
    * Define the Arquillian deployment
    */
   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
         .addResource("bad-applicationContext.xml", "applicationContext.xml");
   }

   private ApplicationContext applicationContext;
   
   /**
    * Ensure that Spring can't load a bean with a type not present in the
    * ShrinkWrap archive. We can't test this as an injection since we have no
    * way to trap the exception to indicate that it's expected.
    */
   @Test(expected = CannotLoadBeanClassException.class)
   public void shouldNotLoadUnavailableBean()
   {
      Assert.assertNotNull("Application context was not injected", applicationContext);
      applicationContext.getBean("unavailableBean");
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
   {
      this.applicationContext = applicationContext;
   }
}
