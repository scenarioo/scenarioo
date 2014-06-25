package org.scenarioo.rest.exceptions;

import org.scenarioo.model.StepIdentifier;

public class PageOccurrenceDoesNotExistException extends RuntimeException {

	public PageOccurrenceDoesNotExistException(
			final StepIdentifier stepIdentifier) {
		super("pageOccurrence " + stepIdentifier.getPageOccurrence()
				+ " does not exist in " + stepIdentifier.toString());
	}

}
