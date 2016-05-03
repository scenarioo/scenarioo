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

import static org.scenarioo.api.rules.CharacterChecker.*;

import java.io.File;
import java.util.List;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

/**
 * Implements the diff info access via xml files.
 */
public class DiffReaderXmlImpl implements DiffReader {

	private final DiffFiles diffFiles;

	public DiffReaderXmlImpl(final File rootDirectory) {
		this.diffFiles = new DiffFiles(rootDirectory);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadBuildDiffInfos(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BuildDiffInfo> loadBuildDiffInfos(final String baseBranchName, final String baseBuildName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);

		final List<File> files = diffFiles.getBuildFiles(baseBranchName, baseBuildName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(BuildDiffInfo.class, files);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadBuildDiffInfo(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public BuildDiffInfo loadBuildDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);

		final File file = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshal(BuildDiffInfo.class, file);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadUseCaseDiffInfos(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<UseCaseDiffInfo> loadUseCaseDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);

		final List<File> files = diffFiles.getUseCaseFiles(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(UseCaseDiffInfo.class, files);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadUseCaseDiffInfo(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public UseCaseDiffInfo loadUseCaseDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);

		final File file = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshal(UseCaseDiffInfo.class, file);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadScenarioDiffInfos(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<ScenarioDiffInfo> loadScenarioDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);

		final List<File> files = diffFiles.getScenarioFiles(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ScenarioDiffInfo.class, files);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadScenarioDiffInfo(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ScenarioDiffInfo loadScenarioDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioName);

		final File file = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioDiffInfo.class, file);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadStepDiffInfos(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<StepDiffInfo> loadStepDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioName);

		final List<File> files = diffFiles.getStepFiles(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(StepDiffInfo.class, files);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#loadStepDiffInfo(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public StepDiffInfo loadStepDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName, final int stepIndex) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioName);

		final File file = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName,
				stepIndex);

		return ScenarioDocuXMLFileUtil.unmarshal(StepDiffInfo.class, file);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#getScreenshotFile(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public File getScreenshotFile(final String baseBranchName, final String baseBuildName, final String comparisonName,
			final String useCaseName,
			final String scenarioName, final String imageName) {
		return diffFiles.getScreenshotFile(checkIdentifier(baseBranchName),
				checkIdentifier(baseBuildName), checkIdentifier(comparisonName), checkIdentifier(useCaseName),
				checkIdentifier(scenarioName), imageName);
	}

	/**
	 * @see org.scenarioo.dao.diffViewer.DiffReader#getBuildComparisonLogFile(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public File getBuildComparisonLogFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return diffFiles.getBuildComparisonLogFile(baseBranchName, baseBuildName, comparisonName);
	}
}
