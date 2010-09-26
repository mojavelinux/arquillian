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
package org.jboss.arquillian.testenricher.spring;

import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

/**
 * An Enum that represents the various autowire strategies in Spring.
 * 
 * <p>
 * Each value has a key and flag. The key is used in a user-facing value that is
 * used in configuration files. The flag is the value that Spring uses in its
 * APIs to communicate the strategy.
 * </p>
 * 
 * <p>
 * The default autowire mode is BY_TYPE.
 * </p>
 * 
 * @author Dan Allen
 */
public enum AutowireMode
{
   BY_TYPE("byType", AbstractAutowireCapableBeanFactory.AUTOWIRE_BY_TYPE),
   BY_NAME("byName", AbstractAutowireCapableBeanFactory.AUTOWIRE_BY_NAME),
   ANNOTATED("annotated", AbstractAutowireCapableBeanFactory.AUTOWIRE_NO),
   DISABLED("disabled", AbstractAutowireCapableBeanFactory.AUTOWIRE_NO);
   
   private String key;
   private int flag;
   
   private AutowireMode(String key, int flag)
   {
      this.key = key;
      this.flag = flag;
   }
   
   public String key()
   {
      return key;
   }
   
   public int flag()
   {
      return flag;
   }
   
   public static AutowireMode getDefault()
   {
      return AutowireMode.BY_TYPE;
   }
   
   public static AutowireMode fromKey(String key)
   {
      if ("".equals(key))
      {
         return getDefault();
      }

      for (AutowireMode candidate : values())
      {
         if (candidate.key.equals(key))
         {
            return candidate;
         }
      }

      return null;
   }
}
