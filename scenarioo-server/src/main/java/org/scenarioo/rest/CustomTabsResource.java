package org.scenarioo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.rest.base.AbstractBuildContentResource;

@Path("/rest/branches/{branchName}/builds/{buildName}/customTabObjects/{tabId}")
public class CustomTabsResource extends AbstractBuildContentResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public CustomObjectTabTree readObjectTreeForTab(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("tabId") final String tabId) {
		
		// TODO #64: do not forget to check for new branch alias mapping to
		// integrate in all new services (after merging it properly)
		
		String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBuildName(branchName, buildName);
		
		return getDAO(branchName, resolvedBuildName).loadCustomObjectTabTree(
				new BuildIdentifier(branchName, resolvedBuildName), tabId);
	}
	
}
