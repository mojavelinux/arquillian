package org.jboss.arquillian.api.environment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to declare an annotation which is generalizing an
 * execution environment an integration test requires to function properly.
 * 
 * <p>The execution environment is specified as a sibling annotation.</p>
 * 
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface RequiredEnvironmentStereotype {}
