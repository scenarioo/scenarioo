package org.scenarioo.uitest.example.infrastructure;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.uitest.dummy.toolkit.UITestToolkit;


/**
 * Example basis class for unit tests that contains UI test scenarios to record these scenarios using Scenarioo.
 */
public class UITest {
	
	/**
	 * Rule to write the use case information for each test class
	 */
	@ClassRule
	public static UseCaseDocuWritingRule useCaseWritingRule = new UseCaseDocuWritingRule();
	
	/**
	 * Rule to write the scenario information for each test
	 */
	@Rule
	public ScenarioDocuWritingRule scenarioWritingRule = new ScenarioDocuWritingRule();
	
	/**
	 * This is the application dependent UI abstraction layer, to use for writing your UI tests against.
	 * 
	 * 
	 * It contains the functionality to write step descriptions and screenshots whenever the test does a click event.
	 */
	public UITestToolkitAbstraction toolkit;
	
	@Before
	public void setUp() {
		// The UITestToolkit is just a dummy toolkit implementation in this example you would replace this by your used
		// testing API (e.g. Selenium's WebDriver).
		toolkit = new UITestToolkitAbstraction(new UITestToolkit(), this);
	}
	
	@After
	public void tearDown() {
		// Wait until asynch writing of documentation data for current test scenario has finished.
		toolkit.getDocuWriter().flush();
	}
	
	/**
	 * Get use case for which this test generates documentation.
	 */
	public UseCase getUseCase() {
		return scenarioWritingRule.getUseCase();
	}
	
	/**
	 * Get the scenario for which this test generates documentation.
	 */
	public Scenario getScenario() {
		return scenarioWritingRule.getScenario();
	}
	
}
