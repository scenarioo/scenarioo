package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.dao.UserScenarioDocuContentDAO;
import ngusd.model.docu.aggregates.usecases.PageVariantsCounter;

@Path("/rest/branches/{branchName}/builds/{buildName}/search/")
public class SearchResource {
	
	UserScenarioDocuContentDAO dao = new UserScenarioDocuContentDAO();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/pagevariants")
	public PageVariantsCounter readUseCaseScenarios(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName) {
		return dao.loadPageVariantsCounter(branchName, buildName);
	}
	
}
