package org.jboss.arquillian.api.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   containers = "~ org\\.jboss\\.arquillian\\.container\\.jbossas\\.(managed|remote|embedded)_5(_[0-9]+)*",
   environments = JavaEE6Environment.class
)
public final class JavaEE5Environment implements ExecutionEnvironment {}