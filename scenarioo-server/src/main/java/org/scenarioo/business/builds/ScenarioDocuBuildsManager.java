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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.aggregator.ScenarioDocuAggregator;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
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
 * using {@link #getAvailableBuilds()}.
 */
public class ScenarioDocuBuildsManager {
	
	public static ScenarioDocuBuildsManager INSTANCE = new ScenarioDocuBuildsManager();
	
	private static final Logger LOGGER = Logger.getLogger(ScenarioDocuBuildsManager.class);
	
	/**
	 * Only the successfully imported builds that are available and can be accessed.
	 */
	private AvailableBuildsList availableBuilds = new AvailableBuildsList();
	
	/**
	 * Importer to hold current state of all builds and to import those that are not yet imported or are outdated.
	 */
	private BuildImporter buildImporter = new BuildImporter();
	
	/**
	 * Is a singleton. Use {@link #INSTANCE}.
	 */
	private ScenarioDocuBuildsManager() {
	}
	
	/**
	 * Get branch builds list with those builds that are currently available (have been already successfully imported).
	 */
	public List<BranchBuilds> getAvailableBuilds() {
		return availableBuilds.getBranchBuildsList();
	}
	
	/**
	 * Summaries about current states of all builds.
	 */
	public List<BuildImportSummary> getBuildImportSummaries() {
		return buildImporter.getBuildImportSummariesAsList();
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
			buildImporter.submitUnprocessedBuildsForImport(availableBuilds);
		}
		LOGGER.info("******************** update finished *******************************");
	}
	
	private synchronized void updateBuildImportStatesAndAvailableBuildsList() {
		LOGGER.info("Updating the list of available builds and their states ...");
		Map<BuildIdentifier, BuildImportSummary> loadedBuildImportSummaries = loadBuildImportSummaries();
		List<BranchBuilds> branchBuildsList = loadBranchBuildsList();
		buildImporter.updateBuildImportStates(branchBuildsList, loadedBuildImportSummaries);
		availableBuilds.updateBuildsWithSuccessfullyImportedBuilds(branchBuildsList,
				buildImporter.getBuildImportSummaries());
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
	
}
