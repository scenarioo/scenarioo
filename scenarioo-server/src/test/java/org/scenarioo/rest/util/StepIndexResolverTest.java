package org.scenarioo.rest.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.rest.request.BuildIdentifier;
import org.scenarioo.rest.request.ScenarioIdentifier;
import org.scenarioo.rest.request.StepIdentifier;

public class StepIndexResolverTest {
	
	private static final String PAGE_NAME_1 = "pageName1";
	private static final String PAGE_NAME_2 = "pageName2";
	private static final String PAGE_NAME_NON_EXISTENT = "pageName3";
	
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
	private final ScenarioPageSteps scenarioPagesAndSteps = createScenarioPagesAndSteps();
	private final BuildIdentifier buildIdentifier = new BuildIdentifier("branch", "build");
	private final ScenarioIdentifier usecaseIdentifier = new ScenarioIdentifier(buildIdentifier, "scenario", "usecase");
	
	@Test
	public void resolveSuccessful() {
		StepIdentifier stepIdentifier = new StepIdentifier(usecaseIdentifier, PAGE_NAME_1, 1, 2);
		
		ResolveStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(scenarioPagesAndSteps,
				stepIdentifier);
		
		assertEquals(4, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertTrue(resolveStepIndexResult.isRequestedStepFound());
	}
	
	@Test
	public void resolvePageInStepOccurrenceNotFound_fallbackInSamePageOccurrencePossible() {
		StepIdentifier stepIdentifier = new StepIdentifier(usecaseIdentifier, PAGE_NAME_1, 1, 3);
		
		ResolveStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(scenarioPagesAndSteps,
				stepIdentifier);
		
		assertEquals(4, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(2, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
	}
	
	@Test
	public void resolvePageOccurrenceNotFound_fallbackWithinScenarioPossible() {
		StepIdentifier stepIdentifier = new StepIdentifier(usecaseIdentifier, PAGE_NAME_1, 2, 3);
		
		ResolveStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(scenarioPagesAndSteps,
				stepIdentifier);
		
		assertEquals(2, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getPageOccurrence());
		assertEquals(0, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
	}
	
	@Test
	public void resolvePageOccurrenceNotFound_pageDoesNotExistInScenario() {
		StepIdentifier stepIdentifier = new StepIdentifier(usecaseIdentifier, PAGE_NAME_NON_EXISTENT, 0, 0);
		
		ResolveStepIndexResult resolveStepIndexResult = stepIndexResolver.resolveStepIndex(scenarioPagesAndSteps,
				stepIdentifier);
		
		assertEquals(-1, resolveStepIndexResult.getIndex());
		assertFalse(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertNull(resolveStepIndexResult.getRedirect());
	}
	
	private ScenarioPageSteps createScenarioPagesAndSteps() {
		ScenarioPageSteps scenarioPageSteps = new ScenarioPageSteps();
		scenarioPageSteps.setPagesAndSteps(createPageSteps());
		return scenarioPageSteps;
	}
	
	private List<PageSteps> createPageSteps() {
		List<PageSteps> pageSteps = new LinkedList<PageSteps>();
		pageSteps.add(createPageSteps(PAGE_NAME_1, 1, 0));
		pageSteps.add(createPageSteps(PAGE_NAME_2, 1, 1));
		pageSteps.add(createPageSteps(PAGE_NAME_1, 3, 2));
		return pageSteps;
	}
	
	private PageSteps createPageSteps(final String pageName, final int stepCount, final int startIndex) {
		PageSteps pageSteps = new PageSteps();
		pageSteps.setPage(createPage(pageName));
		pageSteps.setSteps(createSteps(stepCount, startIndex));
		return pageSteps;
	}
	
	private Page createPage(final String pageName) {
		Page page = new Page();
		page.setName(pageName);
		return page;
	}
	
	private List<StepDescription> createSteps(final int stepCount, final int startIndex) {
		List<StepDescription> stepDescriptions = new ArrayList<StepDescription>(stepCount);
		for (int index = startIndex; index < startIndex + stepCount; index++) {
			stepDescriptions.add(createStepDescription(index));
		}
		return stepDescriptions;
	}
	
	private StepDescription createStepDescription(final int index) {
		StepDescription stepDescription = new StepDescription();
		stepDescription.setIndex(index);
		return stepDescription;
	}
	
}
