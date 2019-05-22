package org.scenarioo.dao.search;

/**
 * Thrown if anything goes wrong with executing a search query.
 */
public class SearchFailedException extends RuntimeException {

	public SearchFailedException(final Throwable t) {
		super(getMessage(t));
	}

	private static String getMessage(final Throwable t) {
		if (t.getCause() != null) {
			return t.getCause().getMessage();
		} else {
			return t.getMessage();
		}
	}

}
