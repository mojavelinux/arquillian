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
import org.jboss.arquillian.container.spring.embedded_3.bean.AutoWiredBean;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Emulate the auto-scanning feature of CDI by presence of a configuration file
 * (in this case empty) to mean that component and injection point scanning
 * should be automatically enabled.
 * 
 * @author Dan Allen
 */
@RunWith(Arquillian.class)
public class SpringEmbeddedAutoAnnotationConfigTestCase
{
   /**
    * Define the Arquillian deployment
    */
   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
         .addClasses(AutoWiredBean.class)
         .addResource(EmptyAsset.INSTANCE, "applicationContext.xml");
   }

   @Autowired
   private AutoWiredBean bean;
   
   /**
    * Ensures the annotation-based injection works.
    */
   @Test
   public void shouldInjectAutoWiredBean()
   {
      Assert.assertNotNull("Bean was not injected", bean);
      Assert.assertEquals("Spring", bean.getFramework());
   }
   
}
