package org.scenarioo.rest.step.logic;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.rest.base.StepIdentifier;

/**
 * Checks whether the requested step exists and in case it doesn't whether a fallback is possible.
 */
public class StepLoader {

	private final ScenarioLoader scenarioLoader;
	private final StepIndexResolver stepIndexResolver;

	public StepLoader(final ScenarioLoader scenarioLoader, final StepIndexResolver stepIndexResolver) {
		this.scenarioLoader = scenarioLoader;
		this.stepIndexResolver = stepIndexResolver;
	}

	public StepLoaderResult loadStep(final StepIdentifier stepIdentifier) {
		LoadScenarioResult loadScenarioResult = scenarioLoader.loadScenario(stepIdentifier);
		return loadStep(stepIdentifier, loadScenarioResult);
	}

	private StepLoaderResult loadStep(final StepIdentifier stepIdentifier, final LoadScenarioResult loadScenarioResult) {
		if (loadScenarioResult.isRequestedScenarioFound()) {
			return resolveStepIndex(stepIdentifier, loadScenarioResult.getPagesAndSteps());
		} else if (loadScenarioResult.containsValidRedirect()) {
			return StepLoaderResult.createRedirect(loadScenarioResult.getRedirect(),
					loadScenarioResult.getScreenshotFileName());
		} else {
			return StepLoaderResult.createNotFound();
		}
	}

	private StepLoaderResult resolveStepIndex(final StepIdentifier stepIdentifier, final ScenarioPageSteps pageSteps) {
		ResolveStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(pageSteps, stepIdentifier);

		if (resolveStepIndexResult.isRequestedStepFound()) {
			StepStatistics stepStatistics = pageSteps.getStepStatistics(stepIdentifier.getPageName(),
					stepIdentifier.getPageOccurrence());
			return StepLoaderResult.createFoundRequestedStep(resolveStepIndexResult.getIndex(), stepIdentifier,
					stepStatistics, resolveStepIndexResult.getScreenshotFileName());
		} else if (resolveStepIndexResult.containsValidRedirect()) {
			return StepLoaderResult.createRedirect(resolveStepIndexResult.getRedirect(),
					resolveStepIndexResult.getScreenshotFileName());
		} else {
			return findPageInAllFeatures(stepIdentifier);
		}
	}

	private StepLoaderResult findPageInAllFeatures(final StepIdentifier stepIdentifier) {
		LoadScenarioResult loadScenarioResult = scenarioLoader
				.findPageInRequestedFeatureOrInAllFeatures(stepIdentifier);

		if (loadScenarioResult.containsValidRedirect()) {
			return StepLoaderResult.createRedirect(loadScenarioResult.getRedirect(),
					loadScenarioResult.getScreenshotFileName());
		} else {
			return StepLoaderResult.createNotFound();
		}
	}

}
