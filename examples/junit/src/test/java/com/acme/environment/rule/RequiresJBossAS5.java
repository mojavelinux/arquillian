package com.acme.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiresEnvironment;

import com.acme.environment.spec.JBossAS5Container;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RequiresEnvironment(JBossAS5Container.class)
public @interface RequiresJBossAS5 {}