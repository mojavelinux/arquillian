package org.jboss.arquillian.api.environment.spec;

import org.jboss.arquillian.api.environment.ExecutionEnvironment;
import org.jboss.arquillian.api.environment.ProvidedBy;

@ProvidedBy(
   environments = JavaEE6Environment.class
)
public final class EJB3_1Environment implements ExecutionEnvironment {}
