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

package org.scenarioo.model.configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The configuration for the server and the client.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {

	public static final String DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD = "most recent";
	public static final String DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD = "last successful";

	private String testDocumentationDirPath;

	private String defaultBranchName = "trunk";

	private String defaultBuildName = DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD;

	private String aliasForMostRecentBuild = DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD;

	private String aliasForLastSuccessfulBuild = DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD;

	private String buildStatusForSuccessfulBuilds = "success";

	private String scenarioPropertiesInOverview;

	private String applicationName = "";

	private String applicationInformation = "";

	private Map<String, String> buildstates = new HashMap<String, String>();
	
	private boolean expandPagesInScenarioOverview = false;

	private List<BranchAlias> branchAliases = new LinkedList<BranchAlias>();
	
	public String getTestDocumentationDirPath() {
		return testDocumentationDirPath;
	}

	public void setTestDocumentationDirPath(String testDocumentationDirPath) {
		this.testDocumentationDirPath = testDocumentationDirPath;
	}

	public String getDefaultBranchName() {
		return defaultBranchName;
	}

	public void setDefaultBranchName(String defaultBranchName) {
		this.defaultBranchName = defaultBranchName;
	}

	public String getDefaultBuildName() {
		return defaultBuildName;
	}

	public void setDefaultBuildName(String defaultBuildName) {
		this.defaultBuildName = defaultBuildName;
	}

	public String getAliasForMostRecentBuild() {
		return aliasForMostRecentBuild;
	}

	public void setAliasForMostRecentBuild(String aliasForMostRecentBuild) {
		this.aliasForMostRecentBuild = aliasForMostRecentBuild;
	}

	public String getAliasForLastSuccessfulBuild() {
		return aliasForLastSuccessfulBuild;
	}

	public void setAliasForLastSuccessfulBuild(
			String aliasForLastSuccessfulBuild) {
		this.aliasForLastSuccessfulBuild = aliasForLastSuccessfulBuild;
	}

	public String getBuildStatusForSuccessfulBuilds() {
		return buildStatusForSuccessfulBuilds;
	}

	public void setBuildStatusForSuccessfulBuilds(
			String buildStatusForSuccessfulBuilds) {
		this.buildStatusForSuccessfulBuilds = buildStatusForSuccessfulBuilds;
	}

	public String getScenarioPropertiesInOverview() {
		return scenarioPropertiesInOverview;
	}

	public void setScenarioPropertiesInOverview(
			String scenarioPropertiesInOverview) {
		this.scenarioPropertiesInOverview = scenarioPropertiesInOverview;
	}

	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * The name of the application that is documented by this Scenarioo
	 * installation. It's displayed in the browser's title bar and in the
	 * navigation bar of Scenarioo.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationInformation() {
		return applicationInformation;
	}

	public void setApplicationInformation(String applicationInformation) {
		this.applicationInformation = applicationInformation;
	}

	public Map<String, String> getBuildstates() {
		return buildstates;
	}

	public void setBuildstates(Map<String, String> buildstates) {
		this.buildstates = buildstates;
	}

	public boolean isExpandPagesInScenarioOverview() {
		return expandPagesInScenarioOverview;
	}
	
	public void setExpandPagesInScenarioOverview(final boolean expandPagesInScenarioOverview) {
		this.expandPagesInScenarioOverview = expandPagesInScenarioOverview;
	}

	public List<BranchAlias> getBranchAliases() {
		if(branchAliases == null) {
			branchAliases = new LinkedList<BranchAlias>();
		}
		return branchAliases;
	}

	public void setBranchAliases(List<BranchAlias> buildAliases) {
		this.branchAliases = buildAliases;
	}

}
