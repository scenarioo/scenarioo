package org.scenarioo.rest.util;

import org.scenarioo.rest.request.StepIdentifier;

public class StepImageInfo {
	
	private final int stepIndex;
	private final StepIdentifier stepIdentifier;
	private final boolean requestedStepFound;
	
	private StepImageInfo(final int stepIndex, final StepIdentifier stepIdentifier,
			final boolean requestedStepFound) {
		this.stepIndex = stepIndex;
		this.stepIdentifier = stepIdentifier;
		this.requestedStepFound = requestedStepFound;
	}
	
	public static StepImageInfo createRedirect(final StepIdentifier redirect) {
		return new StepImageInfo(-1, redirect, false);
	}
	
	public static StepImageInfo createNotFound() {
		return new StepImageInfo(-1, null, false);
	}
	
	public int getStepIndex() {
		return stepIndex;
	}
	
	/**
	 * This is the redirect step identifier in case the requested step was not found.
	 */
	public StepIdentifier getStepIdentifier() {
		return stepIdentifier;
	}
	
	public boolean isRequestedStepFound() {
		return requestedStepFound;
	}
	
	public boolean isRedirect() {
		return stepIdentifier != null && !requestedStepFound;
	}
	
	public boolean isNotFound() {
		return stepIdentifier == null;
	}
	
	public static StepImageInfo createFoundRequestedStep(final int stepIndex, final StepIdentifier stepIdentifier) {
		return new StepImageInfo(stepIndex, stepIdentifier, true);
	}
	
}
