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

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;

@Path("/rest/admin/")
public class AdminResource {
	
	@GET
	@Path("update")
	@Produces({ "application/xml", "application/json" })
	public void updateAllBuildsAndSubmitNewBuildsForImport() {
		ScenarioDocuBuildsManager.INSTANCE.updateAllBuildsAndSubmitNewBuildsForImport();
	}
	
	@GET
	@Path("buildImportStates")
	@Produces({ "application/xml", "application/json" })
	public List<BuildImportSummary> loadBuildImportSummaries() {
		return ScenarioDocuBuildsManager.INSTANCE.getBuildImportSummaries();
	}
	
	// @GET
	// @Path("buildImportLogs/{branchName}/{buildName}")
	// @Produces({ "application/xml", "application/json" })
	// public String loadBuildImportLog() {
	// ScenarioDocuBuildsManager.INSTANCE.updateAll();
	// }
	
}
