package org.jboss.arquillian.impl;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;
import org.jboss.arquillian.api.environment.RequiresEnvironment;

// TODO impl cache for targetContainerId -> environments it provides
// ContainerProvidesCache
public class ExecutionEnvironmentVerifier
{
   private boolean suitable;
   private String targetContainerId;

   public ExecutionEnvironmentVerifier(String targetContainerId)
   {
      this.targetContainerId = targetContainerId;
   }

   public boolean verifyTargetContainerProvidesRequiredEnvironment(Class<?> testClass)
   {
      Class<? extends ExecutionEnvironment> requiredEnv = resolveRequiredEnvironment(testClass);
      if (requiredEnv != null)
      {
         suitable = false;
         verifyContainerProvidesEnvironment(targetContainerId, requiredEnv);
      }
      else
      {
         suitable = true;
      }
      // return or void?
      return suitable;
   }

   public boolean isSuitable()
   {
      return suitable;
   }

   protected Class<? extends ExecutionEnvironment> resolveRequiredEnvironment(Class<?> testClass)
   {
      if (testClass.isAnnotationPresent(RequiresEnvironment.class))
      {
         return testClass.getAnnotation(RequiresEnvironment.class).value();
      }
      else
      {
         for (Annotation candidate : testClass.getAnnotations())
         {
            Class<? extends Annotation> candidateType = candidate.annotationType();
            if (candidateType.isAnnotationPresent(RequiresEnvironment.class))
            {
               return candidateType.getAnnotation(RequiresEnvironment.class).value();
            }
         }
      }
      return null;
   }

   protected void verifyContainerProvidesEnvironment(String containerId, Class<? extends ExecutionEnvironment> env)
   {
      if (!env.isAnnotationPresent(ProvidedBy.class))
      {
         throw new IllegalStateException("Execution environment class must be annotated with @ProvidedBy");
      }

      ProvidedBy providers = env.getAnnotation(ProvidedBy.class);
      if (matchesContainerExpression(containerId, providers.containers()))
      {
         suitable = true;
         return;
      }
      else
      {
         for (Class<? extends ExecutionEnvironment> penv : providers.environments())
         {
            // FIXME verify that we aren't visiting an environment again as optimization and to avoid recursion (in case of user error) 
            verifyContainerProvidesEnvironment(containerId, penv);
            if (suitable)
            {
               return;
            }
         }
      }
   }

   protected boolean matchesContainerExpression(String targetContainerId, String[] expressions)
   {
      for (String expression : expressions)
      {
         if (expression.startsWith("~ "))
         {
            if (matchesContainerRegexp(targetContainerId, expression.substring(2)))
            {
               return true;
            }
         }
         else
         {
            if (matchesContainerExactly(targetContainerId, expression))
            {
               return true;
            }
         }
      }
      return false;
   }

   protected boolean matchesContainerRegexp(String targetContainerId, String regexp)
   {
      return targetContainerId.matches(regexp);
   }

   protected boolean matchesContainerExactly(String targetContainerId, String candidateContainerId)
   {
      return targetContainerId.equals(candidateContainerId);
   }
}
