package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.dao.UserScenarioDocuContentDAO;
import ngusd.model.docu.aggregates.usecases.PageVariantsMap;

@Path("/rest/branches/{branchName}/builds/{buildName}/pagevariants/")
public class PageVariantsResource {
	
	UserScenarioDocuContentDAO dao = new UserScenarioDocuContentDAO();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public PageVariantsMap loadPageVariants(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName) {
		return dao.loadPageVariants(branchName, buildName);
	}
}