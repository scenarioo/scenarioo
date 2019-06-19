package org.scenarioo.rest.integrationtest;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.utils.TestResourceFile;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.io.File;

import static org.scenarioo.rest.integrationtest.AbstractIntegrationTest.ScenariooDataPropertyInitializer;

/**
 * {@link AbstractIntegrationTest} configures the Server to use a predefined configuration with one branch, two builds and
 * two comparisons, which can then be used by the Integration Tests to get sensible answers from the server.
 * Since Spring Boot reuses the Server for multiple Integration Tests, we should only do this configuration once, for
 * the first Integration Tests that is started.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
@ContextConfiguration(initializers = ScenariooDataPropertyInitializer.class)
public abstract class AbstractIntegrationTest {

	private static boolean integrationTestsStarted = false;

	@BeforeClass
	public static void initEnvironment() {
		if(!integrationTestsStarted) {
			//If the Integration Tests are run together with all other unit tests, then ScenarioDocuBuildsManager
			// was already instantiated and it holds a reference to an invalid scenarioo data directory,
			// which will lead to errors during server startup and thus failed Tests.
			ScenarioDocuBuildsManager.resetInstance();
			integrationTestsStarted = true;
		}
	}

	protected HttpEntity<?> noRequestEntity() {
		return null;
	}

	public static class ScenariooDataPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			System.out.println("Overriding scenarioo.data property for integration test...");
			File testBranchFolder = TestResourceFile.getResourceFile("org/scenarioo/rest/testConfiguration");
			TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "scenarioo.data=" + testBranchFolder.getAbsolutePath());
		}
	}

}
