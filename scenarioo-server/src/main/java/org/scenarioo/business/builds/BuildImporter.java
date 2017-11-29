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
import org.scenarioo.model.diffViewer.ComparisonResult;
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
 */
public class BuildImporter {

	private static final Logger LOGGER = Logger.getLogger(BuildImporter.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	private ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
		configurationRepository.getDocumentationDataDirectory());

	/**
	 * Current state for all builds whether imported and aggregated correctly.
	 */
	private Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = new HashMap<BuildIdentifier, BuildImportSummary>();

	/**
	 * Builds that have been scheduled for processing (waiting for import)
	 */
	private final Set<BuildIdentifier> buildsInProcessingQueue = new HashSet<BuildIdentifier>();

	/**
	 * Builds currently beeing imported.
	 */
	private Set<BuildIdentifier> buildsBeeingImported = new HashSet<BuildIdentifier>();

	/**
	 * Executor to execute one import task after the other asynchronously.
	 */
	private final ExecutorService asyncBuildImportExecutor = newAsyncBuildImportExecutor();

	private ComparisonExecutor comparisonExecutor = new ComparisonExecutor(asyncBuildImportExecutor);

	private final LastSuccessfulScenariosBuild lastSuccessfulScenarioBuild = new LastSuccessfulScenariosBuild();

	public Map<BuildIdentifier, BuildImportSummary> getBuildImportSummaries() {
		return buildImportSummaries;
	}

	List<BuildImportSummary> getBuildImportSummariesAsList() {
		return new ArrayList<BuildImportSummary>(buildImportSummaries.values());
	}

	public ExecutorService getAsyncBuildImportExecutor() {
		return asyncBuildImportExecutor;
	}

	public synchronized void updateBuildImportStates(List<BranchBuilds> branchBuildsList,
													 Map<BuildIdentifier, BuildImportSummary> loadedBuildSummaries) {
		Map<BuildIdentifier, BuildImportSummary> result = new HashMap<BuildIdentifier, BuildImportSummary>();
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
	 * @return Returns the number of builds that were submitted (that need to be imported)
	 */
	public synchronized int submitUnprocessedBuildsForImport(AvailableBuildsList availableBuilds) {
		List<BuildImportSummary> buildsSortedByDateDescending = BuildByDateSorter
			.sortBuildsByDateDescending(buildImportSummaries.values());

		List<BuildIdentifier> importNeededBuilds = new LinkedList<BuildIdentifier>();
		for (BuildImportSummary buildImportSummary : buildsSortedByDateDescending) {
			if (buildImportSummary != null && buildImportSummary.getStatus().isImportNeeded()) {
				importNeededBuilds.add(buildImportSummary.getIdentifier());
				submitBuildForImport(availableBuilds, buildImportSummary.getIdentifier());
			}
		}

		for (BuildIdentifier buildIdentifier : importNeededBuilds) {
			submitBuildForComparison(buildIdentifier);
		}

		return importNeededBuilds.size();
	}

	public synchronized void submitBuildForReimport(AvailableBuildsList availableBuilds,
													BuildIdentifier buildIdentifier) {
		removeImportedBuildAndDerivedData(availableBuilds, buildIdentifier);
		submitBuildForImport(availableBuilds, buildIdentifier);
		submitBuildForComparison(buildIdentifier);
		saveBuildImportSummaries(buildImportSummaries);
	}

	public synchronized Future<ComparisonResult> importBuildAndCreateComparison(AvailableBuildsList availableBuilds,
			BuildIdentifier buildIdentifier, BuildIdentifier comparisonBuildIdentifier, String comparisonName) {

		submitBuildForInitialImportIfNewBuild(availableBuilds, buildIdentifier);

		Future<ComparisonResult> comparisonResult =
			submitBuildForSingleComparison(buildIdentifier, comparisonBuildIdentifier,comparisonName);

		saveBuildImportSummaries(buildImportSummaries);

		return comparisonResult;
	}

	public BuildImportStatus getBuildImportStatus(BuildIdentifier buildIdentifier) {
		BuildImportSummary buildImportSummary = buildImportSummaries.get(buildIdentifier);
		return buildImportSummary != null ? buildImportSummary.getStatus() : null;
	}

	private void submitBuildForInitialImportIfNewBuild(AvailableBuildsList availableBuilds, BuildIdentifier buildIdentifier) {
		BuildImportSummary buildImportSummary = buildImportSummaries.get(buildIdentifier);

		if(buildImportSummary == null) {
			buildImportSummary = createBuildImportSummary(buildIdentifier);
			buildImportSummaries.put(buildIdentifier, buildImportSummary);
			submitBuildForImport(availableBuilds, buildIdentifier);
		} else {
			LOGGER.info("Build already exists, not triggering import.");
		}
	}

	private BuildImportSummary createBuildImportSummary(BuildIdentifier buildIdentifier) {
		Build build = dao.loadBuild(buildIdentifier);
		return new BuildImportSummary(buildIdentifier.getBranchName(), build);
	}

	/**
	 * Remove a build from the available builds list and mark it as unprocessed, also remove any available derived data
	 * that mark this build as processed.
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
		ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator(summary);
		aggregator.removeAggregatedDataForBuild();
	}

	/**
	 * Submit any build for import.
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
		asyncBuildImportExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					importBuild(availableBuilds, summary);
				} catch (Throwable e) {
					LOGGER.error("Unexpected error on build import.", e);
				}
			}
		});
	}

	private void submitBuildForComparison(BuildIdentifier buildIdentifier) {
		comparisonExecutor.doComparison(buildIdentifier.getBranchName(), buildIdentifier.getBuildName());
	}

	private Future<ComparisonResult> submitBuildForSingleComparison(BuildIdentifier buildIdentifier, BuildIdentifier compareBuildIdentifier, String comparisonName) {
		return comparisonExecutor.doComparison(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
			compareBuildIdentifier.getBranchName(), compareBuildIdentifier.getBuildName(), comparisonName);
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
		List<BuildImportSummary> summariesToSave = new ArrayList<BuildImportSummary>(
			buildImportSummaries.values());
		ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory());
		dao.saveBuildImportSummaries(summariesToSave);
	}

	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread.
	 */
	private static ExecutorService newAsyncBuildImportExecutor() {
		return new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

}
