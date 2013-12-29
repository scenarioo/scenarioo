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

import java.io.File;
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
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.aggregator.ScenarioDocuAggregator;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.derived.BuildLink;
import org.scenarioo.model.docu.entities.Branch;

/**
 * Manages the list of branches and builds that are currently available in the documentation directory:
 * 
 * 1. Using {@link #updateAll()} all branches and builds are read and processed from the file system, this will
 * calculate any aggregated data using {@link ScenarioDocuAggregator}.
 * 
 * 2. The manager knows all available builds and their states of import, that can be accessed using
 * {@link #getBuildImportSummaries()}.
 * 
 * 3. The current (last processed) list of all successfully imported branches and builds is cached and can be accessed
 * using {@link #getBranchBuildsList()}.
 */
public class ScenarioDocuBuildsManager {
	
	public static ScenarioDocuBuildsManager INSTANCE = new ScenarioDocuBuildsManager();
	
	private static final Logger LOGGER = Logger.getLogger(ScenarioDocuBuildsManager.class);
	
	/**
	 * Current state for all builds whether imported and aggregated correctly.
	 */
	private Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = new HashMap<BuildIdentifier, BuildImportSummary>();
	
	/**
	 * Only the successfully imported builds that are available and can be accessed.
	 */
	private List<BranchBuilds> branchBuildsList = new ArrayList<BranchBuilds>();
	
	/**
	 * Builds that have been scheduled for processing (waiting for import)
	 */
	private Set<BuildIdentifier> buildsInProcessingQueue = new HashSet<BuildIdentifier>();
	
	private ExecutorService asyncBuildImportExecutor = newAsyncBuildImportExecutor();
	
	/**
	 * Is a singleton. Use {@link #INSTANCE}.
	 */
	private ScenarioDocuBuildsManager() {
	}
	
	/**
	 * Processes the content of configured documentation filesystem directory discovering newly added builds or branches
	 * to calculate all data for them. Also updates the branches and builds list.
	 * 
	 * This method should be called on server startup and whenever something on the filesystem changed.
	 * 
	 * The calculation/importing/aggregating of the builds is scheduled to be done in a separate thread/executor.
	 */
	public void updateAllBuildsAndSubmitNewBuildsForImport() {
		LOGGER.info("********************* update builds ********************************");
		LOGGER.info("Updating available builds ...");
		File docuDirectory = ConfigurationDAO.getDocuDataDirectoryPath();
		if (docuDirectory == null) {
			LOGGER.error("No documentation directory is configured.");
			LOGGER.error("Please configure valid documentation directory in configuration UI");
		} else if (!docuDirectory.exists()) {
			LOGGER.error("No valid documentation directory is configured: " + docuDirectory.getAbsolutePath());
			LOGGER.error("Please configure valid documentation directory in configuration UI");
		}
		else {
			LOGGER.info("  Processing documentation content data in directory: " + docuDirectory.getAbsoluteFile());
			updateBuildImportStatesAndAvailableBuildsList();
			submitUnprocessedBuildsForImport();
		}
		LOGGER.info("******************** update finished *******************************");
	}
	
	private synchronized void submitUnprocessedBuildsForImport() {
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
							importBuild(summary);
						}
					});
				}
			}
		}
	}
	
	private void importBuild(BuildImportSummary summary) {
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
				recordBuildImportFinished(summary, BuildImportStatus.SUCCESS);
				addImportedBuild(summary);
				LOGGER.info("  SUCCESS on importing build: " + summary.getIdentifier().getBranchName() + "/"
						+ summary.getBuildDescription().getName());
			}
			else {
				recordBuildImportFinished(summary, BuildImportStatus.SUCCESS);
				addImportedBuild(summary);
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
	
	private synchronized void recordBuildImportFinished(BuildImportSummary summary,
			final BuildImportStatus buildStatus) {
		summary = buildImportSummaries.get(summary.getIdentifier());
		summary.setStatus(buildStatus);
		buildsInProcessingQueue.remove(summary.getIdentifier());
		saveBuildImportSummaries(buildImportSummaries);
	}
	
	private synchronized void addImportedBuild(final BuildImportSummary summary) {
		for (BranchBuilds branchBuilds : branchBuildsList) {
			if (branchBuilds.getBranch().getName().equals(summary.getIdentifier().getBranchName())) {
				BuildLink buildLink = new BuildLink();
				buildLink.setLinkName(summary.getIdentifier().getBuildName());
				buildLink.setBuild(summary.getBuildDescription());
				branchBuilds.getBuilds().add(buildLink);
			}
			sortBuilds(branchBuilds);
		}
	}
	
	private synchronized void updateBuildImportStatesAndAvailableBuildsList() {
		LOGGER.info("Updating the list of available builds and their states ...");
		Map<BuildIdentifier, BuildImportSummary> loadedBuildImportSummaries = loadBuildImportSummaries();
		List<BranchBuilds> branchBuildsList = loadBranchBuildsList();
		buildImportSummaries = updateBuildImportStates(branchBuildsList, loadedBuildImportSummaries);
		this.branchBuildsList = updateBranchBuildsListWithSuccessfullyImportedBuilds(branchBuildsList);
		saveBuildImportSummaries(buildImportSummaries);
	}
	
	private static Map<BuildIdentifier, BuildImportSummary> loadBuildImportSummaries() {
		ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(
				ConfigurationDAO.getDocuDataDirectoryPath());
		List<BuildImportSummary> loadedSummaries = dao.loadBuildImportSummaries();
		Map<BuildIdentifier, BuildImportSummary> result = new HashMap<BuildIdentifier, BuildImportSummary>();
		for (BuildImportSummary buildImportSummary : loadedSummaries) {
			result.put(buildImportSummary.getIdentifier(), buildImportSummary);
		}
		return result;
	}
	
	private List<BranchBuilds> loadBranchBuildsList() {
		final ScenarioDocuReader reader = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
		List<BranchBuilds> result = new ArrayList<BranchBuilds>();
		List<Branch> branches = reader.loadBranches();
		for (Branch branch : branches) {
			BranchBuilds branchBuilds = new BranchBuilds();
			branchBuilds.setBranch(branch);
			branchBuilds.setBuilds(reader.loadBuilds(branch.getName()));
			result.add(branchBuilds);
		}
		return result;
	}
	
	private Map<BuildIdentifier, BuildImportSummary> updateBuildImportStates(
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
		return result;
	}
	
	private List<BranchBuilds> updateBranchBuildsListWithSuccessfullyImportedBuilds(
			final List<BranchBuilds> branchBuildsList) {
		List<BranchBuilds> result = new ArrayList<BranchBuilds>();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			BranchBuilds resultBranchBuilds = new BranchBuilds();
			resultBranchBuilds.setBranch(branchBuilds.getBranch());
			List<BuildLink> resultBuilds = new ArrayList<BuildLink>();
			resultBranchBuilds.setBuilds(resultBuilds);
			for (BuildLink buildLink : branchBuilds.getBuilds()) {
				BuildIdentifier buildIdentifier = new BuildIdentifier(branchBuilds.getBranch().getName(), buildLink
						.getBuild().getName());
				BuildImportSummary summary = buildImportSummaries.get(buildIdentifier);
				if (summary != null && summary.getStatus().isSuccess()) {
					resultBuilds.add(buildLink);
				}
			}
			sortBuilds(resultBranchBuilds);
			result.add(resultBranchBuilds);
		}
		return result;
	}
	
	private static void saveBuildImportSummaries(final Map<BuildIdentifier, BuildImportSummary> buildImportSummaries) {
		List<BuildImportSummary> summariesToSave = new ArrayList<BuildImportSummary>(buildImportSummaries.values());
		ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(
				ConfigurationDAO.getDocuDataDirectoryPath());
		dao.saveBuildImportSummaries(summariesToSave);
	}
	
	/**
	 * Get branch builds list with those builds that are currently available (have been already successfully imported).
	 */
	public List<BranchBuilds> getBranchBuildsList() {
		return branchBuildsList;
	}
	
	public List<BuildImportSummary> getBuildImportSummaries() {
		return new ArrayList<BuildImportSummary>(buildImportSummaries.values());
	}
	
	private void sortBuilds(final BranchBuilds branchBuilds) {
		BuildSorter.sort(branchBuilds.getBuilds());
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
