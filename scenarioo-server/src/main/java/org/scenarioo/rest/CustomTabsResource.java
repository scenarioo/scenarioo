package org.scenarioo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.rest.base.AbstractBuildContentResource;
import org.scenarioo.rest.request.BuildIdentifier;

@Path("/rest/branches/{branchName}/builds/{buildName}/customTabObjects/{tabId}")
public class CustomTabsResource extends AbstractBuildContentResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public CustomObjectTabTree readObjectTreeForTab(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("tabId") final String tabId) {
		
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		
		return getDAO(buildIdentifier).loadCustomObjectTabTree(buildIdentifier, tabId);
	}
	
}
