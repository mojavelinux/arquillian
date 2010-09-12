package org.jboss.arquillian.api.environment;

/**
 * A semantic interface that is used to define a type that represents an
 * execution environment.
 * 
 * <p>
 * Implementations are expected to be final and annotated with metadata, such as
 * &#064;ProvidedBy, that supplies an analyzer with information to determine
 * which containers or broader environments provide the execution environment.
 * </p>
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * &#064;ProvidedBy(containers = &quot;&tilde; org\\.jboss\\.arquillian\\.container\\.jbossas\\.(managed|remote|embedded)_6(_[0-9]+)*&quot;)
 * public final class JavaEE6Container implements ExecutionEnvironment
 * {
 * }
 * </pre>
 * 
 * @author Dan Allen
 */
public interface ExecutionEnvironment {}
