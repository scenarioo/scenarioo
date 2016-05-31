package org.scenarioo.business.diffViewer.comparator;

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
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.RepositoryLocator;

/**
 * @see ScreenshotComparator
 */
public class ScreenshotComparatorGraphicsMagickTest {
	ScreenshotComparator screenshotComparator = new ScreenshotComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			getComparisonConfiguration());
	private static final String FILEPATH = "src/test/resources/org/scenarioo/business/diffViewer/";
	private static final File BASE_SCREENSHOT = new File(FILEPATH + "baseScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_SAME_SIZE = new File(FILEPATH + "comparisonScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_LARGE = new File(FILEPATH + "comparisonScreenshotLarge.png");
	private static final File DIFF_SCREENSHOT = new File(FILEPATH + "diffScreenshot.png");
	private static final File NON_EXISTENT_SCREENSHOT = new File(FILEPATH + "nonExistentScreenshot.png");
	private static final double SCREENSHOT_DIFFERENCE_SAME_SIZE = 12.97;
	private static final double SCREENSHOT_DIFFERENCE_LARGE = 15.08;
	private static final double DOUBLE_TOLERANCE = 0.01;
	private static final boolean IS_GRAPHICS_MAGICK_INSTALLED = isGraphicsMagickInstalled();

	private static final File ROOT_DIRECTORY = new File("tmp");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String BASE_BUILD_NAME = "baseBuild";
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
		} catch (final IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@AfterClass
	public static void deleteDiffScreenshotAfterClass() {
		DIFF_SCREENSHOT.delete();
	}

	@Test
	public void compareEqualScreenshots() {
		final double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				BASE_SCREENSHOT, DIFF_SCREENSHOT);
		assertEquals("Difference of screenshots", 0, difference, DOUBLE_TOLERANCE);
		assertTrue("No DiffScreenshot is saved", !DIFF_SCREENSHOT.exists());
	}

	@Test
	public void compareDifferentScreenshots() {
		final double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				COMPARISON_SCREENSHOT_SAME_SIZE, DIFF_SCREENSHOT);
		if (IS_GRAPHICS_MAGICK_INSTALLED) {
			assertEquals("Difference of screenshots", SCREENSHOT_DIFFERENCE_SAME_SIZE, difference, DOUBLE_TOLERANCE);
		} else {
			assertEquals("Difference of screenshots", 0, difference, DOUBLE_TOLERANCE);
		}

	}

	@Test
	public void compareDifferentSizedScreenshots() {
		final double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				COMPARISON_SCREENSHOT_LARGE, DIFF_SCREENSHOT);
		if (IS_GRAPHICS_MAGICK_INSTALLED) {
			assertEquals("Difference of screenshots", SCREENSHOT_DIFFERENCE_LARGE, difference, DOUBLE_TOLERANCE);
		} else {
			assertEquals("Difference of screenshots", 0, difference, DOUBLE_TOLERANCE);
		}

	}

	@Test
	public void compareNonExistentScreenshots() {
		final Logger LOGGER = screenshotComparator.getLogger();
		final TestAppender appender = new TestAppender();
		LOGGER.addAppender(appender);

		final double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				NON_EXISTENT_SCREENSHOT, DIFF_SCREENSHOT);

		final List<LoggingEvent> log = appender.getLog();
		final LoggingEvent firstLogEntry = log.get(0);

		assertEquals("Difference of screenshots", 0.0D, difference, DOUBLE_TOLERANCE);
		assertEquals("Log Level", Level.WARN, firstLogEntry.getLevel());
		assertTrue("Assert log message is correct",
				firstLogEntry.getMessage().toString().contains("Graphics Magick operation failed"));
		LOGGER.removeAppender(appender);
	}

	private static boolean isGraphicsMagickInstalled() {
		final CompareCmd gmConsole = new CompareCmd(true);
		gmConsole.setOutputConsumer(new ArrayListOutputConsumer());
		final IMOperation compareOperation = new IMOperation();
		compareOperation.addRawArgs("-version");
		try {
			gmConsole.run(compareOperation);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	private static Configuration getTestConfiguration() {

		final ComparisonConfiguration comparisonConfiguration = getComparisonConfiguration();

		final List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<ComparisonConfiguration>();
		comparisonConfigurations.add(comparisonConfiguration);

		final Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonConfigurations(comparisonConfigurations);

		return configuration;
	}

	private static ComparisonConfiguration getComparisonConfiguration() {
		final ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonConfiguration.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonConfiguration.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonConfiguration.setName(COMPARISON_NAME);
		return comparisonConfiguration;
	}

	class TestAppender extends AppenderSkeleton {
		private List<LoggingEvent> log = new ArrayList<LoggingEvent>();

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(final LoggingEvent loggingEvent) {
			log.add(loggingEvent);
		}

		@Override
		public void close() {
			// Not test relevant
		}

		public List<LoggingEvent> getLog() {
			return new ArrayList<LoggingEvent>(log);
		}
	}
}
