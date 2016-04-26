package org.scenarioo.business.diffViewer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListOutputConsumer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.model.configuration.ComparisonAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.RepositoryLocator;

/**
 * @see ScreenshotComparator
 */
public class ScreenshotComparatorGraphicsMagickTest {
	ScreenshotComparator screenshotComparator = new ScreenshotComparator("baseUseCaseName", "baseScenarioName",
			"baseStepLink");
	private static final String FILEPATH = "src/test/resources/org/scenarioo/business/diffViewer/";
	private static final File BASE_SCREENSHOT = new File(FILEPATH + "baseScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_SAME_SIZE = new File(FILEPATH + "comparisonScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_LARGE = new File(FILEPATH + "comparisonScreenshotLarge.png");
	private static final File DIFF_SCREENSHOT = new File(FILEPATH + "diffScreenshot.png");
	private static final File NON_EXISTENT_SCREENSHOT = new File(FILEPATH + "nonExistentScreenshot.png");
	private static final double SCREENSHOT_DIFFERENCE_SAME_SIZE = 12.97;
	private static final double SCREENSHOT_DIFFERENCE_LARGE = 15.35;
	private static final double DOUBLE_TOLERANCE = 0.01;
	private static final boolean IS_GRAPHICS_MAGICK_INSTALLED = isGraphicsMagickInstalled();

	private static final File ROOT_DIRECTORY = new File("tmpDiffViewerUnitTestFiles");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String COMPARISON_BRANCH_NAME = "comparisonBranch";
	private static final String COMPARISON_BUILD_NAME = "comparisonBuild";
	private static final String COMPARISON_NAME = "comparisonName";

	@Before
	public void deleteDiffScreenshot() {
		DIFF_SCREENSHOT.delete();
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

	@AfterClass
	public static void deleteDiffScreenshotAfterClass() {
		DIFF_SCREENSHOT.delete();
	}

	@Test
	public void compareEqualScreenshots() {
		double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				BASE_SCREENSHOT, DIFF_SCREENSHOT);
		assertEquals("Difference of screenshots", 0, difference, DOUBLE_TOLERANCE);
		assertTrue("No DiffScreenshot is saved", !DIFF_SCREENSHOT.exists());
	}

	@Test
	public void compareDifferentScreenshots() {
		double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				COMPARISON_SCREENSHOT_SAME_SIZE, DIFF_SCREENSHOT);
		if (IS_GRAPHICS_MAGICK_INSTALLED) {
			assertEquals("Difference of screenshots", SCREENSHOT_DIFFERENCE_SAME_SIZE, difference, DOUBLE_TOLERANCE);
		} else {
			assertEquals("Difference of screenshots", 0, difference, DOUBLE_TOLERANCE);
		}

	}

	@Test
	public void compareDifferentSizedScreenshots() {
		double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				COMPARISON_SCREENSHOT_LARGE, DIFF_SCREENSHOT);
		if (IS_GRAPHICS_MAGICK_INSTALLED) {
			assertEquals("Difference of screenshots", SCREENSHOT_DIFFERENCE_LARGE, difference, DOUBLE_TOLERANCE);
		} else {
			assertEquals("Difference of screenshots", 0, difference, DOUBLE_TOLERANCE);
		}

	}

	@Test
	public void compareNonExistentScreenshots() {
		Logger LOGGER = screenshotComparator.getLogger();
		TestAppender appender = new TestAppender();
		LOGGER.addAppender(appender);


		double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				NON_EXISTENT_SCREENSHOT, DIFF_SCREENSHOT);

		List<LoggingEvent> log = appender.getLog();
		LoggingEvent firstLogEntry = log.get(0);

		assertEquals("Difference of screenshots", 0.0D, difference, DOUBLE_TOLERANCE);
		assertEquals("Log Level", Level.WARN, firstLogEntry.getLevel());
		assertTrue("Assert log message is correct",
				firstLogEntry.getMessage().toString().contains("Graphics Magick operation failed"));
		LOGGER.removeAppender(appender);
	}

	private static boolean isGraphicsMagickInstalled() {
		CompareCmd gmConsole = new CompareCmd(true);
		gmConsole.setOutputConsumer(new ArrayListOutputConsumer());
		IMOperation compareOperation = new IMOperation();
		compareOperation.addRawArgs("-version");
		try {
			gmConsole.run(compareOperation);
			return true;
		} catch (Exception e) {
			return false;
		}
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

	class TestAppender extends AppenderSkeleton {
		private List<LoggingEvent> log = new ArrayList<LoggingEvent>();

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(LoggingEvent loggingEvent) {
			log.add(loggingEvent);
		}

		@Override
		public void close() {
		}

		public List<LoggingEvent> getLog() {
			return new ArrayList<LoggingEvent>(log);
		}
	}
}
