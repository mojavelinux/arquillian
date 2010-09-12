package com.acme.environment.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.RequiredEnvironmentStereotype;

import com.acme.environment.spec.JBossAS6Container;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JBossAS6Container
@RequiredEnvironmentStereotype
public @interface RequiresJBossAS6 {}