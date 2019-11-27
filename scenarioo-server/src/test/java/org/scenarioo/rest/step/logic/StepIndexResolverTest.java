package org.scenarioo.rest.step.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepIndexResolverTest {

	private final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final ScenarioIdentifier USECASE_IDENTIFIER = new ScenarioIdentifier(BUILD_IDENTIFIER, "scenario",
		"usecase");

	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();

	private ScenarioPageSteps scenarioPagesAndSteps;
	private StepIdentifier stepIdentifier;
	private ResolveStepIndexResult resolveStepIndexResult;

	@BeforeEach
	void setupTest() {
		scenarioPagesAndSteps = StepTestData.SCENARIO;
	}

	@Test
	void resolveIndexSuccessful_noFallback() {
		givenStepIdentifierOfAnExistingStep();

		whenResolvingTheStepIndex();

		expectRequestedStepIndexIsFound();
	}

	//step_Technical Corner Cases_dummy_scenario_with_one_step_with_an_encoded_space_in_url_url-with-encoded%2520space.jsp_0_0

	@Test
	void stepInPageOccurrenceNotFound_fallbackWithinSamePageOccurrence() {
		givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist();

		whenResolvingTheStepIndex();

		expectFallbackFindsClosestStepInPageOccurrence();
	}

	@Test
	void stepInPageOccurrenceNotFound_fallbackWithinSamePageOccurrence_withLabels() {
		givenStepIdentifierWhereStepInPageOccurrenceDoesNotExist_withLabels();

		whenResolvingTheStepIndex();

		expectFallbackFindsStepInPageOccurrenceWithMostMatchingLabels();
	}

	@Test
	void pageOccurrenceNotFound_fallbackWithinScenario() {
		givenStepWherePageOccurrenceDoesNotExist();

		whenResolvingTheStepIndex();

		expectFallbackFindsClosestPageOccurrence();
	}

	@Test
	void pageOccurrenceNotFound_fallbackWithinScenario_withLabels() {
		givenStepWherePageOccurrenceDoesNotExist_withLabels();

		whenResolvingTheStepIndex();

		expectFallbackFindsPageOccurrenceAndStepWithMostMatchingLabels();
	}

	@Test
	void pageNotFound_noFallbackPossible() {
		givenStepWithPageThatDoesNotExistInScenario();

		whenResolvingTheStepIndex();

		expectNoIndexAndNoRedirectIsFound();
	}

	@Test
	void stepIdentifierMustNotBeNull() {
		givenStepIdentifierIsNull();

		assertThrows(NullPointerException.class, this::whenResolvingTheStepIndex);
	}

	@Test
	void scenarioPagesAndStepsMustNotBeNull() {
		givenStepIdentifierOfAnExistingStep();
		givenScenarioPagesAndStepsIsNull();

		assertThrows(NullPointerException.class, this::whenResolvingTheStepIndex);
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
		assertEquals("screenshot-4.jpeg", resolveStepIndexResult.getScreenshotFileName());
	}

	private void expectFallbackFindsStepInPageOccurrenceWithMostMatchingLabels() {
		assertEquals(3, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertEquals("screenshot-3.jpeg", resolveStepIndexResult.getScreenshotFileName());
	}

	private void expectFallbackFindsClosestPageOccurrence() {
		assertEquals(2, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getPageOccurrence());
		assertEquals(0, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertEquals("screenshot-2.jpeg", resolveStepIndexResult.getScreenshotFileName());
	}

	private void expectFallbackFindsPageOccurrenceAndStepWithMostMatchingLabels() {
		assertEquals(3, resolveStepIndexResult.getIndex());
		assertTrue(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertEquals(1, resolveStepIndexResult.getRedirect().getPageOccurrence());
		assertEquals(1, resolveStepIndexResult.getRedirect().getStepInPageOccurrence());
		assertEquals("screenshot-3.jpeg", resolveStepIndexResult.getScreenshotFileName());
	}

	private void expectNoIndexAndNoRedirectIsFound() {
		assertEquals(-1, resolveStepIndexResult.getIndex());
		assertFalse(resolveStepIndexResult.isIndexValid());
		assertFalse(resolveStepIndexResult.isRequestedStepFound());
		assertNull(resolveStepIndexResult.getRedirect());
		assertNull(resolveStepIndexResult.getScreenshotFileName());
	}

}
