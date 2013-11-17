package org.scenarioo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.scenarioo.manager.ScenarioDocuManager;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;


@Path("/rest/branches/")
public class BranchesResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<BranchBuilds> listBranchesAndBuilds() {
		return ScenarioDocuManager.INSTANCE.getBranchBuildsList();
	}
	
}
