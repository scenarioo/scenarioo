package ngusd.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/rest/configuration/")
public class ConfigurationResource {
	
	// @GET
	// @Produces("application/json", "application/xml")
	// public
	//
	@GET
	@Path("/buildstates")
	@Produces("application/json")
	public Map<String, String> getStatusClasses() {
		Map<String, String> classes = new HashMap<String, String>();
		
		classes.put("success", "label-success");
		classes.put("warning", "label-warning");
		classes.put("failed", "label-important");
		
		return classes;
	}
}
