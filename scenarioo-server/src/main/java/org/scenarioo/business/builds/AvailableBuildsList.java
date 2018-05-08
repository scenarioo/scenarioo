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
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.lastSuccessfulScenarios.LastSuccessfulScenariosBuildUpdater;
import org.scenarioo.model.configuration.BranchAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.Branch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

/**
 * Manages all the currently available builds and maintains aliases to the most recent builds for each branch.
 *
 * This list only contains those builds that have been successfully imported and are currently accessible.
 */
public class AvailableBuildsList {

	private static final Logger LOGGER = Logger.getLogger(AvailableBuildsList.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	/**
	 * Only the successfully imported builds that are available and can be accessed.
	 */
	private List<BranchBuilds> branchBuildsList = new ArrayList<BranchBuilds>();

	/**
	 * The branch builds grouped by branch name, only containing the available builds that have already been imported
	 * successfully.
	 */
	private final Map<String, BranchBuilds> branchBuildsByBranchName = new HashMap<String, BranchBuilds>();

	/**
	 * Special aliases to resolve when accessing a build.
	 */
	private final Map<BuildIdentifier, BuildLink> buildAliases = new HashMap<BuildIdentifier, BuildLink>();

	/**
	 * Get branch builds list with those builds that are currently available (have been already successfully imported).
	 */
	public synchronized List<BranchBuilds> getBranchBuildsList() {
		return branchBuildsList;
	}

	/**
	 * Get the builds for a branch with a given name.
	 */
	public synchronized BranchBuilds getBranchBuilds(final String branchName) {
		return branchBuildsByBranchName.get(branchName);
	}

	/**
	 * Update the list of successfully imported builds from passed list of build import summary states of all builds.
	 */
	public synchronized void updateBuildsWithSuccessfullyImportedBuilds(final List<BranchBuilds> branchBuildsList,
			final Map<BuildIdentifier, BuildImportSummary> buildImportSummaries) {
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
			updateAliasesForRecentBuilds(resultBranchBuilds);
			BuildSorter.sort(resultBranchBuilds.getBuilds());
			result.add(resultBranchBuilds);
		}
		updateBuilds(result);
		logAvailableBuildsInformation();
	}

	private synchronized void updateBuilds(final List<BranchBuilds> result) {
		this.branchBuildsList = result;

		refreshAliases();

		branchBuildsByBranchName.clear();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			branchBuildsByBranchName.put(branchBuilds.getBranch().getName(), branchBuilds);
		}
	}

	public synchronized void refreshAliases() {
		List<BranchBuilds> physicalBuilds = getBranchBuildsWithoutAliases();
		List<BranchBuilds> aliasBuilds = createBranchesFromAliases(physicalBuilds);
		List<BranchBuilds> allBranches = new LinkedList<BranchBuilds>();

		allBranches.addAll(physicalBuilds);
		allBranches.addAll(aliasBuilds);

		this.branchBuildsList = allBranches;
	}

	private List<BranchBuilds> getBranchBuildsWithoutAliases() {
		List<BranchBuilds> result = new LinkedList<BranchBuilds>();
		for (BranchBuilds branchBuilds : this.branchBuildsList) {
			if (!branchBuilds.isAlias()) {
				result.add(branchBuilds);
			}
		}
		return result;
	}

	private List<BranchBuilds> createBranchesFromAliases(final List<BranchBuilds> physicalBuilds) {
		List<BranchBuilds> result = new LinkedList<BranchBuilds>();

		Configuration configuration = configurationRepository.getConfiguration();
		List<BranchAlias> branchAliases = configuration.getBranchAliases();
		for (BranchAlias branchAlias : branchAliases) {
			BranchBuilds branchWithBuilds = findBranch(branchAlias.getReferencedBranch());
			if (branchWithBuilds != null) {
				// ignore aliases who's referenced branch can not be found, this should not let the server crash on
				// startup or on updationg builds, could be an old branch not available anymore.
				BranchBuilds branchBuildsAlias = new BranchBuilds();
				branchBuildsAlias.setBuilds(branchWithBuilds.getBuilds());
				Branch branch = new Branch(branchAlias.getName(), branchWithBuilds.getBranch().getName());
				branchBuildsAlias.setBranch(branch);
				branchBuildsAlias.setAlias(true);
				result.add(branchBuildsAlias);
			}
		}
		return result;
	}

	private BranchBuilds findBranch(final String branchName) {
		for (BranchBuilds branchBuilds : this.branchBuildsList) {
			if (!branchBuilds.isAlias() && branchBuilds.getBranch().getName().equals(branchName)) {
				return branchBuilds;
			}
		}
		return null;
	}

	/**
	 * Adding a newly imported build.
	 */
	public synchronized void addImportedBuild(final BuildImportSummary buildImportSummary) {
		// Add to existing BranchBuilds if it exists for the branch
		for (BranchBuilds branchBuilds : branchBuildsList) {
			if (branchBuilds.getBranch().getName().equals(buildImportSummary.getIdentifier().getBranchName())) {
				addImportedBuild(buildImportSummary, branchBuilds);
				return;
			}
		}

		// BranchBuilds does not exist yet for the given branch, so it has to be added
		BranchBuilds newBranchBuild = addNewBranchToBranchBuilds(buildImportSummary);
		addImportedBuild(buildImportSummary, newBranchBuild);
	}

	private BranchBuilds addNewBranchToBranchBuilds(BuildImportSummary buildImportSummary) {
		File documentationDataDirectory = configurationRepository.getDocumentationDataDirectory();
		final ScenarioDocuReader reader = new ScenarioDocuReader(documentationDataDirectory);
		Branch branch = reader.loadBranch(buildImportSummary.getIdentifier().getBranchName());
		BranchBuilds branchBuilds = new BranchBuilds();
		branchBuilds.setBranch(branch);
		branchBuilds.setAlias(isBranchAlias(branch.getName()));
		return branchBuilds;
	}

	private boolean isBranchAlias(String branchName) {
		return configurationRepository.getConfiguration().getBranchAliases().stream()
			.anyMatch(new Predicate<BranchAlias>() {
				@Override
				public boolean test(BranchAlias branchAlias) {
					return branchAlias.getName().equals(branchName);
				}
			});
	}

	private void addImportedBuild(BuildImportSummary buildImportSummary, BranchBuilds branchBuilds) {
		BuildLink buildLink = new BuildLink();
		buildLink.setLinkName(buildImportSummary.getIdentifier().getBuildName());
		buildLink.setBuild(buildImportSummary.getBuildDescription());
		setSpecialDisplayNameForLastSuccessfulScenariosBuild(buildLink);
		branchBuilds.getBuilds().add(buildLink);
		updateAliasesForRecentBuilds(branchBuilds);
		BuildSorter.sort(branchBuilds.getBuilds());
	}

	// TODO [#248] Duplicate method in ScenarioDocuAggregationDAO
	private void setSpecialDisplayNameForLastSuccessfulScenariosBuild(final BuildLink link) {
		if (LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME
				.equals(link.getBuild().getName())) {
			link.setDisplayName(LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_DISPLAY_NAME);
		}
	}

	/**
	 * Remove a build from available builds (e.g. because it is currently processed).
	 *
	 * If build is not found, nothing happens (no effect and no exception)
	 */
	public void removeBuild(final BuildIdentifier buildIdentifier) {
		for (BranchBuilds branchBuilds : branchBuildsList) {
			if (branchBuilds.getBranch().getName().equals(buildIdentifier.getBranchName())) {
				for (BuildLink buildLink : branchBuilds.getBuilds()) {
					if (buildLink.getLinkName().equals(buildIdentifier.getBuildName())) {
						branchBuilds.getBuilds().remove(buildLink);
						break;
					}
				}
				updateAliasesForRecentBuilds(branchBuilds);
				BuildSorter.sort(branchBuilds.getBuilds());
				return;
			}
		}
	}

	/**
	 * Find the most recent builds (both successful and unsuccessful) and tag them with special alias names
	 */
	private void updateAliasesForRecentBuilds(final BranchBuilds branchBuilds) {

		// Search for the builds
		String buildSuccessState = configurationRepository.getConfiguration().getBuildStatusForSuccessfulBuilds();
		String aliasForMostRecentBuild = configurationRepository.getConfiguration().getAliasForMostRecentBuild();
		String aliasForLastSuccessfulBuild = configurationRepository.getConfiguration()
				.getAliasForLastSuccessfulBuild();
		BuildLink aliasLinkForLastSuccessfulBuild = null;
		BuildLink aliasLinkForMostRecentBuild = null;
		BuildLink lastSuccessfulBuild = null;
		BuildLink mostRecentBuild = null;

		for (BuildLink build : branchBuilds.getBuilds()) {
			if (build.getLinkName().equals(aliasForMostRecentBuild)) {
				aliasLinkForMostRecentBuild = build;
			} else if (build.getLinkName().equals(aliasForLastSuccessfulBuild)) {
				aliasLinkForLastSuccessfulBuild = build;
			} else if (build.getLinkName().equals(
					LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME)) {
				// Do nothing. This build is not used to find the last successful and most recent build.
			} else {
				if (isMoreRecentThan(build, mostRecentBuild)) {
					mostRecentBuild = build;
				}
				if (buildSuccessState.equals(build.getBuild().getStatus())
						&& isMoreRecentThan(build, lastSuccessfulBuild)) {
					lastSuccessfulBuild = build;
				}
			}
		}

		// Update aliases
		updateOrAddBuildAlias(mostRecentBuild, aliasForMostRecentBuild, aliasLinkForMostRecentBuild, branchBuilds);
		updateOrAddBuildAlias(lastSuccessfulBuild, aliasForLastSuccessfulBuild, aliasLinkForLastSuccessfulBuild,
				branchBuilds);
	}

	private void updateOrAddBuildAlias(final BuildLink buildLinkForAlias, final String aliasName,
			final BuildLink existingAlias, final BranchBuilds branchBuilds) {
		if (buildLinkForAlias != null) {
			if (existingAlias == null) {
				branchBuilds.getBuilds().add(new BuildLink(buildLinkForAlias.getBuild(), aliasName));
			} else {
				existingAlias.setBuild(buildLinkForAlias.getBuild());
			}
			buildAliases.put(new BuildIdentifier(branchBuilds.getBranch().getName(), aliasName), buildLinkForAlias);
		} else {
			buildAliases.remove(new BuildIdentifier(branchBuilds.getBranch().getName(), aliasName));
		}
	}

	private boolean isMoreRecentThan(final BuildLink build, final BuildLink buildToCompareWith) {
		if (buildToCompareWith == null) {
			// compared with nothing it is more recent
			return true;
		}
		if (buildToCompareWith.getBuild().getDate() == null) {
			if (build.getBuild().getDate() != null) {
				// buld with date is always more recent than one without
				return true;
			}
			// compareAndWrite name if date is not available (assuming somehow the name of the build to reflect the
			// age/date/revision)
			return buildToCompareWith.getBuild().getName().compareTo(build.getBuild().getName()) < 0;
		} else if (build.getBuild().getDate() == null) {
			// build without date is never more recent than one with
			return false;
		} else {
			// compareAndWrite dates
			return buildToCompareWith.getBuild().getDate().compareTo(build.getBuild().getDate()) < 0;
		}
	}

	/**
	 * Resolves possible alias names in 'buildName' for managed build alias links.
	 *
	 * @return the name to use as build name for referencing this build.
	 */
	public String resolveAliasBuildName(final String branchName, final String buildName) {
		BuildIdentifier id = new BuildIdentifier(branchName, buildName);
		BuildLink alias = buildAliases.get(id);
		if (alias != null) {
			return alias.getLinkName();
		} else {
			return buildName;
		}
	}

	/**
	 * Logs some information about currently available builds in the log file.
	 */
	public void logAvailableBuildsInformation() {
		LOGGER.info("Number of available branches: " + branchBuildsList.size());
		if (branchBuildsList.size() == 0) {
			LOGGER.warn("No branches found, please check the configured documentation directory and whether each of the contained branch directories contains a branch.xml to describe the branch properly.");
		}
		for (BranchBuilds branchBuilds : branchBuildsList) {
			int numberOfBuilds = 0;
			int numberOfBuildAliases = 0;
			for (BuildLink link : branchBuilds.getBuilds()) {
				if (link.getLinkName().equals(link.getBuild().getName())) {
					numberOfBuilds++;
				} else {
					numberOfBuildAliases++;
				}
			}
			LOGGER.info("Branch '" + branchBuilds.getBranch().getName() + "' contains " + numberOfBuilds
					+ " usual builds and " + numberOfBuildAliases + " build aliases");
		}
	}

}
