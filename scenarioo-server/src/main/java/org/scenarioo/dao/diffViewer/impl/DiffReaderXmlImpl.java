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
import java.util.List;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

public class DiffReaderXmlImpl implements DiffReader {

	private final DiffFiles diffFiles;

	public DiffReaderXmlImpl() {
		this.diffFiles = new DiffFiles();
	}

	@Override
	public List<BuildDiffInfo> loadBuildDiffInfos(final String baseBranchName, final String baseBuildName) {
		final List<File> files = diffFiles.getBuildFiles(baseBranchName, baseBuildName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(BuildDiffInfo.class, files);
	}

	@Override
	public BuildDiffInfo loadBuildDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		final File file = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshal(BuildDiffInfo.class, file);
	}

	@Override
	public List<UseCaseDiffInfo> loadUseCaseDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		final List<File> files = diffFiles.getUseCaseFiles(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(UseCaseDiffInfo.class, files);
	}

	@Override
	public UseCaseDiffInfo loadUseCaseDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		final File file = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshal(UseCaseDiffInfo.class, file);
	}

	@Override
	public List<ScenarioDiffInfo> loadScenarioDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		final List<File> files = diffFiles.getScenarioFiles(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ScenarioDiffInfo.class, files);
	}

	@Override
	public ScenarioDiffInfo loadScenarioDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName) {
		final File file = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioDiffInfo.class, file);
	}

	@Override
	public List<StepDiffInfo> loadStepDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName) {
		final List<File> files = diffFiles.getStepFiles(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(StepDiffInfo.class, files);
	}

	@Override
	public StepDiffInfo loadStepDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName, final int stepIndex) {
		final File file = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName, stepIndex);

		return ScenarioDocuXMLFileUtil.unmarshal(StepDiffInfo.class, file);
	}

	@Override
	public File getScreenshotFile(final String baseBranchName, final String baseBuildName, final String comparisonName,
			final String useCaseName,
			final String scenarioName, final String imageName) {
		return diffFiles.getScreenshotFile(baseBranchName,
				baseBuildName, comparisonName, useCaseName,
				scenarioName, imageName);
	}

	@Override
	public File getBuildComparisonLogFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		return diffFiles.getBuildComparisonLogFile(baseBranchName, baseBuildName, comparisonName);
	}
}
