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

package org.scenarioo.rest.search;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.BuildIdentifier;

@Path("/rest/branch/{branchName}/build/{buildName}/")
public class SearchResource {

	@GET
	@Produces("application/json")
	@Path("/search/{q}")
	public ObjectIndex search(@PathParam("branchName") final String branchName,
							  @PathParam("buildName") final String buildName, @PathParam("q") final String q) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		FullTextSearch search = new FullTextSearch();
		List<ObjectReference> result = search.search(buildIdentifier, q);

		if(result.isEmpty()) {
			return new ObjectIndex();
		}

		ObjectReference first = result.remove(0);

		ObjectDescription description = new ObjectDescription(first.getType(), first.getName());

		ObjectTreeNode<ObjectReference> objectTreeNode = new ObjectTreeNode<ObjectReference>(first);
		for(ObjectReference entry : result) {
			ObjectTreeNode<ObjectReference> parent = new ObjectTreeNode<ObjectReference>(entry);
			parent.addChild(objectTreeNode);
			objectTreeNode = parent;
		}

		ObjectIndex index = new ObjectIndex();
		index.setObject(description);
		index.setReferenceTree(objectTreeNode);

		return index;
	}

	@GET
	@Path("/searchEngine")
	public Response isEngineRunning() {
		if(new FullTextSearch().isEngineRunning()){
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
