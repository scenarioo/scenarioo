package org.scenarioo.rest.base.logging;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to bind the logging filter to our application resources
 */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface ApplyRequestLogging {
}
