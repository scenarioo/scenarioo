package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ngusd.rest.model.Branch;

@Path("/rest/branches")
public class BranchesResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Branch listBranches() {
		return new Branch("trunk", "main development");
	}
	
}
