package org.scenarioo.rest.step.logic;

import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.rest.base.StepIdentifier;

/**
 * <p>
 * Contains all the information about the attempt to load a step. It can either be:
 * </p>
 * <ul>
 * <li>a success (step found)</li>
 * <li>a redirect (same page found in different step, scenario or usecase)</li>
 * <li>or a failure (page does not exist in this build)</li>
 * </ul>
 */
public class StepLoaderResult {
	
	private final int stepIndex;
	private final StepIdentifier stepIdentifier;
	private final boolean requestedStepFound;
	private final StepStatistics stepStatistics;
	private final String screenshotFileName;
	
	private StepLoaderResult(final int stepIndex, final StepIdentifier stepIdentifier,
			final boolean requestedStepFound, final StepStatistics stepStatistics, final String screenshotFileName) {
		this.stepIndex = stepIndex;
		this.stepIdentifier = stepIdentifier;
		this.requestedStepFound = requestedStepFound;
		this.stepStatistics = stepStatistics;
		this.screenshotFileName = screenshotFileName;
	}
	
	public static StepLoaderResult createFoundRequestedStep(final int stepIndex, final StepIdentifier stepIdentifier,
			final StepStatistics stepStatistics, final String screenshotFileName) {
		return new StepLoaderResult(stepIndex, stepIdentifier, true, stepStatistics, screenshotFileName);
	}
	
	public static StepLoaderResult createRedirect(final StepIdentifier redirect) {
		return new StepLoaderResult(-1, redirect, false, null, null);
	}
	
	public static StepLoaderResult createNotFound() {
		return new StepLoaderResult(-1, null, false, null, null);
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
	
	public StepStatistics getStepStatistics() {
		return stepStatistics;
	}
	
	public String getScreenshotFileName() {
		return screenshotFileName;
	}
	
	public boolean isRedirect() {
		return stepIdentifier != null && !requestedStepFound;
	}
	
	public boolean isNotFound() {
		return stepIdentifier == null;
	}
	
}
