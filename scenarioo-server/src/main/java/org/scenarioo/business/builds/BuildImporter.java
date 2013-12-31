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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.scenarioo.business.aggregator.ScenarioDocuAggregator;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.derived.BuildLink;

/**
 * Takes care of importing new builds.
 */
public class BuildImporter {
	
	private static final Logger LOGGER = Logger.getLogger(BuildImporter.class);
	
	/**
	 * Current state for all builds whether imported and aggregated correctly.
	 */
	private Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = new HashMap<BuildIdentifier, BuildImportSummary>();
	
	/**
	 * Builds that have been scheduled for processing (waiting for import)
	 */
	private Set<BuildIdentifier> buildsInProcessingQueue = new HashSet<BuildIdentifier>();
	
	/**
	 * Executor to execute one import task after the other asynchronously.
	 */
	private ExecutorService asyncBuildImportExecutor = newAsyncBuildImportExecutor();
	
	public Map<BuildIdentifier, BuildImportSummary> getBuildImportSummaries() {
		return buildImportSummaries;
	}
	
	public List<BuildImportSummary> getBuildImportSummariesAsList() {
		return new ArrayList<BuildImportSummary>(buildImportSummaries.values());
	}
	
	public synchronized void updateBuildImportStates(
			final List<BranchBuilds> branchBuildsList,
			final Map<BuildIdentifier, BuildImportSummary> loadedBuildSummaries) {
		Map<BuildIdentifier, BuildImportSummary> result = new HashMap<BuildIdentifier, BuildImportSummary>();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			for (BuildLink buildLink : branchBuilds.getBuilds()) {
				ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator();
				// Take existent summary or create new one.
				BuildIdentifier buildIdentifier = new BuildIdentifier(branchBuilds.getBranch().getName(), buildLink
						.getBuild().getName());
				BuildImportSummary buildSummary = loadedBuildSummaries.get(buildIdentifier);
				if (buildSummary == null) {
					buildSummary = new BuildImportSummary(branchBuilds.getBranch().getName(), buildLink.getBuild());
				}
				aggregator.updateBuildSummary(buildSummary, buildLink);
				if (buildsInProcessingQueue.contains(buildIdentifier)) {
					buildSummary.setStatus(BuildImportStatus.QUEUED_FOR_PROCESSING);
				}
				result.put(buildIdentifier, buildSummary);
			}
		}
		saveBuildImportSummaries(result);
		buildImportSummaries = result;
	}
	
	public synchronized void submitUnprocessedBuildsForImport(final AvailableBuildsList availableBuilds) {
		for (BuildIdentifier buildIdentifier : buildImportSummaries.keySet()) {
			final BuildImportSummary summary = buildImportSummaries.get(buildIdentifier);
			if (summary != null) {
				if (summary.getStatus().isImportNeeded()
						&& !buildsInProcessingQueue.contains(buildIdentifier)) {
					LOGGER.info("   Submitting build for import: " + buildIdentifier.getBranchName() + "/"
							+ buildIdentifier.getBuildName());
					summary.setStatus(BuildImportStatus.QUEUED_FOR_PROCESSING);
					asyncBuildImportExecutor.execute(new Runnable() {
						@Override
						public void run() {
							importBuild(availableBuilds, summary);
						}
					});
				}
			}
		}
	}
	
	private void importBuild(final AvailableBuildsList availableBuilds, BuildImportSummary summary) {
		try {
			LOGGER.info(" ============= START OF ASYNC BUILD IMPORT =================");
			LOGGER.info("  Importing build: " + summary.getIdentifier().getBranchName() + "/"
					+ summary.getBuildDescription().getName());
			LOGGER.info("  This might take a while ...");
			
			summary = buildImportSummaries.get(summary.getIdentifier());
			summary.setStatus(BuildImportStatus.PROCESSING);
			
			ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator();
			if (!aggregator.containsAggregatedDataForBuild(summary.getIdentifier().getBranchName(),
					summary.getBuildDescription().getName())) {
				aggregator.calculateAggregatedDataForBuild(summary.getIdentifier().getBranchName(),
						summary.getBuildDescription().getName());
				addSuccessfullyImportedBuild(availableBuilds, summary);
				LOGGER.info("  SUCCESS on importing build: " + summary.getIdentifier().getBranchName() + "/"
						+ summary.getBuildDescription().getName());
			}
			else {
				addSuccessfullyImportedBuild(availableBuilds, summary);
				LOGGER.info("  ALREADY IMPORTED build: " + summary.getIdentifier().getBranchName() + "/"
						+ summary.getBuildDescription().getName());
			}
			LOGGER.info(" ============= END OF ASYNC BUILD IMPORT (success) ===========");
		} catch (Throwable e) {
			recordBuildImportFinished(summary, BuildImportStatus.FAILED);
			LOGGER.error("  FAILURE on importing build " + summary.getIdentifier().getBranchName() + "/"
					+ summary.getBuildDescription().getName(), e);
			LOGGER.info(" ============= END OF ASYNC BUILD IMPORT (failed) ===========");
		}
	}
	
	private synchronized void addSuccessfullyImportedBuild(final AvailableBuildsList availableBuilds,
			final BuildImportSummary summary) {
		recordBuildImportFinished(summary, BuildImportStatus.SUCCESS);
		availableBuilds.addImportedBuild(summary);
	}
	
	private synchronized void recordBuildImportFinished(BuildImportSummary summary,
			final BuildImportStatus buildStatus) {
		summary = buildImportSummaries.get(summary.getIdentifier());
		summary.setStatus(buildStatus);
		buildsInProcessingQueue.remove(summary.getIdentifier());
		saveBuildImportSummaries(buildImportSummaries);
	}
	
	private static void saveBuildImportSummaries(final Map<BuildIdentifier, BuildImportSummary> buildImportSummaries) {
		List<BuildImportSummary> summariesToSave = new ArrayList<BuildImportSummary>(buildImportSummaries.values());
		ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(
				ConfigurationDAO.getDocuDataDirectoryPath());
		dao.saveBuildImportSummaries(summariesToSave);
	}
	
	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread.
	 */
	private static ExecutorService newAsyncBuildImportExecutor() {
		return new ThreadPoolExecutor(
				1,
				1,
				60L,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}
	
}
