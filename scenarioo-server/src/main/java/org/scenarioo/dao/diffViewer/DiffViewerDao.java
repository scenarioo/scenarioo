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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.entities.Branch;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.repository.RepositoryLocator;

public class DiffViewerDao {

	private final DiffViewerFiles diffFiles = new DiffViewerFiles();

	private final ScenarioDocuReader scenariooReader = new ScenarioDocuReader(RepositoryLocator.INSTANCE.getConfigurationRepository().getDocumentationDataDirectory());

	public List<BuildDiffInfo> loadAllBuildDiffInfos() {
		List<BuildDiffInfo> buildDiffInfos = new ArrayList<>();
		scenariooReader.loadBranches().forEach(new Consumer<Branch>() {
			@Override
			public void accept(Branch branch) {
				scenariooReader.loadBuilds(branch.getName()).forEach(new Consumer<ObjectFromDirectory<Build>>() {
					@Override
					public void accept(ObjectFromDirectory<Build> build) {
						buildDiffInfos.addAll(loadBuildDiffInfos(branch.getName(), build.getObject().getName()));
					}
				});
			}
		});
		return buildDiffInfos;
	}

	public List<BuildDiffInfo> loadBuildDiffInfos(final String baseBranchName, final String baseBuildName) {
		final List<File> files = diffFiles.getBuildFiles(baseBranchName, baseBuildName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(BuildDiffInfo.class, files);
	}

	public BuildDiffInfo loadBuildDiffInfo(final String baseBranchName, final String baseBuildName,
										   final String comparisonName) {
		final File file = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshal(BuildDiffInfo.class, file);
	}

	public List<UseCaseDiffInfo> loadUseCaseDiffInfos(final String baseBranchName, final String baseBuildName,
													  final String comparisonName) {
		final List<File> files = diffFiles.getUseCaseFiles(baseBranchName, baseBuildName, comparisonName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(UseCaseDiffInfo.class, files);
	}

	public UseCaseDiffInfo loadUseCaseDiffInfo(final String baseBranchName, final String baseBuildName,
											   final String comparisonName, final String useCaseName) {
		final File file = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshal(UseCaseDiffInfo.class, file);
	}

	public List<ScenarioDiffInfo> loadScenarioDiffInfos(final String baseBranchName, final String baseBuildName,
														final String comparisonName, final String useCaseName) {
		final List<File> files = diffFiles.getScenarioFiles(baseBranchName, baseBuildName, comparisonName, useCaseName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ScenarioDiffInfo.class, files);
	}

	public ScenarioDiffInfo loadScenarioDiffInfo(final String baseBranchName, final String baseBuildName,
												 final String comparisonName, final String useCaseName, final String scenarioName) {
		final File file = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
			scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioDiffInfo.class, file);
	}

	public List<StepDiffInfo> loadStepDiffInfos(final String baseBranchName, final String baseBuildName,
												final String comparisonName, final String useCaseName, final String scenarioName) {
		final List<File> files = diffFiles.getStepFiles(baseBranchName, baseBuildName, comparisonName, useCaseName,
			scenarioName);

		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(StepDiffInfo.class, files);
	}

	public StepDiffInfo loadStepDiffInfo(final String baseBranchName, final String baseBuildName,
										 final String comparisonName, final String useCaseName, final String scenarioName, final int stepIndex) {
		final File file = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
			scenarioName, stepIndex);

		return ScenarioDocuXMLFileUtil.unmarshal(StepDiffInfo.class, file);
	}

	public File getScreenshotFile(final String baseBranchName, final String baseBuildName, final String comparisonName,
								  final String useCaseName,
								  final String scenarioName, final String imageName) {
		return diffFiles.getScreenshotFile(baseBranchName,
			baseBuildName, comparisonName, useCaseName,
			scenarioName, imageName);
	}

	public File getBuildComparisonLogFile(final String baseBranchName, final String baseBuildName,
										  final String comparisonName) {
		return diffFiles.getBuildComparisonLogFile(baseBranchName, baseBuildName, comparisonName);
	}

	public DiffViewerBuildWriter getBuildDiffWriter(final String baseBranchName, final String baseBuildName,
													final String comparisonName) {
		return new DiffViewerBuildWriter(baseBranchName, baseBuildName, comparisonName);
	}

}
