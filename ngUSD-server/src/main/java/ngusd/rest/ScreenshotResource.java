package ngusd.rest;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import ngusd.dao.UserScenarioDocuFilesystem;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/image/{imgName}")
public class ScreenshotResource {
	
	private final UserScenarioDocuFilesystem filesystem = new UserScenarioDocuFilesystem();
	
	@GET
	@Produces("image/jpeg")
	public Response getScreenshot(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("imgName") final String imgName) {
		File img = filesystem.loadScreenshot(branchName, buildName, usecaseName, scenarioName, imgName);
		if (img == null || !img.exists()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		ResponseBuilder response = Response.ok(img);
		response.header("Content-Disposition", "attachment; filename=\"" + imgName + "\"");
		return response.build();
	}
}
