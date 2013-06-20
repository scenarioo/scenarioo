package ngusd.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ngusd.dao.ScenarioDocuFilesystem;
import ngusd.docu.model.Branch;
import ngusd.rest.model.BranchBuilds;

@Path("/rest/branches/")
public class BranchesResource {
	
	ScenarioDocuFilesystem filesystem = new ScenarioDocuFilesystem();
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<BranchBuilds> listBranchesAndBuilds() {
		
		List<BranchBuilds> result = new ArrayList<BranchBuilds>();
		
		List<Branch> branches = filesystem.getBranches();
		for (Branch branch : branches) {
			BranchBuilds branchBuilds = new BranchBuilds();
			branchBuilds.setBranch(branch);
			branchBuilds.setBuilds(filesystem.getBuilds(branch.getName()));
			result.add(branchBuilds);
		}
		return result;
	}
}
