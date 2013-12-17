package org.scenarioo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;

/**
 * Resource for getting access to generic objects stored inside the documentation with detail informations about where
 * such objects are referenced (on which steps, pages etc.)
 */
@Path("/rest/branches/{branchName}/builds/{buildName}/objects/{type}")
public class GenericObjectsResource {
	
	private final ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath());
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<ObjectDescription> readList(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("type") final String type) {
		return dao.loadObjectsList(branchName, buildName, type).getItems();
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{name}/")
	public ObjectIndex readObjectIndex(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("type") final String objectType,
			@PathParam("name") final String objectName) {
		return dao.loadObjectIndex(branchName, buildName, objectType, objectName);
	}
}
