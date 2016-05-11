package org.scenarioo.business.diffViewer.comparator;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListOutputConsumer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.model.configuration.ComparisonAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.RepositoryLocator;

/**
 * @see ScreenshotComparator
 */
@RunWith(MockitoJUnitRunner.class)
public class ScreenshotComparatorMockitoTest {

	@Mock
	private CompareCmd gmConsole;

	@Mock
	private ArrayListOutputConsumer gmConsoleOutputConsumer;

	@InjectMocks
	private final ScreenshotComparator screenshotComparator = new ScreenshotComparator("baseUseCaseName",
			"baseScenarioName",
			"baseStepLink");

	private static final double SCREENSHOT_DIFFERENCE = 12.97;
	private static final double DOUBLE_TOLERANCE = 0.01;
	private static final ArrayList<String> OUTPUT_CONSUMER_MOCK;
	private static final File ROOT_DIRECTORY = new File("tmp");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String COMPARISON_BRANCH_NAME = "comparisonBranch";
	private static final String COMPARISON_BUILD_NAME = "comparisonBuild";
	private static final String COMPARISON_NAME = "comparisonName";

	static {
		OUTPUT_CONSUMER_MOCK = new ArrayList<String>() {
			{
				add("Image Difference (RootMeanSquaredError):");
				add("            Normalized    Absolute");
				add("           ============  ==========");
				add("      Red: 0.0646782165       16.5");
				add("    Green: 0.1787812120       45.6");
				add("     Blue: 0.1765823425       45.0");
				add("  Opacity: 0.0000000000        0.0");
				add("    Total: 0.1297375400       33.1");
			}
		};
	}

	@BeforeClass
	public static void setUpClass() {
		ROOT_DIRECTORY.mkdirs();
		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(ROOT_DIRECTORY);
		RepositoryLocator.INSTANCE.getConfigurationRepository().getDiffViewerDirectory().mkdirs();
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@AfterClass
	public static void tearDownClass() {
		try {
			FileUtils.deleteDirectory(ROOT_DIRECTORY);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@Test
	public void testParseGmConsoleOutput() {
		File mockScreenshot = new File("mockScreenshot.png");

		try {
			doNothing().when(gmConsole).run(any(IMOperation.class));
		} catch (Exception e) {
			e.getStackTrace();
		}

		when(gmConsoleOutputConsumer.getOutput()).thenReturn(OUTPUT_CONSUMER_MOCK);
		double difference = screenshotComparator.compareScreenshots(mockScreenshot, mockScreenshot, mockScreenshot);
		assertEquals("Difference of screenshots", SCREENSHOT_DIFFERENCE, difference, DOUBLE_TOLERANCE);
	}

	private static Configuration getTestConfiguration() {

		ComparisonAlias comparisonAlias = new ComparisonAlias();
		comparisonAlias.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonAlias.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonAlias.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonAlias.setComparisonName(COMPARISON_NAME);

		List<ComparisonAlias> comparisonAliases = new LinkedList<ComparisonAlias>();
		comparisonAliases.add(comparisonAlias);

		Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonAliases(comparisonAliases);

		return configuration;
	}
}
