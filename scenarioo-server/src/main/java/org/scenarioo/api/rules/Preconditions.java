package org.scenarioo.api.rules;

/**
 * Helper class to check some preconditions.
 * 
 * TODO [#336] should be replaced by google's common base class Preconditions. I thought it might be to risky to
 * introduce a new dependency now, so shortly before a release.
 */
public class Preconditions {
	
	public static void checkNotNull(final Object object, final String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
	}
	
}
