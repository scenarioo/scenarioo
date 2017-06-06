package org.scenarioo.rest.step.logic;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.rest.base.StepIdentifier;

public class ScenarioLoaderTest {

	private final AggregatedDocuDataReader aggregatedDataReader = new DummyAggregatedDataReader();
	private final ScenarioLoader scenarioLoader = new ScenarioLoader(aggregatedDataReader);

	private StepIdentifier stepIdentifier;
	private LoadScenarioResult loadScenarioResult;

	@Test
	public void whenScenarioIsFound_noFallbackHappens() {
		givenScenarioIdentifierOfExistingScenario();

		whenLoadingScenario();

		expectScenarioIsFound();
	}

	@Test
	public void whenScenarioIsNotFound_butUsecaseExists_andPageExists_thenRedirect() {
		givenScenarioIdentifierOfNonExistingScenarioButExistingFeature();

		whenLoadingScenario();

		expectFallbackToPageInDifferentScenarioInSameUsecase();
	}

	@Test
	public void whenScenarioIsNotFound_butUsecaseExists_andPageExists_andStepIdentifierHasLabels_thenRedirectUsingLabels() {
		givenScenarioIdentifierOfNonExistingScenarioButExistingFeatureAndWithLabels();

		whenLoadingScenario();

		expectFallbackToScenarioInSameFeatureWithMostMatchingLabels();
	}

	@Test
	public void whenScenarioAndFeatureAreNotFound_butPageExistsInDifferentUsecase_thenRedirect() {
		givenScenarioIdentifierOfNonExistingFeature();

		whenLoadingScenario();

		expectFallbackToPageInDifferentUsecase();
	}

	@Test
	public void whenScenarioAndFeatureAreNotFound_butPageExistsInDifferentUsecase_andStepIdentifierHasLabels_thenRedirectUsingLabels() {
		givenScenarioIdentifierOfNonExistingFeatureWithLabels();

		whenLoadingScenario();

		expectFallbackToPageInDifferentUsecaseWithMostMatchingLabels();
	}

	private void givenScenarioIdentifierOfExistingScenario() {
		stepIdentifier = StepTestData.STEP_IDENTIFIER_VALID;
	}

	private void givenScenarioIdentifierOfNonExistingScenarioButExistingFeature() {
		stepIdentifier = StepTestData.STEP_IDENTIFIER_INEXISTENT_SCENARIO;
	}

	private void givenScenarioIdentifierOfNonExistingScenarioButExistingFeatureAndWithLabels() {
		stepIdentifier = StepTestData.STEP_IDENTIFIER_INEXISTENT_SCENARIO_WITH_LABELS;
	}

	private void givenScenarioIdentifierOfNonExistingFeature() {
		stepIdentifier = StepTestData.STEP_IDENTIFIER_INEXISTENT_FEATURE;
	}

	private void givenScenarioIdentifierOfNonExistingFeatureWithLabels() {
		stepIdentifier = StepTestData.STEP_IDENTIFIER_INEXISTENT_FEATURE_WITH_LABELS;
	}

	private void whenLoadingScenario() {
		loadScenarioResult = scenarioLoader.loadScenario(stepIdentifier);
	}

	private void expectScenarioIsFound() {
		assertEquals(StepTestData.SCENARIO, loadScenarioResult.getPagesAndSteps());
		assertTrue(loadScenarioResult.isRequestedScenarioFound());
		assertFalse(loadScenarioResult.containsValidRedirect());
	}

	private void expectFallbackToPageInDifferentScenarioInSameUsecase() {
		expectFallback();
		expectBuildIdentifierDidNotChange();
		expectUsecaseDidNotChange();

		assertEquals(StepTestData.SCENARIO_NAME_VALID_2, loadScenarioResult.getRedirect().getScenarioName());

		expectRequestedPageName();
		assertEquals(stepIdentifier.getPageOccurrence(), loadScenarioResult.getRedirect().getPageOccurrence());
		assertEquals(stepIdentifier.getStepInPageOccurrence(), loadScenarioResult.getRedirect()
				.getStepInPageOccurrence());
	}

	private void expectFallbackToScenarioInSameFeatureWithMostMatchingLabels() {
		expectFallback();
		expectBuildIdentifierDidNotChange();
		expectUsecaseDidNotChange();
		assertEquals(StepTestData.SCENARIO_NAME_VALID_WITH_MATCHING_LABELS, loadScenarioResult.getRedirect()
				.getScenarioName());
		expectRequestedPageName();
		assertEquals(0, loadScenarioResult.getRedirect().getPageOccurrence());
		assertEquals(1, loadScenarioResult.getRedirect().getStepInPageOccurrence());
	}

	private void expectFallbackToPageInDifferentUsecase() {
		expectFallback();
		expectBuildIdentifierDidNotChange();
		assertEquals(StepTestData.FEATURE_NAME_VALID, loadScenarioResult.getRedirect().getFeatureName());
		assertEquals(StepTestData.SCENARIO_NAME_VALID_2, loadScenarioResult.getRedirect().getScenarioName());
		expectRequestedPageName();
		assertEquals(stepIdentifier.getPageOccurrence(), loadScenarioResult.getRedirect().getPageOccurrence());
		assertEquals(stepIdentifier.getStepInPageOccurrence(), loadScenarioResult.getRedirect()
				.getStepInPageOccurrence());
	}

	private void expectFallbackToPageInDifferentUsecaseWithMostMatchingLabels() {
		expectFallback();
		expectBuildIdentifierDidNotChange();
		assertEquals(StepTestData.FEATURE_NAME_VALID_WITH_MATCHING_LABELS, loadScenarioResult.getRedirect()
				.getFeatureName());
		assertEquals(StepTestData.SCENARIO_NAME_VALID_WITH_MATCHING_LABELS, loadScenarioResult.getRedirect()
				.getScenarioName());
		expectRequestedPageName();
		assertEquals(0, loadScenarioResult.getRedirect().getPageOccurrence());
		assertEquals(1, loadScenarioResult.getRedirect().getStepInPageOccurrence());
	}

	private void expectFallback() {
		assertNotNull(loadScenarioResult.getPagesAndSteps());
		assertFalse(loadScenarioResult.isRequestedScenarioFound());
		assertTrue(loadScenarioResult.containsValidRedirect());
	}

	private void expectBuildIdentifierDidNotChange() {
		assertEquals(stepIdentifier.getBuildIdentifier(), loadScenarioResult.getRedirect().getBuildIdentifier());
	}

	private void expectUsecaseDidNotChange() {
		assertEquals(stepIdentifier.getFeatureName(), loadScenarioResult.getRedirect().getFeatureName());
	}

	private void expectRequestedPageName() {
		assertEquals(stepIdentifier.getPageName(), loadScenarioResult.getRedirect().getPageName());
	}

}
