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
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * The configuration for the server and the client.
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data()
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
	
	private String applicationInformation;
	
	private Map<String, String> buildstates = new HashMap<String, String>();
	
}
