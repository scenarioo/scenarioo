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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.scenarioo.api.configuration.ScenarioDocuGeneratorConfiguration;
import org.scenarioo.api.exception.ScenarioDocuSaveException;
import org.scenarioo.api.exception.ScenarioDocuTimeoutException;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;

public class DiffWriterXmlImpl implements DiffWriter {

	private static final long KEEP_ALIVE_TIME = 60L;
	private static final int MAXIMUM_POOL_SIZE = 1;
	private static final int CORE_POOL_SIZE = 1;

	private final DiffFiles diffFiles;

	private final String baseBranchName;

	private final String baseBuildName;

	private final String comparisonName;

	private final ExecutorService asyncWriteExecutor = newAsyncWriteExecutor();

	private final List<RuntimeException> caughtExceptions = new ArrayList<RuntimeException>();

	/**
	 * Initialize with directory to generate the documentation contents.
	 */
	public DiffWriterXmlImpl(final String baseBranchName,
			final String baseBuildName,
			final String comparisonName) {
		this.diffFiles = new DiffFiles();
		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonName = comparisonName;

		createComparisonDirectoryIfNotYetExists();
	}

	@Override
	public void saveBuildDiffInfo(final BuildDiffInfo buildDiffInfo) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				final File destBuildFile = diffFiles.getBuildFile(baseBranchName, baseBuildName, comparisonName);
				ScenarioDocuXMLFileUtil.marshal(buildDiffInfo, destBuildFile);
			}
		});
	}

	@Override
	public void saveUseCaseDiffInfo(final UseCaseDiffInfo useCaseDiffInfo) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				final File destUseCaseDir = diffFiles.getUseCaseDirectory(baseBranchName, baseBuildName, comparisonName,
						useCaseDiffInfo.getName());
				createDirectoryIfNotYetExists(destUseCaseDir);
				final File destUseCaseFile = diffFiles.getUseCaseFile(baseBranchName, baseBuildName, comparisonName,
						useCaseDiffInfo.getName());
				ScenarioDocuXMLFileUtil.marshal(useCaseDiffInfo, destUseCaseFile);
			}
		});
	}

	@Override
	public void saveScenarioDiffInfo(final ScenarioDiffInfo scenarioDiffInfo, final String useCaseName) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				final File destScenarioDir = diffFiles.getScenarioDirectory(baseBranchName, baseBuildName,
						comparisonName,
						useCaseName, scenarioDiffInfo.getName());
				createDirectoryIfNotYetExists(destScenarioDir);
				final File destScenarioFile = diffFiles.getScenarioFile(baseBranchName, baseBuildName, comparisonName,
						useCaseName,
						scenarioDiffInfo.getName());
				ScenarioDocuXMLFileUtil.marshal(scenarioDiffInfo, destScenarioFile);
			}
		});
	}

	@Override
	public void saveStepDiffInfo(final String useCaseName, final String scenarioName, final StepDiffInfo stepDiffInfo) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				final File destStepsDir = diffFiles.getStepsDirectory(baseBranchName, baseBuildName, comparisonName,
						useCaseName, scenarioName);
				createDirectoryIfNotYetExists(destStepsDir);
				final File destStepFile = diffFiles.getStepFile(baseBranchName, baseBuildName, comparisonName,
						useCaseName,
						scenarioName,
						stepDiffInfo.getIndex());
				ScenarioDocuXMLFileUtil.marshal(stepDiffInfo, destStepFile);
			}
		});
	}

	@Override
	public void flush() {
		final int timeoutInSeconds = ScenarioDocuGeneratorConfiguration.INSTANCE
				.getTimeoutWaitingForWritingFinishedInSeconds();
		asyncWriteExecutor.shutdown();
		try {
			final boolean terminated = asyncWriteExecutor.awaitTermination(timeoutInSeconds, TimeUnit.SECONDS);
			if (!terminated) {
				asyncWriteExecutor.shutdownNow();
				throw new ScenarioDocuTimeoutException(
						"Timeout occured while waiting for diff files to be written. Writing of files took too long.");
			}
		} catch (final InterruptedException e) {
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
				} catch (final RuntimeException e) {
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
		return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(
						ScenarioDocuGeneratorConfiguration.INSTANCE.getAsyncWriteBufferSize()));
	}

}
