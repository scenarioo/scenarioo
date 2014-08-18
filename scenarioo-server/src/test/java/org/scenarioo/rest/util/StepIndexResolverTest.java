package org.scenarioo.rest.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.TestData;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.request.BuildIdentifier;
import org.scenarioo.rest.request.ScenarioIdentifier;
import org.scenarioo.rest.request.StepIdentifier;
import org.scenarioo.rest.step.ResolveStepIndexResult;
import org.scenarioo.rest.step.StepIndexResolver;

public class StepIndexResolverTest {
	
	private final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final ScenarioIdentifier USECASE_IDENTIFIER = new ScenarioIdentifier(BUILD_IDENTIFIER, "scenario",
			"usecase");
	
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
	
	private ScenarioPageSteps scenarioPagesAndSteps;
	private StepIdentifier stepIdentifier;
	private ResolveStepIndexResult resolveStepIndexResult;
	
	@Before
	public void setupTest() {
		scenarioPagesAndSteps = TestData.SCENARIO;
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
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, TestData.PAGE_NAME_VALID_1, 1, 2);
	}
	
	private void givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, TestData.PAGE_NAME_VALID_1, 1, 3);
	}
	
	private void givenStepWherePageOccurrenceDoesNotExist() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, TestData.PAGE_NAME_VALID_1, 2, 3);
	}
	
	private void givenStepWithPageThatDoesNotExistInScenario() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, TestData.PAGE_NAME_NON_EXISTENT, 0, 0);
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
	
}
