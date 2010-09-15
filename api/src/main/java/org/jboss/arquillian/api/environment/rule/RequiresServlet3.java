package org.jboss.arquillian.api.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiresEnvironment;
import org.jboss.arquillian.api.environment.spec.Servlet3Environment;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RequiresEnvironment(Servlet3Environment.class)
public @interface RequiresServlet3 {}