package org.jboss.arquillian.impl;

import org.jboss.arquillian.api.environment.RequiresEnvironment;
import org.jboss.arquillian.api.environment.spec.JavaEE6Environment;

@RequiresEnvironment(JavaEE6Environment.class)
public class ClassWithRequiresEnvironmentAnnotation {}