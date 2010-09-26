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

import org.jboss.arquillian.spi.ClassContextAppender;
import org.jboss.arquillian.spi.Context;
import org.jboss.arquillian.testenricher.spring.SpringTestEnricher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Makes the Spring {@link ConfigurableApplicationContext} available to the
 * Spring test enricher.
 * 
 * <p>
 * Retrieves the {@link WebApplicationContext} from the {@link ContextLoader},
 * verifies it's an instance of ConfigurableWebApplicationContext and, if so,
 * stores it in Arquillian's class context to make it available to the Spring
 * test enricher.
 * </p>
 * 
 * @author Dan Allen
 * @see SpringTestEnricher
 */
public class SpringConfigurableApplicationContextAppender implements ClassContextAppender
{
   public void append(Context context)
   {
      WebApplicationContext applicationCtx = ContextLoader.getCurrentWebApplicationContext();
      if (applicationCtx instanceof ConfigurableApplicationContext)
      {
         context.add(ConfigurableApplicationContext.class, (ConfigurableApplicationContext) applicationCtx);
      }
   }
}
