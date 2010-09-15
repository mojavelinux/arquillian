package org.jboss.arquillian.api.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiresEnvironment;
import org.jboss.arquillian.api.environment.spec.JavaEE6Environment;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RequiresEnvironment(JavaEE6Environment.class)
public @interface RequiresJavaEE6 {}