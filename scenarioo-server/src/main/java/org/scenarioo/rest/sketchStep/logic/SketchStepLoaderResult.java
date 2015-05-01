package org.scenarioo.rest.sketchStep.logic;

import org.apache.commons.lang3.StringUtils;
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
public class SketchStepLoaderResult {
	
	private final int stepIndex;
	private final StepIdentifier stepIdentifier;
	private final boolean requestedStepFound;
	private final StepStatistics stepStatistics;
	private final String screenshotFileName;
	
	private SketchStepLoaderResult(final int stepIndex, final StepIdentifier stepIdentifier,
			final boolean requestedStepFound, final StepStatistics stepStatistics, final String screenshotFileName) {
		this.stepIndex = stepIndex;
		this.stepIdentifier = stepIdentifier;
		this.requestedStepFound = requestedStepFound;
		this.stepStatistics = stepStatistics;
		this.screenshotFileName = screenshotFileName;
	}
	
	public static SketchStepLoaderResult createFoundRequestedStep(final int stepIndex, final StepIdentifier stepIdentifier,
			final StepStatistics stepStatistics, final String screenshotFileName) {
		return new SketchStepLoaderResult(stepIndex, stepIdentifier, true, stepStatistics, screenshotFileName);
	}
	
	public static SketchStepLoaderResult createRedirect(final StepIdentifier redirect, final String screenshotFileName) {
		return new SketchStepLoaderResult(-1, redirect, false, null, screenshotFileName);
	}
	
	public static SketchStepLoaderResult createNotFound() {
		return new SketchStepLoaderResult(-1, null, false, null, null);
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
	
	public String getScreenshotFileNameExtension() {
		if (StringUtils.isBlank(screenshotFileName)) {
			return "unknown-filetype";
		} else {
			String[] fileNameParts = screenshotFileName.split("\\.");
			return fileNameParts[fileNameParts.length - 1];
		}
	}
	
	public boolean isRedirect() {
		return stepIdentifier != null && !requestedStepFound;
	}
	
	public boolean isNotFound() {
		return stepIdentifier == null;
	}
	
}
