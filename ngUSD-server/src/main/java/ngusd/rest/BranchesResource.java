package ngusd.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.rest.model.Branch;
import ngusd.rest.model.Build;

@Path("/rest/branches/")
public class BranchesResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Collection<Branch> listBranches() {
		Branch trunk = new Branch("trunk", "main development");
		Build build1 = new Build("build1");
		build1.setDate(new Date());
		build1.setRevision("rev:42");
		build1.setState("successful");
		trunk.addBuild(build1);
		
		Build build2 = new Build("build2");
		build2.setDate(new Date());
		build2.setRevision("rev:56");
		build2.setState("failed");
		trunk.addBuild(build2);
		
		Branch maiBranch = new Branch("mai", "");
		Build build3 = new Build("build3");
		build3.setDate(new Date());
		build3.setRevision("rev:22");
		build3.setState("successful");
		maiBranch.addBuild(build3);
		
		Build build4 = new Build("build4");
		build4.setDate(new Date());
		build4.setRevision("rev:12");
		build4.setState("warning");
		maiBranch.addBuild(build4);
		
		return Arrays.asList(trunk, maiBranch);
	}
	
	@GET
	@Path("{branchId}")
	@Produces({ "application/xml", "application/json" })
	public Branch getBranch(@PathParam("branchId") final String branchId) {
		Branch trunk = new Branch("trunk", "main development");
		Build build1 = new Build("build1");
		build1.setDate(new Date());
		build1.setRevision("rev:42");
		build1.setState("successful");
		trunk.addBuild(build1);
		
		Build build2 = new Build("build2");
		build2.setDate(new Date());
		build2.setRevision("rev:56");
		build2.setState("failed");
		trunk.addBuild(build2);
		
		return trunk;
	}
}
