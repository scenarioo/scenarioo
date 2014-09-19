package org.scenarioo.rest.step.logic;

import org.scenarioo.rest.base.StepIdentifier;

public class ResolveStepIndexResult {
	
	private final int index;
	private final boolean requestedStepFound;
	private final boolean indexValid;
	private StepIdentifier redirect = null;
	private String screenshotFileName = null;
	
	private ResolveStepIndexResult(final int index, final boolean requestedStepFound, final boolean indexValid,
			final String screenshotFileName) {
		this(index, requestedStepFound, indexValid);
		this.screenshotFileName = screenshotFileName;
	}
	
	private ResolveStepIndexResult(final int index, final boolean requestedStepFound, final boolean indexValid,
			final StepIdentifier redirect, final String screenshotFileName) {
		this(index, requestedStepFound, indexValid);
		this.redirect = redirect;
		this.screenshotFileName = screenshotFileName;
	}
	
	private ResolveStepIndexResult(final int index, final boolean requestedStepFound, final boolean indexValid) {
		this.index = index;
		this.requestedStepFound = requestedStepFound;
		this.indexValid = indexValid;
	}
	
	public static ResolveStepIndexResult requestedIndexFound(final int index, final String screenshotFileName) {
		return new ResolveStepIndexResult(index, true, true, screenshotFileName);
	}
	
	public static ResolveStepIndexResult otherStepInPageOccurrenceFound(final int index, final StepIdentifier redirect,
			final String screenshotFileName) {
		return new ResolveStepIndexResult(index, false, true, redirect, screenshotFileName);
	}
	
	public static ResolveStepIndexResult noFallbackFound() {
		return new ResolveStepIndexResult(-1, false, false);
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isRequestedStepFound() {
		return requestedStepFound;
	}
	
	public boolean isIndexValid() {
		return indexValid;
	}
	
	/**
	 * Only use this if requestedStepFound == false.
	 */
	public StepIdentifier getRedirect() {
		return redirect;
	}
	
	public String getScreenshotFileName() {
		return screenshotFileName;
	}
	
	public boolean containsValidRedirect() {
		return !requestedStepFound && indexValid && redirect != null;
	}
	
	public boolean didNotFindPage() {
		return !requestedStepFound && redirect == null;
	}
	
}
