package org.scenarioo.business.diffViewer.comparator;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.dao.diffViewer.impl.DiffFiles;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.TestFileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

@RunWith(MockitoJUnitRunner.class)
public class ScreenshotComparatorMockitoTest {

	@InjectMocks
	private final ScreenshotComparator screenshotComparator = new ScreenshotComparator();

	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	@BeforeClass
	public static void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(folder.newFolder());
		File comparisonsFolder = new DiffFiles().getComparisonDirectory(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
		assertTrue(comparisonsFolder.mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Test
	public void noComparisonIfScreenshotNotAvailable() throws Exception {
		ScreenshotComparator comparator = spy(new ScreenshotComparator());
		comparator.compare(getComparatorParameters(),"dummy", "dummy", new StepLink("", "", 0, 0, "", 0, 0), "dummy");

		verify(comparator, never()).compareScreenshots(any(ComparisonParameters.class), any(File.class), any(File.class), any(File.class));
	}
}
