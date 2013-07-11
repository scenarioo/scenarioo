package ngusd.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ngusd.manager.UserScenarioDocuManager;
import ngusd.model.docu.aggregates.branches.BranchBuilds;

@Path("/rest/branches/")
public class BranchesResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<BranchBuilds> listBranchesAndBuilds() {
		return UserScenarioDocuManager.INSTANCE.getBranchBuildsList();
	}
	
}
