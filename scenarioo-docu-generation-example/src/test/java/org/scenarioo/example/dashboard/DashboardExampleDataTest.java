package org.scenarioo.example.dashboard;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A Dummy test to cause the build to copy the dummy test data for dashboard to the demo data directory
 */
public class DashboardExampleDataTest {

	@Test
	public void copyDashboardTestDataToExampleDocuDataOutputDir() throws URISyntaxException, IOException {
		File targetDir = new File("build/scenarioDocuExample/dashboard");
		File sourceDir = getResourceFile("example/documentation/scenarioDocuExample/dashboard");
		FileUtils.copyDirectory(sourceDir, targetDir);
	}

	private static File getResourceFile(final String relativeResourcePath) throws URISyntaxException {
		URL url = DashboardExampleDataTest.class.getClassLoader().getResource(relativeResourcePath);
		return new File(url.toURI());
	}

}
