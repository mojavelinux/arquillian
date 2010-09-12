package org.jboss.arquillian.api.environment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A annotation that is used to mark an annotation type that represents an
 * execution environment.
 * 
 * <p>
 * For an annotation to represent an execution environment, it must be annotated
 * with &#064;ExecutionEnvironment as well as metadata, such as
 * &#064;ProvidedBy, that supplies an analyzer with information to determine
 * which containers or broader environments provide the execution environment.
 * </p>
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * &#064;@Retention(RetentionPolicy.RUNTIME)
 * &#064;@Target(ElementType.ANNOTATION_TYPE)
 * &#064;ExecutionEnvironment
 * &#064;ProvidedBy(containers = &quot;&tilde; org\\.jboss\\.arquillian\\.container\\.jbossas\\.(managed|remote|embedded)_6(_[0-9]+)*&quot;)
 * public @interface JavaEE6Environment {}
 * </pre>
 * 
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Documented
public @interface ExecutionEnvironment {}
