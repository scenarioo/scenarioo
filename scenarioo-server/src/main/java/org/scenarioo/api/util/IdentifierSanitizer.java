package org.scenarioo.api.util;

import org.scenarioo.api.rules.CharacterChecker;

/**
 * Use this to make sure your identifiers are compilant with the Scenarioo APIs restrictions that are enforced in the
 * {@link CharacterChecker}.
 */
public class IdentifierSanitizer {
	
	public static String sanitize(final String identifier) {
		if (identifier == null) {
			return null;
		}
		
		return identifier.replace("/", "_").replace("\\", "_");
	}
	
}
