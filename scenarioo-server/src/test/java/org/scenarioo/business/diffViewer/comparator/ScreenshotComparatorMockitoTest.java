package org.scenarioo.business.diffViewer.comparator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scenarioo.dao.diffViewer.DiffViewerFiles;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.TestFileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

@ExtendWith(MockitoExtension.class)
class ScreenshotComparatorMockitoTest {

	@TempDir
	static File folder;

	@BeforeAll
	static void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(folder);
		File comparisonsFolder = new DiffViewerFiles().getComparisonDirectory(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
		assertTrue(comparisonsFolder.mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Test
	void noComparisonIfScreenshotNotAvailable() throws Exception {
		ScreenshotComparator comparator = spy(new ScreenshotComparator());
		comparator.compare(getComparatorParameters(),"dummy", "dummy", new StepLink("", "", 0, 0, "", 0, 0), "dummy");

		verify(comparator, never()).compareScreenshots(any(ComparisonParameters.class), any(File.class), any(File.class), any(File.class));
	}
}
