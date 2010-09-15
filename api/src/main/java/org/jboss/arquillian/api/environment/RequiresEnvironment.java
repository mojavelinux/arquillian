package org.jboss.arquillian.api.environment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to declare which execution environment an integration test
 * requires to function properly.
 * 
 * <p>
 * A configuration identifier can be specified to require only a specific
 * configurations of an execution environment. This may be useful if the
 * different configurations effectively create unique execution environments.
 * Container configurations are defined using the Arquillian container
 * configuration mechanism (i.e., arquillian.xml).
 * </p>
 * 
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface RequiresEnvironment
{
   Class<? extends ExecutionEnvironment> value();
   String[] configuration() default "";
   
   /**
    * Wraps one or more required execution environment alternatives. Used when
    * the required environments are not hierarchically related or when
    * permitting multiple configurations of one or more environments.
    */
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
   @Documented
   @interface Alternatives
   {
      RequiresEnvironment[] value();
   }
}
