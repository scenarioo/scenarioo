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

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.configuration.ConfigurationDAO;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/image/{imgName}")
public class ScreenshotResource {
	
	private final ScenarioDocuReader filesystem = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
	
	@GET
	@Produces("image/jpeg")
	public Response getScreenshot(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("imgName") final String imgName) {
		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBranchName(branchName);
		String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBuildName(resolvedBranchName, buildName);
		File img = filesystem.getScreenshotFile(resolvedBranchName, resolvedBuildName, usecaseName, scenarioName, imgName);
		if (img == null || !img.exists()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		ResponseBuilder response = Response.ok(img);
		response.header("Content-Disposition", "attachment; filename=\"" + imgName + "\"");
		return response.build();
	}
}
