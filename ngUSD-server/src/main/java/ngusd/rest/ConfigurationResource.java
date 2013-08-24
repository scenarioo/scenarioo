package ngusd.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ngusd.dao.ConfigurationDAO;
import ngusd.model.configuration.Configuration;

@Path("/rest/configuration/")
public class ConfigurationResource {
	
	@GET
	@Path("/buildstates")
	@Produces("application/json")
	public Map<String, String> getStatusClasses() {
		return ConfigurationDAO.getConfiguration().getBuildstates();
	}
	
	@GET
	@Produces({ "application/json", "application/xml" })
	public Configuration getConfiguration() {
		return ConfigurationDAO.getConfiguration();
	}
	
	@POST
	@Consumes({ "application/json", "application/xml" })
	public void updateConfiguration(final Configuration configuration) {
		ConfigurationDAO.updateConfiguration(configuration);
	}
	
}
