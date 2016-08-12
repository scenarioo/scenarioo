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
import java.util.List;

import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

public interface DiffReader {

	List<BuildDiffInfo> loadBuildDiffInfos(String baseBranchName, String baseBuildName);

	BuildDiffInfo loadBuildDiffInfo(String baseBranchName, String baseBuildName,
			String comparisonName);

	List<UseCaseDiffInfo> loadUseCaseDiffInfos(String baseBranchName, String baseBuildName,
			String comparisonName);

	UseCaseDiffInfo loadUseCaseDiffInfo(String baseBranchName, String baseBuildName,
			String comparisonName, String useCaseName);

	List<ScenarioDiffInfo> loadScenarioDiffInfos(String baseBranchName, String baseBuildName,
			String comparisonName, String useCaseName);

	ScenarioDiffInfo loadScenarioDiffInfo(String baseBranchName, String baseBuildName,
			String comparisonName, String useCaseName, String scenarioName);

	List<StepDiffInfo> loadStepDiffInfos(String baseBranchName, String baseBuildName,
			String comparisonName, String useCaseName, String scenarioName);

	StepDiffInfo loadStepDiffInfo(String baseBranchName, String baseBuildName,
			String comparisonName, String useCaseName, String scenarioName, int stepIndex);

	File getScreenshotFile(String baseBranchName, String baseBuildName, String comparisonName,
			String useCaseName,
			String scenarioName, String imageName);

	File getBuildComparisonLogFile(String baseBranchName, String baseBuildName,
			String comparisonName);
}
