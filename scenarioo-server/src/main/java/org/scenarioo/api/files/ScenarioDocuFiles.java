/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules, according
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.api.files;

import org.scenarioo.api.util.files.FilesUtil;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

/**
 * Represents the file structure of the documentation.
 */
public class ScenarioDocuFiles {

	private static final String DIRECTORY_NAME_SCENARIO_SCREENSHOTS = "screenshots";

	private static final String DIRECTORY_NAME_SCENARIO_STEPS = "steps";

	private static final String FILE_NAME_SCENARIO = "scenario.xml";

	private static final String FILE_NAME_CASE = "feature.xml";

	private static final String FILE_NAME_BUILD = "build.xml";

	private static final String FILE_NAME_BRANCH = "branch.xml";

	private static NumberFormat THREE_DIGIT_NUM_FORMAT = createNumberFormatWithMinimumIntegerDigits(3);

	private final File rootDirectory;

	public ScenarioDocuFiles(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public void assertRootDirectoryExists() {
		if (!rootDirectory.exists()) {
			throw new IllegalArgumentException("Directory for docu content generation does not exist: "
					+ rootDirectory.getAbsolutePath());
		}
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public File getBranchDirectory(final String branchName) {
		File branchDirectory = new File(rootDirectory, FilesUtil.encodeName(branchName));
		return branchDirectory;
	}

	public File getBranchFile(final String branchName) {
		return new File(getBranchDirectory(branchName), FILE_NAME_BRANCH);
	}

	public List<File> getBranchFiles() {
		return FilesUtil.getListOfFilesFromSubdirs(rootDirectory, FILE_NAME_BRANCH);
	}

	public File getBuildDirectory(final String branchName, final String buildName) {
		File buildDirectory = new File(getBranchDirectory(branchName), FilesUtil.encodeName(buildName));
		return buildDirectory;
	}

	public File getBuildFile(final String branchName, final String buildName) {
		return new File(getBuildDirectory(branchName, buildName), FILE_NAME_BUILD);
	}

	public List<File> getBuildFiles(final String branchName) {
		return FilesUtil.getListOfFilesFromSubdirs(getBranchDirectory(branchName), FILE_NAME_BUILD);
	}

	public File getFeatureDirectory(final String branchName, final String buildName, final String featureName) {
		File branchDirectory = new File(getBuildDirectory(branchName, buildName), FilesUtil.encodeName(featureName));
		return branchDirectory;
	}

	public File getFeatureFile(final String branchName, final String buildName, final String featureName) {
		return new File(getFeatureDirectory(branchName, buildName, featureName), FILE_NAME_CASE);
	}

	public List<File> getFeatureFiles(final String branchName, final String buildName) {
		return FilesUtil.getListOfFilesFromSubdirs(getBuildDirectory(branchName, buildName), FILE_NAME_CASE);
	}

	public File getScenarioDirectory(final String branchName, final String buildName, final String featureName,
			final String scenarioName) {
		File branchDirectory = new File(getFeatureDirectory(branchName, buildName, featureName),
				FilesUtil.encodeName(scenarioName));
		return branchDirectory;
	}

	public File getScenarioFile(final String branchName, final String buildName, final String featureName,
			final String scenarioName) {
		return new File(getScenarioDirectory(branchName, buildName, featureName, scenarioName), FILE_NAME_SCENARIO);
	}

	public List<File> getScenarioFiles(final String branchName, final String buildName, final String featureName) {

		System.out.println(branchName);
		System.out.println(buildName);
		System.out.println(featureName);

		System.out.println(getFeatureDirectory(branchName, buildName, featureName));

		return FilesUtil.getListOfFilesFromSubdirs(getFeatureDirectory(branchName, buildName, featureName),
				FILE_NAME_SCENARIO);
	}

	public File getStepsDirectory(final String branchName, final String buildName, final String featureName,
			final String scenarioName) {
		File branchDirectory = new File(getScenarioDirectory(branchName, buildName, featureName, scenarioName),
				DIRECTORY_NAME_SCENARIO_STEPS);
		return branchDirectory;
	}

	public File getStepFile(final String branchName, final String buildName, final String featureName,
			final String scenarioName, final int stepIndex) {
		return new File(getStepsDirectory(branchName, buildName, featureName, scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".xml");
	}

	public List<File> getStepFiles(final String branchName, final String buildName, final String featureName,
			final String scenarioName) {
		return FilesUtil.getListOfFiles(getStepsDirectory(branchName, buildName, featureName, scenarioName));
	}

	public File getScreenshotsDirectory(final String branchName, final String buildName,
			final String featureName, final String scenarioName) {
		return new File(getScenarioDirectory(branchName, buildName, featureName, scenarioName),
				DIRECTORY_NAME_SCENARIO_SCREENSHOTS);
	}

	/**
	 * @return A {@link File} object pointing to the PNG file of the step screenshot. The method does not care whether
	 *         the file actually exists.
	 */
	public File getScreenshotFile(final String branchName, final String buildName,
			final String featureName, final String scenarioName, final int stepIndex) {
		return new File(getScreenshotsDirectory(branchName, buildName, featureName, scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".png");
	}

	private static NumberFormat createNumberFormatWithMinimumIntegerDigits(
			final int minimumIntegerDigits) {
		final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		return numberFormat;
	}

}
