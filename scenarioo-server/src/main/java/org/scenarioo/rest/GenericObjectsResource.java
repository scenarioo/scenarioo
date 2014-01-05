/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.rest.base.AbstractBuildContentResource;

/**
 * Resource for getting access to generic objects stored inside the documentation with detail informations about where
 * such objects are referenced (on which steps, pages etc.)
 */
@Path("/rest/branches/{branchName}/builds/{buildName}/objects/{type}")
public class GenericObjectsResource extends AbstractBuildContentResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<ObjectDescription> readList(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("type") final String type) {
		String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBuildName(branchName, buildName);
		return getDAO(branchName, buildName).loadObjectsList(branchName, resolvedBuildName, type).getItems();
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{name}/")
	public ObjectIndex readObjectIndex(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("type") final String objectType,
			@PathParam("name") final String objectName) {
		String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBuildName(branchName, buildName);
		return getDAO(branchName, buildName).loadObjectIndex(branchName, resolvedBuildName, objectType, objectName);
	}
}
