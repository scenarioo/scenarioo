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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.elasticsearch.index.IndexNotFoundException;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.dao.search.SearchEngineNotRunningException;
import org.scenarioo.dao.search.SearchFailedException;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.BuildIdentifier;

@Path("/rest")
public class SearchResource {

	@GET
	@Produces("application/json")
	@Path("/branch/{branchName}/build/{buildName}/search/{q}")
	public SearchResponse search(@PathParam("branchName") final String branchName,
												  @PathParam("buildName") final String buildName, @PathParam("q") final String q) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		FullTextSearch search = new FullTextSearch();
		try {
			ObjectTreeNode<ObjectReference> results = search.search(q, buildIdentifier).buildObjectTree();
			return new SearchResponse(results);
		} catch (IndexNotFoundException e) {
			return new SearchResponse("The search index was not found for the selected build.");
		} catch (SearchEngineNotRunningException e) {
			return new SearchResponse("The search engine is not running or not reachable for Scenarioo.");
		} catch (SearchFailedException e) {
			return new SearchResponse(e.getMessage());
		}
	}

	@GET
	@Path("/searchEngineStatus")
	public SearchEngineStatus getSearchEngineStatus() {
		return SearchEngineStatus.create();
	}
}
