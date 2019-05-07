package org.scenarioo.rest.integrationtest;

import org.junit.BeforeClass;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.utils.TestResourceFile;

import java.io.File;

/**
 * {@link AbstractIntegrationTest} configures the Server to use a predefined configuration with one branch, two builds and
 * two comparisons, which can then be used by the Integration Tests to get sensible answers from the server.
 * Since Spring Boot reuses the Server for multiple Integration Tests, we should only do this configuration once, for
 * the first Integration Tests that is started.
 */
public abstract class AbstractIntegrationTest {

	private static boolean integrationTestsStarted = false;

	@BeforeClass
	public static void initEnvironment() {
		if(!integrationTestsStarted) {
			File testBranchFolder = TestResourceFile.getResourceFile("org/scenarioo/rest/testConfiguration");
			System.setProperty("org.scenarioo.data", testBranchFolder.getAbsolutePath());
			//If the Integration Tests are run together with all other unit tests, then ScenarioDocuBuildsManager
			// was already instantiated and it holds a reference to an invalid scenarioo data directory,
			// which will lead to errors during server startup and thus failed Tests.
			ScenarioDocuBuildsManager.resetInstance();
			integrationTestsStarted = true;
		}
	}
}
