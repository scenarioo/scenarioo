package ngusd.rest;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.rest.model.Branch;
import ngusd.rest.model.DummyDataProvider;

@Path("/rest/branches")
public class BranchesResource {
	private final DummyDataProvider dataProvider = DummyDataProvider.getInstance();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Collection<Branch> listBranches() {
		return dataProvider.getBranches().values();
	}
	
	@GET
	@Path("{branchId}")
	@Produces({ "application/xml", "application/json" })
	public Branch getBranch(@PathParam("branchId") final String branchId) {
		return dataProvider.getBranch(branchId);
	}
}
