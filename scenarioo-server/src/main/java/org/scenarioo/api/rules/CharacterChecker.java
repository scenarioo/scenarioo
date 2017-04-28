package org.scenarioo.api.rules;

import org.scenarioo.api.exception.IllegalCharacterException;

/**
 * Checks certain strings for validity.
 * 
 * @see {@link #checkIdentifier(String)}.
 * @see {@link #checkLabel(String)};
 */
public class CharacterChecker {
	
	/**
	 * Checks the identifier for invalid characters. Currently, only slashes and backslashes are disallowed.
	 * 
	 * @param identifier
	 *            E.g. a branch name or a page name.
	 * 
	 * @return The identifier in case it is valid. This makes it possible to use the method inline.
	 * 
	 * @throws IllegalCharacterException
	 *             If a / or \ is found in the identifier.
	 */
	public static String checkIdentifier(final String identifier) {
		if (identifier == null) {
			return null;
		}
		
		if (identifier.contains("/") || identifier.contains("\\")) {
			throw new IllegalCharacterException("Identifier " + identifier + " contains illegal characters.");
		}
		
		return identifier;
	}
	
	/**
	 * Checks a label for invalid characters.
	 * 
	 * @param label
	 *            A label string, valid or invalid. This makes it possible to use the method inline.
	 * 
	 * @return The label in case it is valid.
	 * @throws IllegalCharacterException
	 *             If the label string is <code>null</code> or if it does not match the RegEx
	 *             <code>^[ a-zA-Z0-9_-]+$</code>.
	 */
	public static String checkLabel(final String label) {
		if (label == null || !label.matches("^[ a-zA-Z0-9_-]+$")) {
			throw new IllegalCharacterException("Label " + label + " contains illegal characters.");
		}
		
		return label;
	}
	
}
