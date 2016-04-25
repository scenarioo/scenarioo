package org.scenarioo.business.diffViewer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;

/**
 * @see ScreenshotComparator
 */
@RunWith(MockitoJUnitRunner.class)
public class ScreenshotComparatorGraphicsMagickTest {
	ScreenshotComparator screenshotComparator = new ScreenshotComparator("baseUseCaseName", "baseScenarioName",
			"baseStepLink");
	private static final String FILEPATH = "src/test/resources/org/scenarioo/business/diffViewer/";
	private static final File BASE_SCREENSHOT = new File(FILEPATH + "baseScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_SAME_SIZE = new File(FILEPATH + "comparisonScreenshot.png");
	private static final File COMPARISON_SCREENSHOT_LARGE = new File(FILEPATH + "comparisonScreenshotLarge.png");
	private static final File DIFF_SCREENSHOT = new File(FILEPATH + "diffScreenshot.png");
	private static final File NON_EXISTENT_SCREENSHOT = new File(FILEPATH + "nonExistentScreenshot.png");
	private static final double SCREENSHOT_DIFFERENCE_SAME_SIZE = 12;
	private static final double SCREENSHOT_DIFFERENCE_LARGE = 15;
	private static final double DOUBLE_TOLERANCE = 0.001;
	private static final boolean IS_GRAPHICS_MAGICK_INSTALLED = isGraphicsMagickInstalled();

	@Before
	public void deleteDiffScreenshot() {
		DIFF_SCREENSHOT.delete();
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
		final Logger LOGGER = Logger.getLogger(ScenarioDocuBuildsManager.class);
		final TestAppender appender = new TestAppender();
		LOGGER.addAppender(appender);

		double difference = screenshotComparator.compareScreenshots(BASE_SCREENSHOT,
				NON_EXISTENT_SCREENSHOT, DIFF_SCREENSHOT);

		final List<LoggingEvent> log = appender.getLog();
		final LoggingEvent firstLogEntry = log.get(0);

		assertEquals("Difference of screenshots", 0.0D, difference, 0.001);
		assertEquals("Log Level", Level.WARN, firstLogEntry.getLevel());
		assertTrue("Assert log message is correct",
				firstLogEntry.getMessage().toString().contains("Graphics Magick operation failed"));
		LOGGER.removeAppender(appender);
	}

	private static boolean isGraphicsMagickInstalled() {
		CompareCmd cmd = new CompareCmd(true);
		IMOperation compareOperation = new IMOperation();
		compareOperation.addRawArgs("-version");
		try {
			cmd.run(compareOperation);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	class TestAppender extends AppenderSkeleton {
		private final List<LoggingEvent> log = new ArrayList<LoggingEvent>();

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
		}

		public List<LoggingEvent> getLog() {
			return new ArrayList<LoggingEvent>(log);
		}
	}
}
