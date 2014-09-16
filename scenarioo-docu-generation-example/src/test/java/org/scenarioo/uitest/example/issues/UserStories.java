package org.scenarioo.uitest.example.issues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Example application specific annotation, that we use in this example to annotate user stories (by work item IDs) on
 * the test scenarios, to document, which requirements (user stories and parent epic as well as feature) a test scenario
 * is testing and therefore partialy documenting.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface UserStories {
	
	/**
	 * IDs of the user stories to attach to a test scenario (the user stories this scenario tests)
	 */
	long[] value() default {};
	
}
