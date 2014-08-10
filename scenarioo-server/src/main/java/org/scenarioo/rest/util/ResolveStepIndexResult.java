package org.scenarioo.rest.util;

import org.scenarioo.rest.request.StepIdentifier;

public class ResolveStepIndexResult {
	
	private final int index;
	private final boolean requestedStepFound;
	private final boolean indexValid;
	private final StepIdentifier redirect;
	
	private ResolveStepIndexResult(final int index, final boolean requestedStepFound, final boolean indexValid) {
		this.index = index;
		this.requestedStepFound = requestedStepFound;
		this.indexValid = indexValid;
		this.redirect = null;
	}
	
	private ResolveStepIndexResult(final int index, final boolean requestedStepFound, final boolean indexValid,
			final StepIdentifier redirect) {
		this.index = index;
		this.requestedStepFound = requestedStepFound;
		this.indexValid = indexValid;
		this.redirect = redirect;
	}
	
	public static ResolveStepIndexResult requestedIndexFound(final int index) {
		return new ResolveStepIndexResult(index, true, true);
	}
	
	public static ResolveStepIndexResult otherStepInPageOccurrenceFound(final int index, final StepIdentifier redirect) {
		return new ResolveStepIndexResult(index, false, true, redirect);
	}
	
	public static ResolveStepIndexResult noFallbackFound() {
		return new ResolveStepIndexResult(-1, false, false, null);
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
	
	public boolean containsValidRedirect() {
		return !requestedStepFound && indexValid && redirect != null;
	}
	
	public boolean didNotFindPage() {
		return !requestedStepFound && redirect == null;
	}
	
}
