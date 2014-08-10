package org.scenarioo.rest.util;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.request.StepIdentifier;

public class StepImageInfoLoader {
	
	private final ScenarioLoader scenarioLoader;
	private final StepIndexResolver stepIndexResolver;
	
	public StepImageInfoLoader(final ScenarioLoader scenarioLoader, final StepIndexResolver stepIndexResolver) {
		this.scenarioLoader = scenarioLoader;
		this.stepIndexResolver = stepIndexResolver;
	}
	
	public StepImageInfo loadStepImageInfo(final StepIdentifier stepIdentifier) {
		LoadScenarioResult loadScenarioResult = scenarioLoader.loadScenario(stepIdentifier);
		return getStepImageInfo(stepIdentifier, loadScenarioResult);
	}
	
	private StepImageInfo getStepImageInfo(final StepIdentifier stepIdentifier,
			final LoadScenarioResult loadScenarioResult) {
		if (loadScenarioResult.isRequestedScenarioFound()) {
			return resolveStepIndex(stepIdentifier, loadScenarioResult.getPagesAndSteps());
		} else if (loadScenarioResult.containsValidRedirect()) {
			return StepImageInfo.createRedirect(loadScenarioResult.getRedirect());
		} else {
			return StepImageInfo.createNotFound();
		}
	}
	
	private StepImageInfo resolveStepIndex(final StepIdentifier stepIdentifier, final ScenarioPageSteps pageSteps) {
		ResolveStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(pageSteps, stepIdentifier);
		
		if (resolveStepIndexResult.isRequestedStepFound()) {
			return StepImageInfo.createFoundRequestedStep(resolveStepIndexResult.getIndex(), stepIdentifier);
		} else if (resolveStepIndexResult.containsValidRedirect()) {
			return StepImageInfo.createRedirect(resolveStepIndexResult.getRedirect());
		} else {
			return findPageInAllUseCases(stepIdentifier);
		}
	}
	
	private StepImageInfo findPageInAllUseCases(final StepIdentifier stepIdentifier) {
		LoadScenarioResult loadScenarioResult = scenarioLoader
				.findPageInRequestedUseCaseAndInAllUseCases(stepIdentifier);
		
		if (loadScenarioResult.containsValidRedirect()) {
			return StepImageInfo.createRedirect(loadScenarioResult.getRedirect());
		} else {
			return StepImageInfo.createNotFound();
		}
	}
	
}
