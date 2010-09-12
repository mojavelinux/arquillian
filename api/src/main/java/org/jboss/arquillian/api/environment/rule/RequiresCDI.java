package org.jboss.arquillian.api.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiresEnvironment;
import org.jboss.arquillian.api.environment.spec.CDIEnvironment;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RequiresEnvironment(CDIEnvironment.class)
public @interface RequiresCDI {}
