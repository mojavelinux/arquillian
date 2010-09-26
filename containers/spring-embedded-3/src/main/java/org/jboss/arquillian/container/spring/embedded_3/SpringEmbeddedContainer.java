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
package org.jboss.arquillian.container.spring.embedded_3;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.spring.embedded_3.shrinkwrap.StrictShrinkWrapClassLoader;
import org.jboss.arquillian.protocol.local.LocalMethodExecutor;
import org.jboss.arquillian.spi.Configuration;
import org.jboss.arquillian.spi.ContainerMethodExecutor;
import org.jboss.arquillian.spi.Context;
import org.jboss.arquillian.spi.DeployableContainer;
import org.jboss.arquillian.spi.DeploymentException;
import org.jboss.arquillian.spi.LifecycleException;
import org.jboss.arquillian.testenricher.spring.AutowireMode;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * An embedded Arquillian container for Spring
 *
 * <p>This {@link DeployableContainer} implementation provides an embedded
 * container that bootstraps the Spring bean container.</p>
 *
 * <p>The Spring bean container is started in the deploy() method and shutdown
 * in the undeploy() method.</p>
 *
 * <p>The current thread's context ClassLoader is also replaced with a
 * ClassLoader implementation that can discover resources in a ShrinkWrap
 * archive.</p>
 *
 * @author <a href="mailto:dan.allen@mojavelinux.com">Dan Allen</a>
 */
public class SpringEmbeddedContainer implements DeployableContainer
{
   private SpringEmbeddedConfiguration springContainerConfig;
   
   /**
    * @see org.jboss.arquillian.spi.DeployableContainer#setup(org.jboss.arquillian.spi.Configuration)
    */
   public void setup(Context context, Configuration configuration)
   {
      springContainerConfig = configuration.getContainerConfig(SpringEmbeddedConfiguration.class);
   }
   
   /**
    * @see org.jboss.arquillian.spi.DeployableContainer#start()
    */
   public void start(Context context) throws LifecycleException
   {
   }

   /**
    * @see org.jboss.arquillian.spi.DeployableContainer#stop()
    */
   public void stop(Context context) throws LifecycleException
   {
   }

   /**
    * @see org.jboss.arquillian.spi.DeployableContainer#deploy(org.jboss.shrinkwrap.api.Archive)
    */
   public ContainerMethodExecutor deploy(Context context, final Archive<?> archive)
         throws DeploymentException
   {
      List<String> waivedPackages = new ArrayList<String>();
      // don't be strict about loading classes from the Spring Framework
      waivedPackages.add("org.springframework");
      StrictShrinkWrapClassLoader cl = new StrictShrinkWrapClassLoader(archive.getClass().getClassLoader(), waivedPackages, archive);
      
      ArchivePath descriptorPath = ArchivePaths.create(springContainerConfig.getConfigLocation());
      if (archive.contains(descriptorPath))
      {
         AutowireMode autowireMode = springContainerConfig.getAutowireMode();
         boolean markerConfiguration = false;
         if (!AutowireMode.ANNOTATED.equals(autowireMode) && archive.get(descriptorPath).getAsset().equals(EmptyAsset.INSTANCE))
         {
            markerConfiguration = true;
            autowireMode = AutowireMode.ANNOTATED;
         }
         GenericApplicationContext applicationCtx = createApplicationContext(autowireMode);
         applicationCtx.setClassLoader(cl);
         if (!markerConfiguration)
         {
            createBeanDefinitionReader(applicationCtx, cl)
               .loadBeanDefinitions(springContainerConfig.getConfigLocation());
         }
         if (AutowireMode.ANNOTATED.equals(autowireMode) && applicationCtx instanceof AnnotationConfigApplicationContext)
         {
            ((AnnotationConfigApplicationContext) applicationCtx).scan(springContainerConfig.getScanBasePackage());
         }
         applicationCtx.refresh();
         context.add(ConfigurableApplicationContext.class, applicationCtx);
         context.add(AutowireMode.class, autowireMode);
      }

      return new LocalMethodExecutor();
   }

   /**
    * @see org.jboss.arquillian.spi.DeployableContainer#undeploy(org.jboss.shrinkwrap.api.Archive)
    */
   public void undeploy(Context context, Archive<?> archive) throws DeploymentException
   {
      ConfigurableApplicationContext applicationCtx = context.get(ConfigurableApplicationContext.class);
      if (applicationCtx != null)
      {
         applicationCtx.close();
      }
   }

   protected GenericApplicationContext createApplicationContext(AutowireMode autowire)
   {
      if (AutowireMode.ANNOTATED.equals(autowire))
      {
         return new AnnotationConfigApplicationContext();
      }
      return new GenericApplicationContext();
   }

   protected BeanDefinitionReader createBeanDefinitionReader(final GenericApplicationContext ctx, final ClassLoader cl)
   {
      XmlBeanDefinitionReader defReader = new XmlBeanDefinitionReader(ctx);
      // setting the classloader here causes Spring to attempt to load each bean class that is referenced in the configuration file
//      defReader.setBeanClassLoader(cl);
      return defReader;
   }
}
