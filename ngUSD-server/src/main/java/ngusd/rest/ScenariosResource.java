package ngusd.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.dao.ScenarioDocuFilesystem;
import ngusd.docu.model.UseCase;
import ngusd.rest.model.PageSteps;
import ngusd.rest.model.ScenarioPageSteps;
import ngusd.rest.model.UseCaseScenarios;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/")
public class ScenariosResource {
	
	ScenarioDocuFilesystem filesystem = new ScenarioDocuFilesystem();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public UseCaseScenarios readUseCaseScenarios(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName) {
		UseCase usecase = filesystem.getUsecase(branchName, buildName, usecaseName);
		UseCaseScenarios item = new UseCaseScenarios();
		item.setUseCase(usecase);
		item.setScenarios(filesystem.getScenarios(branchName, buildName, usecase.getName()));
		return item;
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
		
		ScenarioPageSteps result = new ScenarioPageSteps();
		
		result.setUseCase(filesystem.getUsecase(branchName, buildName, usecaseName));
		result.setScenario(filesystem.getScenario(branchName, buildName, usecaseName, scenarioName));
		
		List<PageSteps> pageSteps = filesystem.readPageSteps(branchName,
				buildName, usecaseName,
				scenarioName);
		result.setPagesAndSteps(pageSteps);
		return result;
	}
}
