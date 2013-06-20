package ngusd.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.dao.ScenarioDocuFilesystem;
import ngusd.docu.model.UseCase;
import ngusd.rest.model.UseCaseScenarios;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/")
public class UseCasesResource {
	
	ScenarioDocuFilesystem filesystem = new ScenarioDocuFilesystem();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<UseCaseScenarios> listUseCasesAndScenarios(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName) {
		List<UseCaseScenarios> result = new ArrayList<UseCaseScenarios>();
		List<UseCase> usecases = filesystem.getUsecases(branchName, buildName);
		for (UseCase usecase : usecases) {
			UseCaseScenarios item = new UseCaseScenarios();
			item.setUseCase(usecase);
			item.setScenarios(filesystem.getScenarios(branchName, buildName, usecase.getName()));
			result.add(item);
		}
		return result;
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{usecaseName}/scenarios")
	public UseCaseScenarios listScenarios(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName) {
		UseCase usecase = filesystem.getUsecase(branchName, buildName, usecaseName);
		UseCaseScenarios item = new UseCaseScenarios();
		item.setUseCase(usecase);
		item.setScenarios(filesystem.getScenarios(branchName, buildName, usecase.getName()));
		return item;
	}
	
}
