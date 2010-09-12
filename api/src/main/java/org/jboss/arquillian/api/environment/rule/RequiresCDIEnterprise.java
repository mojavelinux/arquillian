package org.jboss.arquillian.api.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiredEnvironmentStereotype;
import org.jboss.arquillian.api.environment.spec.CDIEnterpriseEnvironment;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CDIEnterpriseEnvironment
@RequiredEnvironmentStereotype
public @interface RequiresCDIEnterprise {}
