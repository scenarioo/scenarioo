/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.dao.diffViewer.impl;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.NumberFormatCreator;

public class DiffFiles {

	private static final String DIFF_VIEWER_DIRECTORY = "scenarioo-application-data/diffViewer";
	private static final String COMPARISON_LOGFILE_NAME = "comparison.derived.log";
	private static final String DIRECTORY_NAME_SCENARIO_SCREENSHOTS = "screenshots";
	private static final String DIRECTORY_NAME_SCENARIO_STEPS = "steps";
	private static final String FILE_NAME_BUILD = "build.xml";
	private static final String FILE_NAME_USECASE = "usecase.xml";
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	private static final NumberFormat THREE_DIGIT_NUM_FORMAT = NumberFormatCreator
			.createNumberFormatWithMinimumIntegerDigits(3);

	private final File diffViewerDirectory;

	public DiffFiles() {
		diffViewerDirectory = getDiffViewerDirectory();
	}

	public static File getDiffViewerDirectory() {
		final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
				.getConfigurationRepository();
		final File rootDirectory = configurationRepository.getDocumentationDataDirectory();
		return new File(rootDirectory, DIFF_VIEWER_DIRECTORY);
	}

	public File getBaseBranchDirectory(final String baseBranchName) {
		return new File(diffViewerDirectory, FilesUtil.encodeName(baseBranchName));
	}

	public File getBaseBuildDirectory(final String baseBranchName, final String baseBuildName) {
		return new File(getBaseBranchDirectory(baseBranchName), FilesUtil.encodeName(baseBuildName));
	}

	public File getComparisonDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return new File(getBaseBuildDirectory(baseBranchName, baseBuildName), FilesUtil.encodeName(comparisonName));
	}

	public File getBuildFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return new File(getComparisonDirectory(baseBranchName, baseBuildName, comparisonName), FILE_NAME_BUILD);
	}

	public List<File> getBuildFiles(final String baseBranchName, final String baseBuildName) {
		return FilesUtil.getListOfFilesFromSubdirs(getBaseBuildDirectory(baseBranchName, baseBuildName),
				FILE_NAME_BUILD);
	}

	public File getUseCaseDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		return new File(getComparisonDirectory(baseBranchName, baseBuildName, comparisonName),
				FilesUtil.encodeName(useCaseName));
	}

	public File getUseCaseFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		return new File(getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName),
				FILE_NAME_USECASE);
	}

	public List<File> getUseCaseFiles(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return FilesUtil.getListOfFilesFromSubdirs(
				getComparisonDirectory(baseBranchName, baseBuildName, comparisonName), FILE_NAME_USECASE);
	}

	public File getScenarioDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return new File(
				getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName),
				FilesUtil.encodeName(scenarioName));
	}

	public File getScenarioFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return new File(getScenarioDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				FILE_NAME_SCENARIO);
	}

	public List<File> getScenarioFiles(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		return FilesUtil.getListOfFilesFromSubdirs(
				getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName),
				FILE_NAME_SCENARIO);
	}

	public File getStepsDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return new File(getScenarioDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				DIRECTORY_NAME_SCENARIO_STEPS);
	}

	public File getStepFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		return new File(getStepsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".xml");
	}

	public List<File> getStepFiles(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return FilesUtil.getListOfFiles(getStepsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName));
	}

	public File getScreenshotsDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName,
			final String useCaseName, final String scenarioName) {
		return new File(getScenarioDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				DIRECTORY_NAME_SCENARIO_SCREENSHOTS);
	}

	public File getScreenshotFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName,
			final String useCaseName, final String scenarioName, final String imageName) {
		return new File(getScreenshotsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName),
				imageName);
	}

	public void assertRootDirectoryExists() {
		if (!diffViewerDirectory.exists()) {
			throw new IllegalArgumentException("Directory for diff content does not exist: "
					+ diffViewerDirectory.getAbsolutePath());
		}
	}

	public File getBuildComparisonLogFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		final File comparisonDirectory = getComparisonDirectory(baseBranchName, baseBuildName, comparisonName);
		if (!comparisonDirectory.exists()) {
			comparisonDirectory.mkdirs();
		}
		return new File(comparisonDirectory, COMPARISON_LOGFILE_NAME);
	}
}
