package org.jboss.arquillian.api.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   containers = "~ org\\.jboss\\.arquillian\\.container\\.weld\\.ee\\..*",
   environments = JavaEE6Environment.class)
public final class CDIEnterpriseEnvironment implements ExecutionEnvironment {}
