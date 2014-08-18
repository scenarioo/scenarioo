package org.scenarioo.rest.step;

import static com.google.common.base.Preconditions.*;

import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.request.StepIdentifier;

public class StepIndexResolver {
	
	private static final Logger LOGGER = Logger.getLogger(StepIndexResolver.class);
	
	/**
	 * Retrieves the overall index of a step in the scenario given a step identifier. Can do a fallback in case the
	 * requested step is not found in the scenario.
	 */
	public ResolveStepIndexResult resolveStepIndex(final ScenarioPageSteps scenarioPagesAndSteps,
			final StepIdentifier stepIdentifier) {
		checkNotNull(scenarioPagesAndSteps);
		checkNotNull(stepIdentifier);
		
		int occurrence = 0;
		PageSteps lastPageOccurrence = null;
		
		for (PageSteps pageWithSteps : scenarioPagesAndSteps.getPagesAndSteps()) {
			if (isCorrectPage(pageWithSteps, stepIdentifier.getPageName())) {
				if (occurrence == stepIdentifier.getPageOccurrence()) {
					return resolveStepInPageOccurrence(pageWithSteps, stepIdentifier);
				}
				lastPageOccurrence = pageWithSteps;
				occurrence++;
			}
		}
		
		// Because the loop counted one too many
		int redirectPageOccurrence = occurrence - 1;
		
		if (redirectPageOccurrence < 0) {
			return ResolveStepIndexResult.noFallbackFound();
		}
		
		return redirectToHighestExistingPageOccurrence(stepIdentifier, lastPageOccurrence, redirectPageOccurrence);
	}
	
	private ResolveStepIndexResult resolveStepInPageOccurrence(final PageSteps pageWithSteps,
			final StepIdentifier stepIdentifier) {
		if (stepIdentifier.getStepInPageOccurrence() < pageWithSteps.getSteps().size()) {
			int index = pageWithSteps.getSteps().get(stepIdentifier.getStepInPageOccurrence()).getIndex();
			return ResolveStepIndexResult.requestedIndexFound(index);
		} else {
			// Return the highest stepInPageOccurrence possible (the requested one was even higher)
			int index = pageWithSteps.getLastStep().getIndex();
			int redirectStepInPageOccurrence = pageWithSteps.getNumberOfSteps() - 1;
			StepIdentifier redirectStepIdentifier = StepIdentifier.withDifferentStepInPageOccurrence(stepIdentifier,
					redirectStepInPageOccurrence);
			
			LOGGER.warn("stepInPageOccurrence " + stepIdentifier.getStepInPageOccurrence() + " does not exist in "
					+ stepIdentifier + ". Redirecting to " + redirectStepIdentifier);
			
			// TODO [fallback with labels] Use the step with the most matching step-labels (all other labels can be
			// ignored)
			return ResolveStepIndexResult.otherStepInPageOccurrenceFound(index, redirectStepIdentifier);
		}
	}
	
	private boolean isCorrectPage(final PageSteps pageWithSteps, final String pageName) {
		return pageName.equals(pageWithSteps.getPage().getName());
	}
	
	private ResolveStepIndexResult redirectToHighestExistingPageOccurrence(final StepIdentifier stepIdentifier,
			final PageSteps lastPageOccurrence, final int redirectPageOccurrence) {
		// Here we don't use the highest possible value, because the stepInPageOccurrence
		// is not related to the original pageOccurrence
		int redirectStepInPageOccurrence = 0;
		
		StepIdentifier redirectStepIdentifier = StepIdentifier.withDifferentIds(stepIdentifier, redirectPageOccurrence,
				redirectStepInPageOccurrence);
		
		LOGGER.warn("pageOccurrence " + stepIdentifier.getPageOccurrence() + " does not exist in "
				+ stepIdentifier.toString() + ". Redirecting to " + redirectStepIdentifier);
		
		// TODO [fallback with labels] Use the step with the most matching step-labels (all other label levels can be
		// ignored)
		return ResolveStepIndexResult.otherStepInPageOccurrenceFound(lastPageOccurrence.getIndexOfFirstStep(),
				redirectStepIdentifier);
	}
	
}
