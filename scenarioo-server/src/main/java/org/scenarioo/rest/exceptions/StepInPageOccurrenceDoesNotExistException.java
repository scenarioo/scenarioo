package org.scenarioo.rest.exceptions;

import org.scenarioo.model.StepIdentifier;

public class StepInPageOccurrenceDoesNotExistException extends RuntimeException {

	public StepInPageOccurrenceDoesNotExistException(
			final StepIdentifier stepIdentifier) {
		super("stepInPageOccurrence "
				+ stepIdentifier.getStepInPageOccurrence()
				+ " does not exist in " + stepIdentifier.toString());
	}
}
