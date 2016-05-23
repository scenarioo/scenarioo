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
import org.scenarioo.utils.NumberFormatCreator;

/**
 * Represents the diff file structure.
 */
public class DiffFiles {

	private static final String COMPARISON_LOGFILE_NAME = "comparison.derived.log";
	private static final String DIRECTORY_NAME_SCENARIO_SCREENSHOTS = "screenshots";
	private static final String DIRECTORY_NAME_SCENARIO_STEPS = "steps";
	private static final String FILE_NAME_BUILD = "build.xml";
	private static final String FILE_NAME_USECASE = "usecase.xml";
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	private static final NumberFormat THREE_DIGIT_NUM_FORMAT = NumberFormatCreator
			.createNumberFormatWithMinimumIntegerDigits(3);

	private final File rootDirectory;

	public DiffFiles(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	/**
	 * Gets the root directory for the diff files.
	 * 
	 * @return the root directory
	 */
	public File getRootDirectory() {
		return rootDirectory;
	}

	/**
	 * Gets the base branch directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @return the base branch directory
	 */
	public File getBaseBranchDirectory(final String baseBranchName) {
		return new File(rootDirectory, FilesUtil.encodeName(baseBranchName));
	}

	/**
	 * Gets the base build directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @return the base build directory
	 */
	public File getBaseBuildDirectory(final String baseBranchName, final String baseBuildName) {
		return new File(getBaseBranchDirectory(baseBranchName), FilesUtil.encodeName(baseBuildName));
	}

	/**
	 * Gets the comparison directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @return the comparison directory
	 */
	public File getComparisonDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return new File(getBaseBuildDirectory(baseBranchName, baseBuildName), FilesUtil.encodeName(comparisonName));
	}

	/**
	 * Gets the xml build file.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @return the build file
	 */
	public File getBuildFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return new File(getComparisonDirectory(baseBranchName, baseBuildName, comparisonName), FILE_NAME_BUILD);
	}

	/**
	 * Gets a list of all xml build files.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @return a list of build files
	 */
	public List<File> getBuildFiles(final String baseBranchName, final String baseBuildName) {
		return FilesUtil.getListOfFilesFromSubdirs(getBaseBuildDirectory(baseBranchName, baseBuildName),
				FILE_NAME_BUILD);
	}

	/**
	 * Gets the use case directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @return the use case directory
	 */
	public File getUseCaseDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		return new File(getComparisonDirectory(baseBranchName, baseBuildName, comparisonName),
				FilesUtil.encodeName(useCaseName));
	}

	/**
	 * Gets the xml use case file.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @return the use case file
	 */
	public File getUseCaseFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		return new File(getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName),
				FILE_NAME_USECASE);
	}

	/**
	 * Gets a list of all xml use case files.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @return the use case files
	 */
	public List<File> getUseCaseFiles(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return FilesUtil.getListOfFilesFromSubdirs(
				getComparisonDirectory(baseBranchName, baseBuildName, comparisonName), FILE_NAME_USECASE);
	}

	/**
	 * Gets the scenario directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the scenario directory
	 */
	public File getScenarioDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return new File(
				getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName),
				FilesUtil.encodeName(scenarioName));
	}

	/**
	 * Gets the xml scenario file.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the scenario file
	 */
	public File getScenarioFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return new File(getScenarioDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				FILE_NAME_SCENARIO);
	}

	/**
	 * Gets a list of all xml scenario files.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @return the scenario files
	 */
	public List<File> getScenarioFiles(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		return FilesUtil.getListOfFilesFromSubdirs(
				getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName),
				FILE_NAME_SCENARIO);
	}

	/**
	 * Gets the steps directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the steps directory
	 */
	public File getStepsDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return new File(getScenarioDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				DIRECTORY_NAME_SCENARIO_STEPS);
	}

	/**
	 * Gets the xml step file.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @param stepIndex
	 *            the index of the step in the scenario
	 * @return the step file
	 */
	public File getStepFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		return new File(getStepsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".xml");
	}

	/**
	 * Gets a list of all xml step files.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the step files
	 */
	public List<File> getStepFiles(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName,
			final String scenarioName) {
		return FilesUtil.getListOfFiles(getStepsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName));
	}

	/**
	 * Gets the screenshots directory.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the screenshots directory
	 */
	public File getScreenshotsDirectory(final String baseBranchName, final String baseBuildName,
			final String comparisonName,
			final String useCaseName, final String scenarioName) {
		return new File(getScenarioDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
				DIRECTORY_NAME_SCENARIO_SCREENSHOTS);
	}

	/**
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @param imageName
	 *            the name of the image
	 * @return A {@link File} object pointing to the PNG file of the step screenshot. The method does not care whether
	 *         the file actually exists.
	 */
	public File getScreenshotFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName,
			final String useCaseName, final String scenarioName, final String imageName) {
		return new File(getScreenshotsDirectory(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName),
				imageName);
	}

	/**
	 * Checks if the root directory for the diff content exists.
	 */
	public void assertRootDirectoryExists() {
		if (!rootDirectory.exists()) {
			throw new IllegalArgumentException("Directory for diff content does not exist: "
					+ rootDirectory.getAbsolutePath());
		}
	}

	/**
	 * Gets the log file for a comparison.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @param comparisonName
	 *            the name of the comparison configuration
	 * @return the log file
	 */
	public File getBuildComparisonLogFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		final File comparisonDirectory = getComparisonDirectory(baseBranchName, baseBuildName, comparisonName);
		if (!comparisonDirectory.exists()) {
			comparisonDirectory.mkdirs();
		}
		return new File(comparisonDirectory, COMPARISON_LOGFILE_NAME);
	}
}
