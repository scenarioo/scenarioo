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

import static org.scenarioo.api.rules.CharacterChecker.*;

import java.io.File;
import java.util.List;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

public class DiffReader {

	private final DiffFiles diffFiles;

	public DiffReader(final File rootDirectory) {
		this.diffFiles = new DiffFiles(rootDirectory);
	}

	public List<BuildDiffInfo> loadBuildDiffInfos(final String baseBranchName, final String baseBuildName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);

		List<File> files = diffFiles.getBuildFiles(baseBranchName, baseBuildName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(BuildDiffInfo.class, files);
	}

	public BuildDiffInfo loadBuildDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);

		File file = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshal(BuildDiffInfo.class, file);
	}

	public List<UseCaseDiffInfo> loadUseCaseDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);

		List<File> files = diffFiles.getUseCaseFiles(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(UseCaseDiffInfo.class, files);
	}

	public UseCaseDiffInfo loadUseCaseDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);

		File file = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshal(UseCaseDiffInfo.class, file);
	}

	public List<ScenarioDiffInfo> loadScenarioDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);

		List<File> files = diffFiles.getScenarioFiles(baseBranchName, baseBuildName, comparisonName, useCaseName);
		
	
		
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ScenarioDiffInfo.class, files);
	}

	public ScenarioDiffInfo loadScenarioDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioName);

		File file = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioDiffInfo.class, file);
	}

	public List<StepDiffInfo> loadStepDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioName);

		List<File> files = diffFiles.getStepFiles(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(StepDiffInfo.class, files);
	}

	public StepDiffInfo loadStepDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName, final int stepIndex) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioName);

		File file = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName,
				stepIndex);

		return ScenarioDocuXMLFileUtil.unmarshal(StepDiffInfo.class, file);
	}

	/**
	 * Screenshot files are simply provided by path, the REST service will take care of streaming it.
	 */
	public File getScreenshotFile(final String baseBranchName, final String baseBuildName, final String comparisonName,
			final String useCaseName,
			final String scenarioName, final String imageName) {
		return new File(
				"/home/scenarioo/scenarioo/scenarioo-server/src/test/resources/org/scenarioo/business/diffViewer/comparisonScreenshot.png");
		// TODO: mscheube: remove Testfile
		// return new File(diffFiles.getScreenshotsFile(checkIdentifier(baseBranchName),
		// checkIdentifier(baseBuildName), checkIdentifier(comparisonName), checkIdentifier(useCaseName),
		// checkIdentifier(scenarioName)), imageName);
	}
}
