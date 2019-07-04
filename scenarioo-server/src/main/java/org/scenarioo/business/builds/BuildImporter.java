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

package org.scenarioo.business.builds;

import org.apache.log4j.Logger;
import org.scenarioo.business.aggregator.ScenarioDocuAggregator;
import org.scenarioo.business.diffViewer.ComparisonExecutor;
import org.scenarioo.business.lastSuccessfulScenarios.LastSuccessfulScenariosBuild;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.ThreadLogAppender;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * Takes care of importing builds.
 *
 * Also submitts comparisons for the imported builds.
 *
 * This is done asynch with two separate asynch executed queues:
 * 1. Executor in BuildImporter executes import tasks in one thread
 * 2. Executor in ComparisonExecutor executes comparisons in another thread (such that further imports can run in parallel)
 */
public class BuildImporter {

	/**
	 * Log entry text that the CI/CD build can check for after a deployment of a new version to verify all imports and comparisons have been executed.
	 *
	 * Be aware that changing this text means also to adjust our CI/CD pipeline accordingly.
	 */
	private static final String ALL_BUILD_IMPORTS_AND_COMPARISONS_FINISHED_LOG_ENTRY_FOR_CI_CHECK = "All Builds Imported and Comparisons Calculated.";

	private static final Logger LOGGER = Logger.getLogger(BuildImporter.class);

	private ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
		getConfigurationRepository().getDocumentationDataDirectory());

	/**
	 * Current state for all builds whether imported and aggregated correctly.
	 */
	private Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = new HashMap<>();

	/**
	 * Builds that have been scheduled for processing (waiting for import)
	 */
	private final Set<BuildIdentifier> buildsInProcessingQueue = new HashSet<>();

	/**
	 * Builds currently beeing imported.
	 */
	private Set<BuildIdentifier> buildsBeeingImported = new HashSet<>();

	/**
	 * Executor to execute one import task after the other asynchronously.
	 */
	private final ThreadPoolExecutor asyncBuildImportExecutor = newAsyncBuildImportExecutor();

	private ComparisonExecutor comparisonExecutor = new ComparisonExecutor(new LazyAliasResolver());

	private final LastSuccessfulScenariosBuild lastSuccessfulScenarioBuild = new LastSuccessfulScenariosBuild();

	public Map<BuildIdentifier, BuildImportSummary> getBuildImportSummaries() {
		return buildImportSummaries;
	}

	List<BuildImportSummary> getBuildImportSummariesAsList() {
		return new ArrayList<>(buildImportSummaries.values());
	}

	public synchronized void updateBuildImportStates(List<BranchBuilds> branchBuildsList,
													 Map<BuildIdentifier, BuildImportSummary> loadedBuildSummaries) {
		Map<BuildIdentifier, BuildImportSummary> result = new HashMap<>();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			for (BuildLink buildLink : branchBuilds.getBuilds()) {
				// Take existent summary or create new one.
				BuildIdentifier buildIdentifier = new BuildIdentifier(branchBuilds.getBranch().getName(),
					buildLink.getBuild().getName());
				BuildImportSummary buildSummary = loadedBuildSummaries.get(buildIdentifier);
				if (buildSummary == null) {
					buildSummary = new BuildImportSummary(branchBuilds.getBranch().getName(), buildLink.getBuild());
				}
				ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator(buildSummary);
				aggregator.updateBuildSummary(buildLink);
				if (buildsBeeingImported.contains(buildIdentifier)) {
					buildSummary.setStatus(BuildImportStatus.PROCESSING);
				} else if (buildsInProcessingQueue.contains(buildIdentifier)) {
					buildSummary.setStatus(BuildImportStatus.QUEUED_FOR_PROCESSING);
				}
				result.put(buildIdentifier, buildSummary);
			}
		}
		saveBuildImportSummaries(result);
		buildImportSummaries = result;
	}

	/**
	 * Loops over given builds and submits all builds that are not yet imported.
	 *
	 * The flow is as follows:
	 * 1. All Builds are executed and every Build schedules his Comparisons with the AsyncBuildImporterExecutor to ensure
	 *    that they are only processed after all Builds have been imported (to make sure the build to be compared with is also imported first).
	 * 2. All Comparisons are submitted as tasks to ComparisonExecutor to execute in separate thread
	 * 3. ComparisonExecutor processes all Comparisons in separate thread
	 * 4. After everything has been processed a log message that all submitted imports and comparisons have been processed is logged.
	 *
	 * @return Returns the number of builds that were submitted (that need to be imported)
	 */
	public synchronized int submitUnprocessedBuildsForImport(AvailableBuildsList availableBuilds) {
		List<BuildImportSummary> buildsSortedByDateDescending = BuildByDateSorter
			.sortBuildsByDateDescending(buildImportSummaries.values());

		List<BuildIdentifier> importNeededBuilds = new LinkedList<>();
		for (BuildImportSummary buildImportSummary : buildsSortedByDateDescending) {
			if (buildImportSummary != null && buildImportSummary.getStatus().isImportNeeded()) {
				importNeededBuilds.add(buildImportSummary.getIdentifier());
				submitBuildForImport(availableBuilds, buildImportSummary.getIdentifier());
			}
		}

		executeAfterAllPendingImportsAndComparisonsDone(() -> LOGGER.info(ALL_BUILD_IMPORTS_AND_COMPARISONS_FINISHED_LOG_ENTRY_FOR_CI_CHECK));

		return importNeededBuilds.size();
	}

	private void executeAfterAllPendingImportsAndComparisonsDone(Runnable task) {
		executeAfterAllPendingImportsDone(() ->

			// submit another task for execution, to be sure that all scheduled comparison tasks,
			// which have been scheduled for all imported builds in the meantime, are submitted first
			asyncBuildImportExecutor.execute(() ->
				// let the task execute after all comparisons have been executed
				comparisonExecutor.executeAfterAllPendingComparisonsDone(task)
			)
		);
	}

	private void executeAfterAllPendingImportsDone(Runnable task) {
		asyncBuildImportExecutor.execute(task);
	}

	public synchronized void submitBuildForReimport(AvailableBuildsList availableBuilds,
													BuildIdentifier buildIdentifier) {
		removeImportedBuildAndDerivedData(availableBuilds, buildIdentifier);
		submitBuildForImport(availableBuilds, buildIdentifier);
		saveBuildImportSummaries(buildImportSummaries);
	}

	/**
	 * Schedule import of build (if new) and once it was imported also schedule a comparison to be calculated on that same build.
	 *
	 * @return buildDiffInfo as a double future ;-) - because waiting on a result from an asynchronous computation that triggers another asynchronous computation now ;-)
	 */
	public synchronized Future<Future<BuildDiffInfo>> importBuildIfNewAndScheduleHiPrioComparison(AvailableBuildsList availableBuilds,
																								  BuildIdentifier buildIdentifier,
																								  BuildIdentifier comparisonBuildIdentifier,
																								  String comparisonName) {
		BuildImportSummary buildImportSummary = buildImportSummaries.get(buildIdentifier);
		if (buildImportSummary == null) {
			LOGGER.info("Build not exists yet, submitting new import with additional task for hi prio comparison calculation.");
			buildImportSummary = createBuildImportSummary(buildIdentifier);
			buildImportSummaries.put(buildIdentifier, buildImportSummary);
			submitBuildForImport(availableBuilds, buildIdentifier);
			Future<Future<BuildDiffInfo>> futureResult = submitSingleBuildComparisonAfterLastImport(buildIdentifier, comparisonBuildIdentifier, comparisonName);
			saveBuildImportSummaries(buildImportSummaries);
			return futureResult;
		} else {
			LOGGER.info("Build already exists, only triggering comparison dirextly, not waiting for any other build import to finish first.");
			CompletableFuture<Future<BuildDiffInfo>> submittedFutureComparison = new CompletableFuture<>();
			submittedFutureComparison.complete(
				submitSingleBuildComparison(buildIdentifier, comparisonBuildIdentifier, comparisonName)
			);
			return submittedFutureComparison;
		}

	}

	public BuildImportStatus getBuildImportStatus(BuildIdentifier buildIdentifier) {
		BuildImportSummary buildImportSummary = buildImportSummaries.get(buildIdentifier);
		return buildImportSummary != null ? buildImportSummary.getStatus() : null;
	}

	/**
	 * @return <code>true</code> if no builds are being imported by asyncBuildImportExecutor
	 * and no comparisons are being calculated by comparisonExecutor.
	 */
	public boolean areAllImportsAndComparisonCalculationsFinished() {
		return asyncBuildImportExecutor.getActiveCount() == 0 && comparisonExecutor.areAllComparisonCalculationsFinished();
	}

	private BuildImportSummary createBuildImportSummary(BuildIdentifier buildIdentifier) {
		Build build = dao.loadBuild(buildIdentifier);
		return new BuildImportSummary(buildIdentifier.getBranchName(), build);
	}

	/**
	 * Remove a build from the available builds list and mark it as unprocessed,
	 * also remove any available derived data that mark this build as processed.
	 */
	private synchronized void removeImportedBuildAndDerivedData(AvailableBuildsList availableBuilds,
																BuildIdentifier buildIdentifier) {

		// Do not do anything when build is unknown or already queued for asynchronous processing
		BuildImportSummary summary = buildImportSummaries.get(buildIdentifier);
		if (summary == null || buildsInProcessingQueue.contains(buildIdentifier)) {
			return;
		}

		availableBuilds.removeBuild(buildIdentifier);
		summary.setStatus(BuildImportStatus.UNPROCESSED);
		summary.setStatusMessage(null);
		ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator(summary);
		aggregator.removeAggregatedDataForBuild();

	}

	/**
	 * Schedule a build to get imported.
	 * After import it will trigger comparisons to get scheduled for that build once all other pending comparisons are done.
	 */
	private synchronized void submitBuildForImport(final AvailableBuildsList availableBuilds,
												   BuildIdentifier buildIdentifier) {

		// Do not do anything when build is unknown or already queued
		final BuildImportSummary summary = buildImportSummaries.get(buildIdentifier);
		if (summary == null || buildsInProcessingQueue.contains(buildIdentifier)) {
			return;
		}

		LOGGER.info("Submitting build for import: " + buildIdentifier.getBranchName() + "/"
			+ buildIdentifier.getBuildName());
		buildsInProcessingQueue.add(buildIdentifier);
		summary.setStatus(BuildImportStatus.QUEUED_FOR_PROCESSING);

		asyncBuildImportExecutor.execute(() -> {
			try {
				importBuild(availableBuilds, summary);
				scheduleAllComparisonsForBuild(buildIdentifier);
			} catch (Throwable e) {
				LOGGER.error("Unexpected error on build import.", e);
			}
		});

	}

	private void scheduleAllComparisonsForBuild(BuildIdentifier buildIdentifier) {
		// The scheduling of comparisons is only done after all currently pending imports,
		// just to ensure that first all pending imports have been done before a comparison is calculated.
		// because comparisons could need import of other pending imports of builds
		// before they can run.
		asyncBuildImportExecutor.execute(() -> {
			try {
				comparisonExecutor.scheduleAllConfiguredComparisonsForOneBuild(buildIdentifier.getBranchName(), buildIdentifier.getBuildName());
			} catch (Throwable e) {
				LOGGER.error("Unexpected error on scheduling comparisons for a build.", e);
			}
		});
	}

	/**
	 * Submit a comparison to be immediately scheduled for asynch execution
	 * without waiting for any pending build imports to happen first.
	 */
	public Future<BuildDiffInfo> submitSingleBuildComparison(BuildIdentifier buildIdentifier, BuildIdentifier compareBuildIdentifier, String comparisonName) {
		return comparisonExecutor.scheduleComparison(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
			compareBuildIdentifier.getBranchName(), compareBuildIdentifier.getBuildName(), comparisonName);
	}

	/**
	 * Submit a comparison for execution but only schedule it for execution once the last scheduled build has been imported.
	 */
	private synchronized Future<Future<BuildDiffInfo>> submitSingleBuildComparisonAfterLastImport(BuildIdentifier buildIdentifier,
																					 BuildIdentifier comparisonBuildIdentifier,
																					 String comparisonName) {
		CompletableFuture<Future<BuildDiffInfo>> completableBuildDiffInfoFuture = new CompletableFuture<>();
		asyncBuildImportExecutor.execute(() -> {
			LOGGER.info("Scheduling comparison that was scheduled to happen after last build that was imported.");
			completableBuildDiffInfoFuture.complete(
				submitSingleBuildComparison(buildIdentifier, comparisonBuildIdentifier, comparisonName)
			);
		});
		return completableBuildDiffInfoFuture;
	}

	private void importBuild(AvailableBuildsList availableBuilds, BuildImportSummary summary) {

		ThreadLogAppender buildImportLog = null;

		try {


			File importLogFile = dao.getBuildImportLogFile(summary.getIdentifier());
			buildImportLog = ThreadLogAppender.createAndRegisterForLogs(summary.getIdentifier(), importLogFile);

			long startTime = System.currentTimeMillis();
			buildsBeeingImported.add(summary.getIdentifier());

			LOGGER.info("=== START OF BUILD IMPORT ===");
			LOGGER.info("Importing build: " + summary.getIdentifier().getBranchName() + "/"
				+ summary.getIdentifier().getBuildName());

			summary = buildImportSummaries.get(summary.getIdentifier());
			summary.setStatus(BuildImportStatus.PROCESSING);

			ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator(summary);
			if (!aggregator.isAggregatedDataForBuildAlreadyAvailableAndCurrentVersion()) {
				aggregator.calculateAggregatedDataForBuild();
				addSuccessfullyImportedBuild(availableBuilds, summary);
				lastSuccessfulScenarioBuild.updateLastSuccessfulScenarioBuild(summary, this, availableBuilds);
				LOGGER.info("SUCCESS on importing build: " + summary.getIdentifier().getBranchName() + "/"
					+ summary.getIdentifier().getBuildName());
			} else {
				addSuccessfullyImportedBuild(availableBuilds, summary);
				LOGGER.info("ADDED ALREADY IMPORTED build: " + summary.getIdentifier().getBranchName() + "/"
					+ summary.getIdentifier().getBuildName());
			}

			logDuration(startTime);
			LOGGER.info("=== END OF BUILD IMPORT (success) ===");
		} catch (Throwable e) {
			recordBuildImportFinished(summary, BuildImportStatus.FAILED, e.getMessage());
			LOGGER.error("FAILURE on importing build " + summary.getIdentifier().getBranchName() + "/"
				+ summary.getBuildDescription().getName(), e);
			LOGGER.info("=== END OF BUILD IMPORT (failed) ===");
		} finally {
			if (buildImportLog != null) {
				buildImportLog.unregisterAndFlush();
			}
		}
	}

	private void logDuration(long startTime) {
		long duration = (System.currentTimeMillis() - startTime) / 1000;
		long minutes = duration / 60;
		long seconds = duration % 60;
		LOGGER.info("Build Import finished in " + minutes + " min. " + seconds + " sec.");
	}

	private synchronized void addSuccessfullyImportedBuild(AvailableBuildsList availableBuilds,
														   BuildImportSummary buildImportSummary) {
		recordBuildImportFinished(buildImportSummary, BuildImportStatus.SUCCESS);
		availableBuilds.addImportedBuild(buildImportSummary);
	}

	private synchronized void recordBuildImportFinished(BuildImportSummary summary,
														BuildImportStatus buildStatus) {
		recordBuildImportFinished(summary, buildStatus, null);
	}

	private synchronized void recordBuildImportFinished(BuildImportSummary summary,
														BuildImportStatus buildStatus, String statusMessage) {
		summary = buildImportSummaries.get(summary.getIdentifier());
		summary.setStatus(buildStatus);
		summary.setStatusMessage(statusMessage);
		summary.setImportDate(new Date());
		buildsBeeingImported.remove(summary.getIdentifier());
		buildsInProcessingQueue.remove(summary.getIdentifier());
		saveBuildImportSummaries(buildImportSummaries);
	}

	private void saveBuildImportSummaries(Map<BuildIdentifier, BuildImportSummary> buildImportSummaries) {
		List<BuildImportSummary> summariesToSave = new ArrayList<>(
			buildImportSummaries.values());
		ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
			getConfigurationRepository().getDocumentationDataDirectory());
		dao.saveBuildImportSummaries(summariesToSave);
	}

	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread.
	 */
	private static ThreadPoolExecutor newAsyncBuildImportExecutor() {
		return new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	}

	public void shutdownAndAwaitTerminationOfRunningImportJob() throws InterruptedException {
		asyncBuildImportExecutor.shutdown();
		while (!asyncBuildImportExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
			LOGGER.info("Awaiting completion of build imports...");
		}
	}

	private ConfigurationRepository getConfigurationRepository() {
		return RepositoryLocator.INSTANCE.getConfigurationRepository();
	}

}
