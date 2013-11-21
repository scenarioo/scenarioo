package org.scenarioo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.scenarioo.manager.ScenarioDocuBuildsManager;


@Path("/rest/admin/")
public class AdminResource {
	
	@GET
	@Path("update")
	@Produces({ "application/xml", "application/json" })
	public void updateAll() {
		ScenarioDocuBuildsManager.INSTANCE.updateAll();
	}
}
