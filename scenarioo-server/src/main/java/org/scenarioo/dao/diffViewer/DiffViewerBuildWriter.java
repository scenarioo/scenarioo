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

import org.scenarioo.api.configuration.ScenarioDocuGeneratorConfiguration;
import org.scenarioo.api.exception.ScenarioDocuSaveException;
import org.scenarioo.api.exception.ScenarioDocuTimeoutException;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Writer to save diff info for one specific build
 */
public class DiffViewerBuildWriter {

	private static final long KEEP_ALIVE_TIME = 60L;
	private static final int MAXIMUM_POOL_SIZE = 1;
	private static final int CORE_POOL_SIZE = 1;

	private final DiffViewerFiles diffFiles  = new DiffViewerFiles();

	private final String baseBranchName;

	private final String baseBuildName;

	private final String comparisonName;

	private final List<RuntimeException> caughtExceptions = new ArrayList<RuntimeException>();

	/**
	 * Initialize with directory to generate the documentation contents.
	 */
	public DiffViewerBuildWriter(final String baseBranchName,
							 final String baseBuildName,
							 final String comparisonName) {

		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonName = comparisonName;

		createComparisonDirectoryIfNotYetExists();
	}

	public void saveBuildDiffInfo(final BuildDiffInfo buildDiffInfo) {
		final File destBuildFile = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);
		ScenarioDocuXMLFileUtil.marshal(buildDiffInfo, destBuildFile);
	}

	public void saveUseCaseDiffInfo(final UseCaseDiffInfo useCaseDiffInfo) {
		final File destUseCaseDir = diffFiles.getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName,
			useCaseDiffInfo.getName());
		createDirectoryIfNotYetExists(destUseCaseDir);
		final File destUseCaseFile = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName,
			useCaseDiffInfo.getName());
		ScenarioDocuXMLFileUtil.marshal(useCaseDiffInfo, destUseCaseFile);
	}

	public void saveScenarioDiffInfo(final ScenarioDiffInfo scenarioDiffInfo, final String useCaseName) {
				final File destScenarioDir = diffFiles.getScenarioDirectory(baseBranchName, baseBuildName,
					comparisonName,
					useCaseName, scenarioDiffInfo.getName());
				createDirectoryIfNotYetExists(destScenarioDir);
				final File destScenarioFile = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName,
					useCaseName,
					scenarioDiffInfo.getName());
				ScenarioDocuXMLFileUtil.marshal(scenarioDiffInfo, destScenarioFile);
	}

	public void saveStepDiffInfo(final String useCaseName, final String scenarioName, final StepDiffInfo stepDiffInfo) {
				final File destStepsDir = diffFiles.getStepsDirectory(baseBranchName, baseBuildName, comparisonName,
					useCaseName, scenarioName);
				createDirectoryIfNotYetExists(destStepsDir);
				final File destStepFile = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName,
					useCaseName,
					scenarioName,
					stepDiffInfo.getIndex());
				ScenarioDocuXMLFileUtil.marshal(stepDiffInfo, destStepFile);
	}


	private void createComparisonDirectoryIfNotYetExists() {
		createDirectoryIfNotYetExists(diffFiles.getComparisonDirectory(baseBranchName, baseBuildName, comparisonName));
	}

	private void createDirectoryIfNotYetExists(final File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

}
