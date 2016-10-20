package org.scenarioo.business.diffViewer.comparator;

import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListOutputConsumer;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.dao.diffViewer.impl.DiffFiles;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.TestFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

@RunWith(MockitoJUnitRunner.class)
public class ScreenshotComparatorMockitoTest {

	@Mock
	private CompareCmd gmConsole;

	@Mock
	private ArrayListOutputConsumer gmConsoleOutputConsumer;

	@InjectMocks
	private final ScreenshotComparator screenshotComparator = new ScreenshotComparator(BASE_BRANCH_NAME,
		BASE_BUILD_NAME,
		getComparisonConfiguration());

	private static final double SCREENSHOT_DIFFERENCE = 14.11;
	private static final double DOUBLE_TOLERANCE = 0.01;
	private static final ArrayList<String> OUTPUT_CONSUMER_MOCK;

	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	static {
		OUTPUT_CONSUMER_MOCK = new ArrayList<String>() {
			{
				add("Image Difference (MeanAbsoluteError):");
				add("            Normalized    Absolute");
				add("           ============  ==========");
				add("      Red: 0.0117114886      767.5");
				add("    Green: 0.0343489217     2251.1");
				add("     Blue: 0.0336219026     2203.4");
				add("  Opacity: 0.0000000000        0.0");
				add("    Total: 0.0199205782     1305.5");
			}
		};
	}

	@BeforeClass
	public static void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(folder.newFolder());
		assertTrue(DiffFiles.getDiffViewerDirectory().mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Test
	public void testParseGmConsoleOutput() throws Exception {
		File mockScreenshot = new File("mockScreenshot.png");
		doNothing().when(gmConsole).run(any(IMOperation.class));

		when(gmConsoleOutputConsumer.getOutput()).thenReturn(OUTPUT_CONSUMER_MOCK);
		final double difference = screenshotComparator.compareScreenshots(mockScreenshot, mockScreenshot, mockScreenshot);
		assertEquals("Difference of screenshots", SCREENSHOT_DIFFERENCE, difference, DOUBLE_TOLERANCE);
	}

	@Test
	public void noComparisonIfScreenshotNotAvailable() throws Exception {
		ScreenshotComparator comparator = spy(new ScreenshotComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME, getComparisonConfiguration()));
		doReturn(true).when(comparator).isGraphicsMagickAvailable();
		comparator.compare("dummy", "dummy", new StepLink("", "", 0, 0, "", 0, 0), "dummy");

		verify(comparator, never()).compareScreenshots(any(File.class), any(File.class), any(File.class));
	}
}
