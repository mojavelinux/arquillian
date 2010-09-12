package com.acme.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   containers =
   {
      "~ org\\.jboss\\.arquillian\\.container\\.jbossas\\.(managed|remote|embedded)_6(_[0-9]+)*"
   }
)
public final class JBossAS5Container implements ExecutionEnvironment {}
