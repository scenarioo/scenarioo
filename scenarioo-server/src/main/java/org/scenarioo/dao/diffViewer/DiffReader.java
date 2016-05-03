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

/**
 * Interface to read diff information.
 */
public interface DiffReader {

	/**
	 * Loads a list with build diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch
	 * @param baseBuildName
	 *            the name of the base build
	 * @return list with build diff infos
	 */
	List<BuildDiffInfo> loadBuildDiffInfos(final String baseBranchName, final String baseBuildName);

	/**
	 * Loads the build diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @return the build diff info
	 */
	BuildDiffInfo loadBuildDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName);

	/**
	 * Loads a list with use case diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @return the use case diff infos
	 */
	List<UseCaseDiffInfo> loadUseCaseDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName);

	/**
	 * Loads the use case diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @param stepIndex
	 *            the index of the step in the scenario
	 * @return the use case diff info
	 */
	UseCaseDiffInfo loadUseCaseDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName);

	/**
	 * Loads a list with scenario diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @param useCaseName
	 *            the name of the use case
	 * @return the scenario diff infos
	 */
	List<ScenarioDiffInfo> loadScenarioDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName);

	/**
	 * Loads the scenario diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the scenario diff info
	 */
	ScenarioDiffInfo loadScenarioDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName);

	/**
	 * Loads a list with step diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @return the step diff infos
	 */
	List<StepDiffInfo> loadStepDiffInfos(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName);

	/**
	 * Loads the step diff information.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @param stepIndex
	 *            the index of the step in the scenario
	 * @return the step diff info
	 */
	StepDiffInfo loadStepDiffInfo(final String baseBranchName, final String baseBuildName,
			final String comparisonName, final String useCaseName, final String scenarioName, final int stepIndex);

	/**
	 * Screenshot files are simply provided by path, the REST service will take care of streaming it.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @param useCaseName
	 *            the name of the use case
	 * @param scenarioName
	 *            the name of the scenario
	 * @param imageName
	 *            the name of the image
	 * @return the screenshot file
	 */
	File getScreenshotFile(final String baseBranchName, final String baseBuildName, final String comparisonName,
			final String useCaseName,
			final String scenarioName, final String imageName);

	/**
	 * Gets the log file for a comparison.
	 * 
	 * @param baseBranchName
	 *            the name of the base branch name
	 * @param baseBuildName
	 *            the name of the base build name
	 * @param comparisonName
	 *            the name of the comparison alias
	 * @return the log file
	 */
	File getBuildComparisonLogFile(final String baseBranchName, final String baseBuildName,
			final String comparisonName);
}
