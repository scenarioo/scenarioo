package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import ngusd.dao.ScenarioDocuFilesystem;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/pages/{pageName}/{pageOccurenceInScenario}/steps/{stepIndex}")
public class ScreenshotResource {
	
	ScenarioDocuFilesystem filesystem = new ScenarioDocuFilesystem();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Response getScreenshot(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("pageName") final String pageName,
			@PathParam("pageOccurenceInScenario") final int pageOccurenceInScenario,
			@PathParam("stepIndex") final int stepIndex) {
		filesystem.getStep(branchName, buildName, usecaseName, scenarioName, pageName, stepIndex);
		return Response.ok().build();
		
	}
}
