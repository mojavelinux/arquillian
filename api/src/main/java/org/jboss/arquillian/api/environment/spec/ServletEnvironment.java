package org.jboss.arquillian.api.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   containers = "~ org\\.jboss\\.arquillian\\.container\\.(jetty|tomcat)\\..*",
   environments = JavaEE5Environment.class
)
public final class ServletEnvironment implements ExecutionEnvironment {}
