package org.scenarioo.rest.util;

import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.request.StepIdentifier;

public class StepIndexResolver {
	
	private static final Logger LOGGER = Logger.getLogger(StepIndexResolver.class);
	
	public ResolveStepIndexResult resolveStepIndex(final ScenarioPageSteps scenarioPagesAndSteps,
			final StepIdentifier stepIdentifier) {
		if (scenarioPagesAndSteps == null) {
			throw new RuntimeException("resolveStepIndex: scenarioPagesAndSteps is null");
		}
		
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
		
		return ResolveStepIndexResult.otherStepInPageOccurrenceFound(lastPageOccurrence.getIndexOfFirstStep(),
				redirectStepIdentifier);
	}
	
}
