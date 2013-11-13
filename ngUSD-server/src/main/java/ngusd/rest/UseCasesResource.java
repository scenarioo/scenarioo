package ngusd.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.dao.ConfigurationDAO;
import ngusd.dao.ScenarioDocuAggregationDAO;
import ngusd.model.docu.aggregates.usecases.UseCaseScenarios;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/")
public class UseCasesResource {
	
	ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(ConfigurationDAO.getDocuDataDirectoryPath());
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<UseCaseScenarios> loadUseCaseScenariosList(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName) {
		return dao.loadUseCaseScenariosList(branchName, buildName);
	}
	
}
