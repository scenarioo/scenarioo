package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.dao.filesystem.UserScenarioDocuFilesystem;
import ngusd.model.docu.entities.Step;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/steps/")
public class StepResource {
	
	private final UserScenarioDocuFilesystem filesystem = new UserScenarioDocuFilesystem();
	
	/**
	 * Get a step with all its data (meta data, html, ..)
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{stepIndex}/")
	public Step readScenarioWithPagesAndSteps(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("stepIndex") final int stepIndex) {
		return filesystem.loadStep(branchName, buildName, usecaseName, scenarioName, stepIndex);
		
	}
	
}