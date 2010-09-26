/*
 * JBoss, Community-driven Open Source Middleware
 * Copyright 2010, JBoss by Red Hat, Inc., and individual contributors
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
package org.jboss.arquillian.testenricher.spring;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.jboss.arquillian.spi.Context;
import org.jboss.arquillian.spi.TestEnricher;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * A Spring bean injection enricher adaptor that...
 *
 * @author <a href="mailto:dan.allen@mojavelinux.com">Dan Allen</a>
 */
public class SpringTestEnricher implements TestEnricher
{
   private static final Logger log = Logger.getLogger(SpringTestEnricher.class.getName());
   
   public void enrich(Context context, Object testCase)
   {
      ConfigurableApplicationContext applicationCtx = context.get(ConfigurableApplicationContext.class);
      if (applicationCtx != null)
      {
         AutowireMode autowire = context.get(AutowireMode.class);
         if (autowire == null)
         {
            autowire = AutowireMode.getDefault();
         }
         // NOTE for annotation-based injection, we can simply use autowireBean(testCase); also takes care of ApplicationContextAware
         applicationCtx.getBeanFactory().autowireBeanProperties(testCase, autowire.flag(), false);
         
         // strange this injection isn't satisfied by auto-wire
         if (testCase instanceof ApplicationContextAware)
         {
            ((ApplicationContextAware) testCase).setApplicationContext(applicationCtx);
         }
      }
      else
      {
         log.warning("ConfigurableApplicationContext not found in Arquillian context. Spring injections will be skipped.");
      }
   }

   public Object[] resolve(Context context, Method method)
   {
      return new Object[] {};
   }
}
