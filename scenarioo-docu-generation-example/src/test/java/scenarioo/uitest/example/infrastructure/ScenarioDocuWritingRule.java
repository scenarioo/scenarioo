package scenarioo.uitest.example.infrastructure;

import static scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;
import ngusd.api.ScenarioDocuWriter;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.UseCase;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * A {@link TestRule} to setup as a {@link Rule} on your UI test classes to generate documentation content for each
 * running test method as a {@link Scenario} inside the Scenarioo Documentation.
 */
public class ScenarioDocuWritingRule extends TestWatcher {
	
	private static final Logger LOGGER = Logger.getLogger(UseCaseDocuWritingRule.class);
	
	private ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, EXAMPLE_BRANCH_NAME,
			EXAMPLE_BUILD_NAME);
	
	private UseCase useCase;
	
	private Scenario scenario;
	
	/**
	 * Get the usecase for current running test (as initialized by this rule)
	 */
	public UseCase getUseCase() {
		return useCase;
	}
	
	/**
	 * Get the scenario for current running test (as initialized by this rule)
	 */
	public Scenario getScenario() {
		return scenario;
	}
	
	/**
	 * Initialize current running usecase and scenario before the test gets executed.
	 */
	@Override
	protected void starting(final Description testMethodDescription) {
		useCase = UseCaseDocuWritingRule.createUseCase(testMethodDescription.getTestClass());
		scenario = createScenario(testMethodDescription);
	}
	
	private Scenario createScenario(final Description testMethodDescription) {
		Scenario scenario = new Scenario();
		String description = "";
		String name = createScenarioName(testMethodDescription);
		DocuDescription docuDescription = testMethodDescription.getAnnotation(DocuDescription.class);
		if (docuDescription != null) {
			description = docuDescription.description();
			scenario.addDetail("userRole", docuDescription.userRole());
		}
		scenario.setName(name);
		scenario.setDescription(description);
		return scenario;
	}
	
	private String createScenarioName(final Description testMethodDescription) {
		DocuDescription description = testMethodDescription.getAnnotation(DocuDescription.class);
		if (description != null && !StringUtils.isBlank(description.name())) {
			return description.name();
		}
		else {
			// simply use the test name as scenario name if not set through description annotation.
			return testMethodDescription.getMethodName();
		}
	}
	
	/**
	 * When test succeeded: Save the scenario with status 'success'
	 */
	@Override
	protected void succeeded(final Description description) {
		writeScenarioDescription(description, "success");
	}
	
	/**
	 * When test failed: Save the scenario with status 'failed'
	 */
	@Override
	protected void failed(final Throwable e, final Description description) {
		writeScenarioDescription(description, "failed");
	}
	
	private void writeScenarioDescription(final Description testMethodDescription, final String status) {
		
		// Write scenario
		LOGGER.info("Generating Scenarioo Docu for Scenario " + useCase.getName() + "." + scenario.getName() + " ("
				+ status + ") : " + scenario.getDescription());
		scenario.setStatus(status);
		docuWriter.saveScenario(useCase, scenario);
		
		// Wait until asynch writing has finished.
		docuWriter.flush();
	}
	
}
