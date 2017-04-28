package org.scenarioo.api.exception;

/**
 * This is used when the API finds an illegal character in a restricted field. E.g. slashes / backslashes are not
 * allowed in ID fields.
 */
public class IllegalCharacterException extends RuntimeException {
	
	public IllegalCharacterException(final String message) {
		super(message);
	}
	
}
