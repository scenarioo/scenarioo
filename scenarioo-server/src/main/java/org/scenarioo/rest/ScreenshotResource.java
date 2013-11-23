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
import org.scenarioo.dao.configuration.ConfigurationDAO;


@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/image/{imgName}")
public class ScreenshotResource {
	
	private final ScenarioDocuReader filesystem = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
	
	@GET
	@Produces("image/jpeg")
	public Response getScreenshot(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("imgName") final String imgName) {
		File img = filesystem.getScreenshotFile(branchName, buildName, usecaseName, scenarioName, imgName);
		if (img == null || !img.exists()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		ResponseBuilder response = Response.ok(img);
		response.header("Content-Disposition", "attachment; filename=\"" + imgName + "\"");
		return response.build();
	}
}
