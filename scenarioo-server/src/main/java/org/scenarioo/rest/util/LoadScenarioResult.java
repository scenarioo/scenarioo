package org.scenarioo.rest.util;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.request.StepIdentifier;

public class LoadScenarioResult {
	
	private final ScenarioPageSteps scenarioPageSteps;
	private final boolean requestedScenarioFound;
	private final StepIdentifier redirect;
	
	private LoadScenarioResult(final ScenarioPageSteps scenarioPageSteps, final boolean requestedScenarioFound,
			final StepIdentifier redirect) {
		this.scenarioPageSteps = scenarioPageSteps;
		this.requestedScenarioFound = requestedScenarioFound;
		this.redirect = redirect;
	}
	
	public static LoadScenarioResult foundRequestedScenario(final ScenarioPageSteps loadScenarioPageSteps) {
		return new LoadScenarioResult(loadScenarioPageSteps, true, null);
	}
	
	public static LoadScenarioResult foundFallback(final ScenarioPageSteps loadScenarioPageSteps,
			final StepIdentifier redirect) {
		return new LoadScenarioResult(loadScenarioPageSteps, false, redirect);
	}
	
	public static LoadScenarioResult foundNothing() {
		return new LoadScenarioResult(null, false, null);
	}
	
	public ScenarioPageSteps getPagesAndSteps() {
		return scenarioPageSteps;
	}
	
	public boolean isRequestedScenarioFound() {
		return requestedScenarioFound;
	}
	
	public StepIdentifier getRedirect() {
		return redirect;
	}
	
	public boolean containsValidRedirect() {
		return !requestedScenarioFound && redirect != null;
	}
	
	public boolean didNotFindPage() {
		return !requestedScenarioFound && redirect == null;
	}
	
}
