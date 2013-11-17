package org.scenarioo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.dao.ConfigurationDAO;
import org.scenarioo.dao.ScenarioDocuAggregationDAO;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;


@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/")
public class ScenariosResource {
	
	private final ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath());
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public UseCaseScenarios readUseCaseScenarios(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName) {
		return dao.loadUseCaseScenarios(branchName, buildName, usecaseName);
	}
	
	/**
	 * Get a scenario with all its pages and steps
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{scenarioName}/")
	public ScenarioPageSteps readScenarioWithPagesAndSteps(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName) {
		return dao.loadScenarioPageSteps(branchName, buildName, usecaseName, scenarioName);
		
	}
}
