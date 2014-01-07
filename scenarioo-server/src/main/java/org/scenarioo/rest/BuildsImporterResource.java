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

package org.scenarioo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;

@Path("/rest/builds/")
public class BuildsImporterResource {
	
	private static final Logger LOGGER = Logger.getLogger(BuildsImporterResource.class);
	
	@GET
	@Path("updateAndImport")
	@Produces({ "application/xml", "application/json" })
	public void updateAllBuildsAndSubmitNewBuildsForImport() {
		ScenarioDocuBuildsManager.INSTANCE.updateAllBuildsAndSubmitNewBuildsForImport();
	}
	
	@GET
	@Path("buildImportSummaries")
	@Produces({ "application/xml", "application/json" })
	public List<BuildImportSummary> listImportedBuilds() {
		LOGGER.info("REQUEST: listImportedBuilds()");
		return ScenarioDocuBuildsManager.INSTANCE.getBuildImportSummaries();
	}
	
	// TODO #81: add service to get additional larger error log message for a failed build
	// TODO #81: display it in UI (popup on FAILED-state)
	// @GET
	// @Path("buildImportLogs/{branchName}/{buildName}")
	// @Produces({ "application/xml", "application/json" })
	// public String loadBuildImportLog() {
	// ScenarioDocuBuildsManager.INSTANCE.updateAll();
	// }
	
}
