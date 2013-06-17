package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import ngusd.rest.model.Branch;

@Path("/rest/branches")
public class Branches {
	
	@GET
	public Branch listBranches() {
		return new Branch("trunk", "main development");
	}
}
