package org.jboss.arquillian.api.environment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation used to declare which execution environment an integration
 * test requires to function properly.
 *
 * <p>One or more configuration identifiers can be specified to require only
 * specific configurations of an execution environment. This may be useful if
 * the different configurations effectively create unique execution
 * environments. Container configurations are defined using the Arquillian
 * container configuration mechanism (i.e., arquillian.xml).</p>
 * 
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface RequiresEnvironment
{
   Class<? extends ExecutionEnvironment> value();
   String[] configurations() default {};
}