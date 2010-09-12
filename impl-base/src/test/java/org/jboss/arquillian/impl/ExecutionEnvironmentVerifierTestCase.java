package org.jboss.arquillian.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Dan Allen
 */
public class ExecutionEnvironmentVerifierTestCase
{
   private static final String JBOSSAS_REMOTE_6 = "org.jboss.arquillian.container.jbossas.remote_6";
   private static final String OPENEJB_EMBEDDED_1_1 = "org.jboss.arquillian.container.openejb.embedded_1_1";
   private static final String GLASSFISH_EMBEDDED_3 = "org.jboss.arquillian.container.glassfish.embedded_3";
   
   @Test
   public void shouldVerifyCompatibleEnvironmentFromRequiresEnvironmentAnnotation()
   {
      assertTrue(new ExecutionEnvironmentVerifier(JBOSSAS_REMOTE_6).verifyTargetContainerProvidesRequiredEnvironment(ClassWithRequiresEnvironmentAnnotation.class));
      assertTrue(new ExecutionEnvironmentVerifier(GLASSFISH_EMBEDDED_3).verifyTargetContainerProvidesRequiredEnvironment(ClassWithRequiresEnvironmentAnnotation.class));
   }
   
   @Test
   public void shouldNotVerifyIncompatibleEnvironmentFromRequiresEnvironmentAnnotation()
   {
      assertFalse(new ExecutionEnvironmentVerifier(OPENEJB_EMBEDDED_1_1).verifyTargetContainerProvidesRequiredEnvironment(ClassWithRequiresEnvironmentAnnotation.class));
   }
   
   @Test
   public void shouldVerifyCompatibleEnvironmentFromRequiresEnvironmentRuleAnnotation()
   {
      assertTrue(new ExecutionEnvironmentVerifier(JBOSSAS_REMOTE_6).verifyTargetContainerProvidesRequiredEnvironment(ClassWithRequiresEnvironmentRuleAnnotation.class));
      assertTrue(new ExecutionEnvironmentVerifier(GLASSFISH_EMBEDDED_3).verifyTargetContainerProvidesRequiredEnvironment(ClassWithRequiresEnvironmentRuleAnnotation.class));
   }
   
   @Test
   public void shouldNotVerifyIncompatibleEnvironmentFromRequiresEnvironmentRuleAnnotation()
   {
      assertFalse(new ExecutionEnvironmentVerifier(OPENEJB_EMBEDDED_1_1).verifyTargetContainerProvidesRequiredEnvironment(ClassWithRequiresEnvironmentRuleAnnotation.class));
   }
}
