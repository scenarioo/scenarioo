package org.scenarioo.rest.step.logic;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;

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
		scenarioPagesAndSteps = StepTestData.SCENARIO;
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
	public void stepInPageOccurrenceNotFound_fallbackWithinSamePageOccurrence_withLabels() {
		givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist_withLabels();
		
		whenResolvingTheStepIndex();
		
		expectFallbackFindsStepInPageOccurrenceWithMostMatchingLabels();
	}
	
	@Test
	public void pageOccurrenceNotFound_fallbackWithinScenario() {
		givenStepWherePageOccurrenceDoesNotExist();
		
		whenResolvingTheStepIndex();
		
		expectFallbackFindsClosestPageOccurrence();
	}
	
	@Test
	public void pageOccurrenceNotFound_fallbackWithinScenario_withLabels() {
		givenStepWherePageOccurrenceDoesNotExist_withLabels();
		
		whenResolvingTheStepIndex();
		
		expectFallbackFindsPageOccurrenceAndStepWithMostMatchingLabels();
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
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, StepTestData.PAGE_NAME_VALID_1, 1, 2);
	}
	
	private void givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, StepTestData.PAGE_NAME_VALID_1, 1, 3);
	}
	
	private void givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist_withLabels() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, StepTestData.PAGE_NAME_VALID_1, 1, 3,
				createLabels("step-label-3"));
	}
	
	private Set<String> createLabels(final String label) {
		Set<String> labels = new HashSet<String>();
		labels.add(label);
		return labels;
	}
	
	private void givenStepWherePageOccurrenceDoesNotExist() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, StepTestData.PAGE_NAME_VALID_1, 2, 3);
	}
	
	private void givenStepWherePageOccurrenceDoesNotExist_withLabels() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, StepTestData.PAGE_NAME_VALID_1, 2, 3,
				createLabels("step-label-3"));
	}
	
	private void givenStepWithPageThatDoesNotExistInScenario() {
		stepIdentifier = new StepIdentifier(USECASE_IDENTIFIER, StepTestData.PAGE_NAME_NON_EXISTENT, 0, 0);
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
		assertEquals("screenshot-4.jpeg", resolveStepIndexResult.getScreenshotFileName());
	}
	
	private void expectFallbackFindsClosestStepInPageOccurrence() {
		assertEquals(4, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(2, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertNull(resolveStepIndexResult.getScreenshotFileName());
	}
	
	private void expectFallbackFindsStepInPageOccurrenceWithMostMatchingLabels() {
		assertEquals(3, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertNull(resolveStepIndexResult.getScreenshotFileName());
	}
	
	private void expectFallbackFindsClosestPageOccurrence() {
		assertEquals(2, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getPageOccurrence());
		assertEquals(0, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertNull(resolveStepIndexResult.getScreenshotFileName());
	}
	
	private void expectFallbackFindsPageOccurrenceAndStepWithMostMatchingLabels() {
		assertEquals(3, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getPageOccurrence());
		assertEquals(1, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertNull(resolveStepIndexResult.getScreenshotFileName());
	}
	
	private void expectNoIndexAndNoRedirectIsFound() {
		assertEquals(-1, resolveStepIndexResult.getIndex());
		assertFalse(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertNull(resolveStepIndexResult.getRedirect());
		assertNull(resolveStepIndexResult.getScreenshotFileName());
	}
	
}
