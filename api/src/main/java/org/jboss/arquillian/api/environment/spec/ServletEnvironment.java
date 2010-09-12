package org.jboss.arquillian.api.environment.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@ExecutionEnvironment
@ProvidedBy(
   containers = "~ org\\.jboss\\.arquillian\\.container\\.(jetty|tomcat)\\..*",
   environments = JavaEE5Environment.class
)
public @interface ServletEnvironment {}
