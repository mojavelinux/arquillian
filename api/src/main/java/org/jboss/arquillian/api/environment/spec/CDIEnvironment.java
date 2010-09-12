package org.jboss.arquillian.api.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   containers =
   {
      "~ org\\.jboss\\.arquillian\\.container\\.weld\\..*",
      "~ org\\.jboss\\.arquillian\\.container\\.openwebbeans\\..*"
   },
   environments = JavaEE6Environment.class
)
public final class CDIEnvironment implements ExecutionEnvironment {}
