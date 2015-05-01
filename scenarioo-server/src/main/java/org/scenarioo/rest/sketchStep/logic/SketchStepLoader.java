package org.scenarioo.rest.sketchStep.logic;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.rest.base.StepIdentifier;

/**
 * Checks whether the requested step exists and in case it doesn't whether a fallback is possible.
 */
public class SketchStepLoader {
	
	private final ProposalLoader scenarioLoader;
	private final SketchStepIndexResolver stepIndexResolver;
	
	public SketchStepLoader(final ProposalLoader scenarioLoader, final SketchStepIndexResolver stepIndexResolver) {
		this.scenarioLoader = scenarioLoader;
		this.stepIndexResolver = stepIndexResolver;
	}
	
	public SketchStepLoaderResult loadStep(final StepIdentifier stepIdentifier) {
		LoadProposalResult loadScenarioResult = scenarioLoader.loadScenario(stepIdentifier);
		return loadStep(stepIdentifier, loadScenarioResult);
	}
	
	private SketchStepLoaderResult loadStep(final StepIdentifier stepIdentifier, final LoadProposalResult loadScenarioResult) {
		if (loadScenarioResult.isRequestedScenarioFound()) {
			return resolveStepIndex(stepIdentifier, loadScenarioResult.getPagesAndSteps());
		} else if (loadScenarioResult.containsValidRedirect()) {
			return SketchStepLoaderResult.createRedirect(loadScenarioResult.getRedirect(),
					loadScenarioResult.getScreenshotFileName());
		} else {
			return SketchStepLoaderResult.createNotFound();
		}
	}
	
	private SketchStepLoaderResult resolveStepIndex(final StepIdentifier stepIdentifier, final ScenarioPageSteps pageSteps) {
		ResolveSketchStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(pageSteps, stepIdentifier);
		
		if (resolveStepIndexResult.isRequestedStepFound()) {
			StepStatistics stepStatistics = pageSteps.getStepStatistics(stepIdentifier.getPageName(),
					stepIdentifier.getPageOccurrence());
			return SketchStepLoaderResult.createFoundRequestedStep(resolveStepIndexResult.getIndex(), stepIdentifier,
					stepStatistics, resolveStepIndexResult.getScreenshotFileName());
		} else if (resolveStepIndexResult.containsValidRedirect()) {
			return SketchStepLoaderResult.createRedirect(resolveStepIndexResult.getRedirect(),
					resolveStepIndexResult.getScreenshotFileName());
		} else {
			return findPageInAllUseCases(stepIdentifier);
		}
	}
	
	private SketchStepLoaderResult findPageInAllUseCases(final StepIdentifier stepIdentifier) {
		LoadProposalResult loadScenarioResult = scenarioLoader
				.findPageInRequestedUseCaseOrInAllUseCases(stepIdentifier);
		
		if (loadScenarioResult.containsValidRedirect()) {
			return SketchStepLoaderResult.createRedirect(loadScenarioResult.getRedirect(),
					loadScenarioResult.getScreenshotFileName());
		} else {
			return SketchStepLoaderResult.createNotFound();
		}
	}
	
}
