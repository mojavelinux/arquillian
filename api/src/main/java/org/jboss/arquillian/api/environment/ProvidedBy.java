package org.jboss.arquillian.api.environment;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the containers and runtimes that provide the runtime qualified by
 * the target execution environment type.
 * 
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ProvidedBy
{
   /**
    * An array of regular expression strings that match the package names of one
    * or more DeployableContainer SPI implementation classes, declaring which of
    * those implementations provide deployment to the execution environment type
    * being annotated.
    */
   String[] containers() default {};
   
   /**
    * An array of execution environments interfaces collectively providing the
    * runtime qualified by the target annotation.
    */
   Class<? extends Annotation>[] environments() default {};
}
