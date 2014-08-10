package org.scenarioo.rest.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scenarioo.TestData;
import org.scenarioo.dao.aggregates.AggregatedDataReader;
import org.scenarioo.rest.request.StepIdentifier;

public class ScenarioLoaderTest {
	
	private final AggregatedDataReader aggregatedDataReader = new DummyAggregatedDataReader();
	private final ScenarioLoader scenarioLoader = new ScenarioLoader(aggregatedDataReader);
	
	private StepIdentifier stepIdentifier;
	private LoadScenarioResult loadScenarioResult;
	
	@Test
	public void scenarioIsFound() {
		givenScenarioIdentifierOfExistingScenario();
		
		whenLoadingScenario();
		
		expectScenarioIsFound();
	}
	
	@Test
	public void scenarioNotFound_usecaseExists_pageExists_redirect() {
		givenScenarioIdentifierOfNonExistingScenarioButExistingUseCase();
		
		whenLoadingScenario();
		
		expectFallbackToPageInDifferentScenarioInSameUsecase();
	}
	
	@Test
	public void scenarioNotFound_pageExistsInDifferentUsecase_redirect() {
		givenScenarioIdentifierOfNonExistingUseCase();
		
		whenLoadingScenario();
		
		expectFallbackToPageInDifferentUsecase();
	}
	
	private void givenScenarioIdentifierOfExistingScenario() {
		stepIdentifier = TestData.STEP_IDENTIFIER_VALID;
	}
	
	private void givenScenarioIdentifierOfNonExistingScenarioButExistingUseCase() {
		stepIdentifier = TestData.STEP_IDENTIFIER_INEXISTENT_SCENARIO;
	}
	
	private void givenScenarioIdentifierOfNonExistingUseCase() {
		stepIdentifier = TestData.STEP_IDENTIFIER_INEXISTENT_USECASE;
	}
	
	private void whenLoadingScenario() {
		loadScenarioResult = scenarioLoader.loadScenario(stepIdentifier);
	}
	
	private void expectScenarioIsFound() {
		assertEquals(TestData.SCENARIO, loadScenarioResult.getPagesAndSteps());
		assertTrue(loadScenarioResult.isRequestedScenarioFound());
		assertFalse(loadScenarioResult.containsValidRedirect());
	}
	
	private void expectFallbackToPageInDifferentScenarioInSameUsecase() {
		assertNull(loadScenarioResult.getPagesAndSteps());
		assertFalse(loadScenarioResult.isRequestedScenarioFound());
		assertTrue(loadScenarioResult.containsValidRedirect());
		
		assertEquals(stepIdentifier.getBuildIdentifier(), loadScenarioResult.getRedirect().getBuildIdentifier());
		assertEquals(stepIdentifier.getUsecaseName(), loadScenarioResult.getRedirect().getUsecaseName());
		assertEquals(TestData.SCENARIO_NAME_VALID_2, loadScenarioResult.getRedirect().getScenarioName());
		assertEquals(stepIdentifier.getPageName(), loadScenarioResult.getRedirect().getPageName());
		assertEquals(stepIdentifier.getPageOccurrence(), loadScenarioResult.getRedirect().getPageOccurrence());
		assertEquals(stepIdentifier.getStepInPageOccurrence(), loadScenarioResult.getRedirect()
				.getStepInPageOccurrence());
	}
	
	private void expectFallbackToPageInDifferentUsecase() {
		assertNull(loadScenarioResult.getPagesAndSteps());
		assertFalse(loadScenarioResult.isRequestedScenarioFound());
		assertTrue(loadScenarioResult.containsValidRedirect());
		
		assertEquals(stepIdentifier.getBuildIdentifier(), loadScenarioResult.getRedirect().getBuildIdentifier());
		assertEquals(TestData.USECASE_NAME_VALID, loadScenarioResult.getRedirect().getUsecaseName());
		assertEquals(TestData.SCENARIO_NAME_VALID_2, loadScenarioResult.getRedirect().getScenarioName());
		assertEquals(stepIdentifier.getPageName(), loadScenarioResult.getRedirect().getPageName());
		assertEquals(stepIdentifier.getPageOccurrence(), loadScenarioResult.getRedirect().getPageOccurrence());
		assertEquals(stepIdentifier.getStepInPageOccurrence(), loadScenarioResult.getRedirect()
				.getStepInPageOccurrence());
	}
	
}
