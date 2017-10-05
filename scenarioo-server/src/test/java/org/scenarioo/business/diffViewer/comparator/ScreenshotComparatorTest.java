package org.scenarioo.business.diffViewer.comparator;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.scenarioo.dao.diffViewer.impl.DiffFiles;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.TestFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

public class ScreenshotComparatorTest {
	private ScreenshotComparator screenshotComparator;

	private static final String FILEPATH = "src/test/resources/org/scenarioo/business/diffViewer/";
	private static final File BASE_SCREENSHOT = new File(FILEPATH + "baseScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_SAME_SIZE = new File(FILEPATH + "comparisonScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_LARGE = new File(FILEPATH + "comparisonScreenshotLarge.png");
	private static final File DIFF_SCREENSHOT = new File(FILEPATH + "diffScreenshot.png");
	private static final File NON_EXISTENT_SCREENSHOT = new File(FILEPATH + "nonExistentScreenshot.png");
	private static final File BLUE_100 = new File(FILEPATH + "blue_100x100.png");
	private static final File RED_100 = new File(FILEPATH + "red_100x100.png");
	private static final File WHITE_100 = new File(FILEPATH + "white_100x100.png");
	private static final File BLACK_100 = new File(FILEPATH + "black_100x100.png");
	private static final File RED_200 = new File(FILEPATH + "red_200x100.png");
	private static final File BLUE_RED_200 = new File(FILEPATH + "red_blue_200x100.png");
	private static final double SCREENSHOT_DIFFERENCE_SAME_SIZE = 16.3;
	private static final double SCREENSHOT_DIFFERENCE_LARGE = 27.73;
	private static final double DOUBLE_TOLERANCE = 0.01;

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();

	@Before
	public void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(rootFolder.newFolder());
		File comparisonsFolder = new DiffFiles().getComparisonDirectory(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
		assertTrue(comparisonsFolder.mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
		screenshotComparator = new ScreenshotComparator(getComparatorParameters());
	}

	@After
	public void cleanUpTest() {
		if (DIFF_SCREENSHOT.exists()) {
			assertTrue("Unable to clean up the test data: " + DIFF_SCREENSHOT.getAbsolutePath(), DIFF_SCREENSHOT.delete());
		}
	}

	@Test
	public void compare_sameSizeAndColor_returnsZero_andDoesNotCreateDiffImage() {
		DIFF_SCREENSHOT.delete();
		assertDifferenceForScreenshots(RED_100, RED_100, 0);
		assertFalse("No DiffScreenshot expected.", DIFF_SCREENSHOT.exists());
	}

	@Test
	public void compare_differentScreenshots() {
		assertDifferenceForScreenshots(BASE_SCREENSHOT, COMPARISON_SCREENSHOT_SAME_SIZE, SCREENSHOT_DIFFERENCE_SAME_SIZE);
	}

	@Test
	public void compare_differentSizedScreenshots() {
		assertDifferenceForScreenshots(BASE_SCREENSHOT, COMPARISON_SCREENSHOT_LARGE, SCREENSHOT_DIFFERENCE_LARGE);
	}

	@Test
	public void compare_allRed_allBlue_returnsHighValue() {
		// 100% of pixels are different, but each pixel only changed around 80% of its color
		assertDifferenceForScreenshots(RED_100, BLUE_100, 80.6);
	}

	@Test
	public void compare_allWhite_allBlack_returnsHundred() {
		// all pixels changed 100%
		assertDifferenceForScreenshots(BLACK_100, WHITE_100, 100);
	}

	@Test
	public void compare_bothRed_oneIsDoubleTheSize_returnsLessThanFifty() {
		// 50% more pixels, but each does only count around 80% (depending on color)
		// This is somehow strange and we might have to improve that in future that added
		// pixels should always count as 100% changed pixels.
		// But not that important.
		assertDifferenceForScreenshots(RED_100, RED_200, 42.08);
	}

	@Test
	public void compare_allRed_redBlue_oneIsDoubleTheSize_returnsMoreThanFifty() {
		// 75% of pixels are different, but not every pixel has changed color by 100% --> 80% * 75% = 60%
		assertDifferenceForScreenshots(RED_100, BLUE_RED_200, 58.63);
	}

	private void assertDifferenceForScreenshots(File baseScreenshot, File comparisonScreenshot, double expectedDifference) {
		final double actualDifference
			= screenshotComparator.compareScreenshots(baseScreenshot, comparisonScreenshot, DIFF_SCREENSHOT);
		assertEquals("Difference of screenshots", expectedDifference, actualDifference, DOUBLE_TOLERANCE);
	}

	@Test
	public void compare_nonExistentScreenshots() {
		Logger LOGGER = ScreenshotComparator.getLogger();
		TestAppender appender = new TestAppender();
		LOGGER.addAppender(appender);

		final double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
			NON_EXISTENT_SCREENSHOT, DIFF_SCREENSHOT);

		final List<LoggingEvent> log = appender.getLog();
		final LoggingEvent firstLogEntry = log.get(0);

		assertEquals("Difference of screenshots", 0.0D, difference, DOUBLE_TOLERANCE);
		assertEquals("Log Level", Level.WARN, firstLogEntry.getLevel());
		assertTrue("Assert log message is correct",
			firstLogEntry.getMessage().toString().contains("Failed to compare images"));
		LOGGER.removeAppender(appender);
	}

	private class TestAppender extends AppenderSkeleton {
		private final List<LoggingEvent> log = new ArrayList<>();

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

		List<LoggingEvent> getLog() {
			return new ArrayList<>(log);
		}
	}
}
