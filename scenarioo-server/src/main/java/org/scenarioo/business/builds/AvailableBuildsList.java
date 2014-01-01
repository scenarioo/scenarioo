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
import java.util.List;
import java.util.Map;

import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.derived.BuildLink;

/**
 * Manages all the currently available builds and maintains aliases to the most recent builds for each branch.
 * 
 * This list only contains those builds that have been successfully imported and are currently accessible.
 */
public class AvailableBuildsList {
	
	/**
	 * Only the successfully imported builds that are available and can be accessed.
	 */
	private List<BranchBuilds> branchBuildsList = new ArrayList<BranchBuilds>();
	
	/**
	 * The branch builds grouped by brnach name, only containing the available builds that have already been imported
	 * successfully.
	 */
	private Map<String, BranchBuilds> branchBuildsByBranchName = new HashMap<String, BranchBuilds>();
	
	/**
	 * Special aliases to resolve when accessing a build.
	 */
	private Map<BuildIdentifier, BuildLink> buildAliases = new HashMap<BuildIdentifier, BuildLink>();
	
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
	public synchronized void updateBuildsWithSuccessfullyImportedBuilds(
			final List<BranchBuilds> branchBuildsList,
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
	}
	
	/**
	 * @param result
	 */
	private synchronized void updateBuilds(final List<BranchBuilds> result) {
		this.branchBuildsList = result;
		branchBuildsByBranchName.clear();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			branchBuildsByBranchName.put(branchBuilds.getBranch().getName(), branchBuilds);
		}
	}
	
	/**
	 * Adding a newly imported build.
	 */
	public synchronized void addImportedBuild(final BuildImportSummary summary) {
		for (BranchBuilds branchBuilds : branchBuildsList) {
			if (branchBuilds.getBranch().getName().equals(summary.getIdentifier().getBranchName())) {
				BuildLink buildLink = new BuildLink();
				buildLink.setLinkName(summary.getIdentifier().getBuildName());
				buildLink.setBuild(summary.getBuildDescription());
				branchBuilds.getBuilds().add(buildLink);
				updateAliasesForRecentBuilds(branchBuilds);
				BuildSorter.sort(branchBuilds.getBuilds());
				return;
			}
		}
	}
	
	/**
	 * Find the most recent builds (both successful and unsuccessful) and tag them with a special alias names
	 */
	private void updateAliasesForRecentBuilds(final BranchBuilds branchBuilds) {
		
		// Search for the builds
		String buildSuccessState = ConfigurationDAO.getConfiguration().getBuildStatusForSuccessfulBuilds();
		String lastRecentBuildAlias = ConfigurationDAO.getConfiguration().getAliasForLastRecentBuild();
		String lastSuccessfulBuildAlias = ConfigurationDAO.getConfiguration().getAliasForLastSuccessfulBuild();
		BuildLink aliasForLastSuccessfulBuild = null;
		BuildLink aliasForLastRecentBuild = null;
		BuildLink lastSuccessfulBuild = null;
		BuildLink lastRecentBuild = null;
		for (BuildLink build : branchBuilds.getBuilds()) {
			if (build.getLinkName().equals(lastRecentBuildAlias)) {
				aliasForLastRecentBuild = build;
			}
			else if (build.getLinkName().equals(lastSuccessfulBuildAlias)) {
				aliasForLastSuccessfulBuild = build;
			}
			else {
				if (isMoreRecentThan(build, lastRecentBuild)) {
					lastRecentBuild = build;
				}
				if (build.getBuild().getStatus().equals(buildSuccessState)
						&& isMoreRecentThan(build, lastSuccessfulBuild)) {
					lastSuccessfulBuild = build;
				}
			}
		}
		
		// Update aliases
		updateOrAddBuildAlias(lastRecentBuild, lastRecentBuildAlias, aliasForLastRecentBuild, branchBuilds);
		updateOrAddBuildAlias(lastSuccessfulBuild, lastSuccessfulBuildAlias, aliasForLastSuccessfulBuild,
				branchBuilds);
	}
	
	private void updateOrAddBuildAlias(final BuildLink buildLinkForAlias, final String aliasName,
			final BuildLink existingAlias, final BranchBuilds branchBuilds) {
		if (buildLinkForAlias != null) {
			if (existingAlias == null) {
				branchBuilds.getBuilds().add(new BuildLink(buildLinkForAlias.getBuild(), aliasName));
			}
			else {
				existingAlias.setBuild(buildLinkForAlias.getBuild());
			}
			buildAliases.put(new BuildIdentifier(branchBuilds.getBranch().getName(), aliasName), buildLinkForAlias);
		}
		else {
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
			// compare name if date is not available (assuming somehow the name of the build to reflect the
			// age/date/revision)
			return buildToCompareWith.getBuild().getName().compareTo(build.getBuild().getName()) < 0;
		}
		else if (build.getBuild().getDate() == null) {
			// build without date is never more recent than one with
			return false;
		}
		else {
			// compare dates
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
		}
		else {
			return buildName;
		}
	}
	
}
