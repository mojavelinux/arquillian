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
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface RequiresEnvironment
{
   Class<? extends ExecutionEnvironment> value();
}
