package org.scenarioo.rest.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
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
	private final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final ScenarioIdentifier USECASE_IDENTIFIER = new ScenarioIdentifier(BUILD_IDENTIFIER, "scenario",
			"usecase");
	private final ScenarioPageSteps SCENARIO_WITH_PAGES_AND_STEPS = createScenarioPagesAndSteps();
	
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
	
	private ScenarioPageSteps scenarioPagesAndSteps;
	private StepIdentifier stepIdentifier;
	private ResolveStepIndexResult resolveStepIndexResult;
	
	@Before
	public void setupTest() {
		scenarioPagesAndSteps = SCENARIO_WITH_PAGES_AND_STEPS;
	}
	
	@Test
	public void resolveIndexSuccessful_noFallback() {
		givenStepIdentifierOfAnExistingStep();
		
		whenResolvingTheStepIndex();
		
		expectRequestedStepIndexIsFound();
	}
	
	@Test
	public void stepInPageOccurrenceNotFound_fallbackWithinSamePageOccurrence() {
		givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist();
		
		whenResolvingTheStepIndex();
		
		expectFallbackFindsClosestStepInPageOccurrence();
	}
	
	@Test
	public void pageOccurrenceNotFound_fallbackWithinScenario() {
		givenStepWherePageOccurrenceDoesNotExist();
		
		whenResolvingTheStepIndex();
		
		expectFallbackFindsClosestPageOccurrence();
	}
	
	@Test
	public void pageNotFound_noFallbackPossible() {
		givenStepWithPageThatDoesNotExistInScenario();
		
		whenResolvingTheStepIndex();
		
		expectNoIndexAndNoRedirectIsFound();
	}
	
	@Test(expected = NullPointerException.class)
	public void stepIdentifierMustNotBeNull() {
		givenStepIdentifierIsNull();
		
		whenResolvingTheStepIndex();
	}
	
	@Test(expected = NullPointerException.class)
	public void scenarioPagesAndStepsMustNotBeNull() {
		givenStepIdentifierOfAnExistingStep();
		givenScenarioPagesAndStepsIsNull();
		
		whenResolvingTheStepIndex();
	}
	
	private void givenStepIdentifierOfAnExistingStep() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, PAGE_NAME_1, 1, 2);
	}
	
	private void givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, PAGE_NAME_1, 1, 3);
	}
	
	private void givenStepWherePageOccurrenceDoesNotExist() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, PAGE_NAME_1, 2, 3);
	}
	
	private void givenStepWithPageThatDoesNotExistInScenario() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, PAGE_NAME_NON_EXISTENT, 0, 0);
	}
	
	private void givenStepIdentifierIsNull() {
		stepIdentifier = null;
	}
	
	private void givenScenarioPagesAndStepsIsNull() {
		scenarioPagesAndSteps = null;
	}
	
	private void whenResolvingTheStepIndex() {
		resolveStepIndexResult = stepIndexResolver.resolveStepIndex(scenarioPagesAndSteps, stepIdentifier);
	}
	
	private void expectRequestedStepIndexIsFound() {
		assertEquals(4, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertTrue(resolveStepIndexResult.isRequestedStepFound());
	}
	
	private void expectFallbackFindsClosestStepInPageOccurrence() {
		assertEquals(4, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(2, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
	}
	
	private void expectFallbackFindsClosestPageOccurrence() {
		assertEquals(2, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getPageOccurrence());
		assertEquals(0, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
	}
	
	private void expectNoIndexAndNoRedirectIsFound() {
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
