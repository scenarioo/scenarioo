package org.scenarioo.rest.step.logic;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.rest.base.StepIdentifier;

public class LoadScenarioResult {
	
	private final ScenarioPageSteps scenarioPageSteps;
	private final boolean requestedScenarioFound;
	private final StepIdentifier redirect;
	private final String screenshotFileName;
	
	private LoadScenarioResult(final ScenarioPageSteps scenarioPageSteps, final boolean requestedScenarioFound,
			final StepIdentifier redirect, final String screenshotFileName) {
		this.scenarioPageSteps = scenarioPageSteps;
		this.requestedScenarioFound = requestedScenarioFound;
		this.redirect = redirect;
		this.screenshotFileName = screenshotFileName;
	}
	
	public static LoadScenarioResult foundRequestedScenario(final ScenarioPageSteps loadScenarioPageSteps) {
		return new LoadScenarioResult(loadScenarioPageSteps, true, null, null);
	}
	
	public static LoadScenarioResult foundFallback(final ScenarioPageSteps pagesAndSteps, final StepIdentifier redirect) {
		String screenshotFileName = getScreenshotFileName(pagesAndSteps, redirect);
		return new LoadScenarioResult(pagesAndSteps, false, redirect, screenshotFileName);
	}
	
	private static String getScreenshotFileName(final ScenarioPageSteps pagesAndSteps, final StepIdentifier redirect) {
		StepDescription stepDescription = pagesAndSteps.getStepDescription(redirect.getPageName(),
				redirect.getPageOccurrence(), redirect.getStepInPageOccurrence());
		return stepDescription.getScreenshotFileName();
	}
	
	public static LoadScenarioResult foundNothing() {
		return new LoadScenarioResult(null, false, null, null);
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
	
	public String getScreenshotFileName() {
		return screenshotFileName;
	}
	
}
