package org.jboss.arquillian.api.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   containers = "~ org\\.jboss\\.arquillian\\.container\\.openejb\\.embedded_3(_[0-9]+)*",
   environments = EJB3_1Environment.class
)
public final class EJB3Environment implements ExecutionEnvironment {}
