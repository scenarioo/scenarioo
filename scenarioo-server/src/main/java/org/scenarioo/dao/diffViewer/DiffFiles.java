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

package org.scenarioo.dao.diffViewer;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import org.scenarioo.api.util.files.FilesUtil;

/**
 * Represents the diff file structure.
 */
public class DiffFiles {

	private static final String DIRECTORY_NAME_SCENARIO_SCREENSHOTS = "screenshots";
	private static final String DIRECTORY_NAME_SCENARIO_STEPS = "steps";
	private static final String FILE_NAME_BUILD = "build.xml";
	private static final String FILE_NAME_USECASE = "usecase.xml";
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	private static final NumberFormat THREE_DIGIT_NUM_FORMAT = createNumberFormatWithMinimumIntegerDigits(3);

	private final File rootDirectory;

	public DiffFiles(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public File getBaseBranchDirectory(final String baseBranchName) {
		return new File(rootDirectory, FilesUtil.encodeName(baseBranchName));
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

	/**
	 * @return A {@link File} object pointing to the PNG file of the step screenshot. The method does not care whether
	 *         the file actually exists.
	 */
	public File getScreenshotFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName,
			final String useCaseName, final String scenarioName, final int stepIndex) {
		return new File(getScreenshotsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".png");
	}

	public void assertRootDirectoryExists() {
		if (!rootDirectory.exists()) {
			throw new IllegalArgumentException("Directory for diff content does not exist: "
					+ rootDirectory.getAbsolutePath());
		}
	}

	private static NumberFormat createNumberFormatWithMinimumIntegerDigits(
			final int minimumIntegerDigits) {
		final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		return numberFormat;
	}
}
