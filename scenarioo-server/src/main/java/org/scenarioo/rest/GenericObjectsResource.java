package org.scenarioo.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.dao.ConfigurationDAO;
import org.scenarioo.dao.ScenarioDocuAggregationDAO;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;


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
		List<ObjectDescription> objects = new ArrayList<ObjectDescription>();
		objects.addAll(createObjectDescriptions(type, "MobileService.operation", 10));
		objects.addAll(createObjectDescriptions(type, "TvService.operation", 10));
		return objects;
		
	}
	
	private Collection<? extends ObjectDescription> createObjectDescriptions(final String type,
			final String namePrefix, final int size) {
		List<ObjectDescription> objects = new ArrayList<ObjectDescription>();
		for (int i = 0; i < size; i++) {
			objects.add(createObjectDescription(type, namePrefix + i));
		}
		return objects;
	}
	
	private ObjectDescription createObjectDescription(final String type, final String name) {
		ObjectDescription obj = new ObjectDescription(type, name);
		obj.addDetail("eaiName", "TIBCO:" + name);
		obj.addDetail("realName", "SIEBEL:" + name);
		return obj;
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{name}/")
	public ObjectIndex readObjectIndex(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("type") final String type,
			@PathParam("name") final String name) {
		
		ObjectIndex result = new ObjectIndex();
		result.setObject(createObjectDescription(type, name));
		
		ObjectTreeNode<ObjectReference> node = new ObjectTreeNode<ObjectReference>(null);
		
		return result;
	}
}
