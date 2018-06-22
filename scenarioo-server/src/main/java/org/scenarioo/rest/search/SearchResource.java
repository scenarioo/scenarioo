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

import org.elasticsearch.index.IndexNotFoundException;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.search.*;
import org.scenarioo.rest.base.BuildIdentifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class SearchResource {

	@GetMapping("/branch/{branchName}/build/{buildName}/search/{q}")
	public SearchResponse search(@PathVariable("branchName") final String branchName,
								 @PathVariable("buildName") final String buildName, @PathVariable("q") final String q,
								 @RequestParam(value = "includeHtml", required = false) boolean includeHtml) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		FullTextSearch search = new FullTextSearch();
		try {
			SearchTree results = search.search(new SearchRequest(buildIdentifier, q, includeHtml));
			return new SearchResponse(results);
		} catch (IndexNotFoundException e) {
			return new SearchResponse("The search index was not found for the selected build.");
		} catch (SearchEngineNotRunningException e) {
			return new SearchResponse("The search engine is not running or not reachable for Scenarioo.");
		} catch (SearchFailedException e) {
			return new SearchResponse(e.getMessage());
		}
	}

	@GetMapping("/searchEngineStatus")
	public SearchEngineStatus getSearchEngineStatus() {
		return SearchEngineStatus.create();
	}
}
