package org.scenarioo.rest.sketchStep.logic;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.rest.base.StepIdentifier;

public class LoadProposalResult {
	
	private final ScenarioPageSteps scenarioPageSteps;
	private final boolean requestedScenarioFound;
	private final StepIdentifier redirect;
	private final String screenshotFileName;
	
	private LoadProposalResult(final ScenarioPageSteps scenarioPageSteps, final boolean requestedScenarioFound,
			final StepIdentifier redirect, final String screenshotFileName) {
		this.scenarioPageSteps = scenarioPageSteps;
		this.requestedScenarioFound = requestedScenarioFound;
		this.redirect = redirect;
		this.screenshotFileName = screenshotFileName;
	}
	
	public static LoadProposalResult foundRequestedScenario(final ScenarioPageSteps loadScenarioPageSteps) {
		return new LoadProposalResult(loadScenarioPageSteps, true, null, null);
	}
	
	public static LoadProposalResult foundFallback(final ScenarioPageSteps pagesAndSteps, final StepIdentifier redirect) {
		String screenshotFileName = getScreenshotFileName(pagesAndSteps, redirect);
		return new LoadProposalResult(pagesAndSteps, false, redirect, screenshotFileName);
	}
	
	private static String getScreenshotFileName(final ScenarioPageSteps pagesAndSteps, final StepIdentifier redirect) {
		StepDescription stepDescription = pagesAndSteps.getStepDescription(redirect.getPageName(),
				redirect.getPageOccurrence(), redirect.getStepInPageOccurrence());
		return stepDescription.getScreenshotFileName();
	}
	
	public static LoadProposalResult foundNothing() {
		return new LoadProposalResult(null, false, null, null);
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
