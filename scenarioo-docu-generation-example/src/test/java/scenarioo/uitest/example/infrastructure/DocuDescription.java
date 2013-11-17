package scenarioo.uitest.example.infrastructure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Example annotation to annotate your junit UI tests with additional information to store inside the documentation.
 * 
 * See {@link UITest} test basis class on how to write such annotations on all your tests into the documentation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface DocuDescription {
	
	String name() default "";
	
	String description();
	
	String userRole() default "unauthenticated";
	
}
