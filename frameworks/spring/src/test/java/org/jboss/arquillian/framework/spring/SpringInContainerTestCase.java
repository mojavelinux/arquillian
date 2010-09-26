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

import java.util.logging.Logger;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.framework.spring.bean.EchoBean;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests that Spring beans work through the Arquillian lifecycle.
 * 
 * @author Dan Allen
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class SpringInContainerTestCase
{
   private static final Logger log = Logger.getLogger(SpringInContainerTestCase.class.getName());

   @Deployment
   public static WebArchive createDeployment()
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
         .addClass(EchoBean.class)
         .addWebResource("applicationContext.xml");
   }

   private EchoBean echoBean;

   @Test
   public void testEchoBean()
   {
      Assert.assertNotNull("Bean was not injected", echoBean);

      // Define the input and expected outcome
      final String expected = "Holler";

      // Invoke the bean
      final String received = echoBean.echo(expected);

      // Test
      Assert.assertEquals("Expected output was not equal by value", expected, received);
      Assert.assertTrue("Expected output was not equal by reference", expected == received);
      log.info("Got expected result from bean: " + received);

      Assert.assertEquals("Expected volume to be assigned and incremented by one", 5, echoBean.getVolume());
   }

   /**
    * Setter method indicates that we want to receive an injection.
    */
   public void setEchoBean(EchoBean echoBean)
   {
      this.echoBean = echoBean;
   }
}
