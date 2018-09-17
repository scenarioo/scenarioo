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

package org.scenarioo.rest.objectRepository;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.rest.base.AbstractBuildContentResource;
import org.scenarioo.rest.base.BuildIdentifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Resource for getting access to generic objects stored inside the documentation with detail informations about where
 * such objects are referenced (on which steps, pages etc.)
 */
@RestController
@RequestMapping("/rest/branch/{branchName}/build/{buildName}/object")
public class GenericObjectsResource extends AbstractBuildContentResource {

	@GetMapping("{type}")
	public List<ObjectDescription> readList(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("type") final String type) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		return getDAO(buildIdentifier).loadObjectsList(buildIdentifier, type).getItems();
	}

	@GetMapping(path = "{type}/name", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ObjectIndex readObjectIndex(@PathVariable("branchName") final String branchName,
									   @PathVariable("buildName") final String buildName, @PathVariable("type") final String objectType,
			@RequestParam("name") final String objectName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
			buildName);

		AggregatedDocuDataReader aggregatedDataReader = getDAO(buildIdentifier);
		return aggregatedDataReader.loadObjectIndex(buildIdentifier, objectType, objectName);
	}

}
