package ngusd.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ngusd.manager.UserScenarioDocuManager;

import org.apache.log4j.Logger;

@Path("/rest/admin/")
public class AdminResource {
	
	private static final Logger LOGGER = Logger.getLogger(AdminResource.class);
	
	@GET
	@Path("update")
	@Produces({ "application/xml", "application/json" })
	public void updateAll() {
		UserScenarioDocuManager.INSTANCE.updateAll();
	}
}
