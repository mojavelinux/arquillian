package org.jboss.arquillian.api.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiredEnvironmentStereotype;
import org.jboss.arquillian.api.environment.spec.EJB3_1Environment;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EJB3_1Environment
@RequiredEnvironmentStereotype
public @interface RequiresEJB3_1 {}
