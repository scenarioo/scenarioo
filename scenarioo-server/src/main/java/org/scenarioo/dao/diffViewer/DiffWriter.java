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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.scenarioo.api.configuration.ScenarioDocuGeneratorConfiguration;
import org.scenarioo.api.exception.ScenarioDocuSaveException;
import org.scenarioo.api.exception.ScenarioDocuTimeoutException;
import org.scenarioo.api.rules.CharacterChecker;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

public class DiffWriter {

	private final DiffFiles diffFiles;

	private final String baseBranchName;

	private final String baseBuildName;

	private final String comparisonName;

	private final ExecutorService asyncWriteExecutor = newAsyncWriteExecutor();

	private final List<RuntimeException> caughtExceptions = new ArrayList<RuntimeException>();

	/**
	 * Initialize with directory inside which to generate the documentation contents.
	 * 
	 * @param destinationDirectory
	 *            the directory where the content should be generated (this directory must be precreated by you!).
	 * @param baseBranchName
	 *            name of the branch we are generating content for
	 * @param baseBuildName
	 *            name of the build (concrete identifier like revision and date) for which we are generating content.
	 * @param comparisonName
	 *            name of the comparison build
	 */
	public DiffWriter(final File destinationRootDirectory, final String baseBranchName, final String baseBuildName,
			final String comparisonName) {
		checkIdentifier(baseBranchName);
		checkIdentifier(baseBuildName);
		checkIdentifier(comparisonName);

		this.diffFiles = new DiffFiles(destinationRootDirectory);
		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonName = comparisonName;

		createComparisonDirectoryIfNotYetExists();
	}

	public void saveBuildDiffInfo(final BuildDiffInfo buildDiffInfo) {
		checkIdentifier(buildDiffInfo.getName());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destBuildFile = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);
				ScenarioDocuXMLFileUtil.marshal(buildDiffInfo, destBuildFile);
			}
		});
	}

	public void saveUseCaseDiffInfo(final UseCaseDiffInfo useCaseDiffInfo) {
		checkIdentifier(useCaseDiffInfo.getName());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destUseCaseDir = diffFiles.getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName,
						useCaseDiffInfo.getName());
				createDirectoryIfNotYetExists(destUseCaseDir);
				File destUseCaseFile = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName,
						useCaseDiffInfo.getName());
				ScenarioDocuXMLFileUtil.marshal(useCaseDiffInfo, destUseCaseFile);
			}
		});
	}

	public void saveScenarioDiffInfo(final ScenarioDiffInfo scenarioDiffInfo, final String useCaseName) {
		checkIdentifier(useCaseName);
		checkIdentifier(scenarioDiffInfo.getName());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destScenarioDir = diffFiles.getScenarioDirectory(baseBranchName, baseBuildName, comparisonName,
						useCaseName, scenarioDiffInfo.getName());
				createDirectoryIfNotYetExists(destScenarioDir);
				File destScenarioFile = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName,
						useCaseName,
						scenarioDiffInfo.getName());
				ScenarioDocuXMLFileUtil.marshal(scenarioDiffInfo, destScenarioFile);
			}
		});
	}

	public void saveStepDiffInfo(final String useCaseName, final String scenarioName, final StepDiffInfo stepDiffInfo) {
		CharacterChecker.checkIdentifier(useCaseName);
		CharacterChecker.checkIdentifier(scenarioName);
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destStepsDir = diffFiles.getStepsDirectory(baseBranchName, baseBuildName, comparisonName,
						useCaseName, scenarioName);
				createDirectoryIfNotYetExists(destStepsDir);
				File destStepFile = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
						scenarioName,
						stepDiffInfo.getIndex());
				ScenarioDocuXMLFileUtil.marshal(stepDiffInfo, destStepFile);
			}
		});
	}


	/**
	 * Finish asynchronous writing of all saved files. This has to be called in the end, to ensure all data saved in
	 * this generator is written to the filesystem.
	 * 
	 * Will block until writing has finished or timeout occurs.
	 * 
	 * @throws ScenarioDocuSaveException
	 *             if any of the save commands throwed an exception during asynchronous execution.
	 * @throws ScenarioDocuTimeoutException
	 *             if waiting for the saving beeing finished exceeds the configured timeout
	 */
	public void flush() {
		int timeoutInSeconds = ScenarioDocuGeneratorConfiguration.INSTANCE
				.getTimeoutWaitingForWritingFinishedInSeconds();
		asyncWriteExecutor.shutdown();
		try {
			boolean terminated = asyncWriteExecutor.awaitTermination(timeoutInSeconds, TimeUnit.SECONDS);
			if (!terminated) {
				asyncWriteExecutor.shutdownNow();
				throw new ScenarioDocuTimeoutException(
						"Timeout occured while waiting for diff files to be written. Writing of files took too long.");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("Async writing of scenarioo docu files was interrupted", e);
		}
		if (!caughtExceptions.isEmpty()) {
			throw new ScenarioDocuSaveException(caughtExceptions);
		}
	}

	private void createComparisonDirectoryIfNotYetExists() {
		createDirectoryIfNotYetExists(diffFiles.getComparisonDirectory(baseBranchName, baseBuildName, comparisonName));
	}

	private void createDirectoryIfNotYetExists(final File directory) {
		diffFiles.assertRootDirectoryExists();
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private void executeAsyncWrite(final Runnable writeTask) {
		asyncWriteExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					writeTask.run();
				} catch (RuntimeException e) {
					caughtExceptions.add(e);
				}
			}
		});
	}

	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread. The executor will
	 * start to block further executions as soon as more than the configured write tasks are waiting for execution.
	 */
	private static ExecutorService newAsyncWriteExecutor() {
		return new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(
				ScenarioDocuGeneratorConfiguration.INSTANCE.getAsyncWriteBufferSize()));
	}

}
