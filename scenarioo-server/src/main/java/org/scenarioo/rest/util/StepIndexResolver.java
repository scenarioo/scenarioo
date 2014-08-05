package org.scenarioo.rest.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.request.StepIdentifier;

public class StepIndexResolver {
	
	private static final Logger LOGGER = Logger.getLogger(StepIndexResolver.class);
	
	public int resolveStepIndex(final ScenarioPageSteps scenarioPagesAndSteps,
			final StepIdentifier stepIdentifier) {
		if (scenarioPagesAndSteps == null) {
			throw new RuntimeException(
					"resolveStepIndex: scenarioPagesAndSteps is null");
		}
		
		int occurrence = 0;
		
		for (PageSteps pageWithSteps : scenarioPagesAndSteps.getPagesAndSteps()) {
			if (isCorrectPage(pageWithSteps, stepIdentifier.getPageName())) {
				if (occurrence == stepIdentifier.getPageOccurrence()) {
					return resolveStepInPageOccurrence(pageWithSteps,
							stepIdentifier);
				}
				occurrence++;
			}
		}
		
		LOGGER.warn("pageOccurrence " + stepIdentifier.getPageOccurrence()
				+ " does not exist in " + stepIdentifier.toString());
		
		throw new WebApplicationException(Status.NOT_FOUND);
	}
	
	private int resolveStepInPageOccurrence(final PageSteps pageWithSteps,
			final StepIdentifier stepIdentifier) {
		if (stepIdentifier.getStepInPageOccurrence() < pageWithSteps.getSteps()
				.size()) {
			return pageWithSteps.getSteps()
					.get(stepIdentifier.getStepInPageOccurrence()).getIndex();
		}
		
		LOGGER.warn("stepInPageOccurrence "
				+ stepIdentifier.getStepInPageOccurrence()
				+ " does not exist in " + stepIdentifier.toString());
		
		throw new WebApplicationException(Status.NOT_FOUND);
	}
	
	private boolean isCorrectPage(final PageSteps pageWithSteps,
			final String pageName) {
		return pageName.equals(pageWithSteps.getPage().getName());
	}
	
}
