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

package org.scenarioo.rest.diffViewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.usecase.UseCasesResource;

/**
 * Handles requests for scenario diff information.
 */
@Path("/rest/diffViewer/{baseBranchName}/{baseBuildName}/{comparisonName}/{useCaseName}")
public class ScenarioDiffResource {

	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReader(configurationRepository.getDiffViewerDirectory());

	@GET
	@Produces("application/json")
	@Path("/scenarioDiffInfos")
	public Response getScenarioDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName) {
		LOGGER.info("REQUEST: getScenarioDiffInfos(" + baseBranchName + ", " + baseBranchName + ", " + comparisonName
				+ ", " + useCaseName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		try {
			final List<ScenarioDiffInfo> scenarioDiffInfos = diffReader.loadScenarioDiffInfos(
					buildIdentifier.getBranchName(),
					buildIdentifier.getBuildName(), comparisonName, useCaseName);
			return Response.ok(getScenarioDiffInfoMap(scenarioDiffInfos), MediaType.APPLICATION_JSON).build();
		} catch (final ResourceNotFoundException e) {
			LOGGER.warn("Unable to get scenario diff infos", e);
			return Response.noContent().build();
		} catch (final Throwable e) {
			LOGGER.warn("Unable to get scenario diff infos", e);
			return Response.serverError().build();
		}
	}

	private Map<String, ScenarioDiffInfo> getScenarioDiffInfoMap(List<ScenarioDiffInfo> scenarioDiffInfos) {
		final Map<String, ScenarioDiffInfo> scenarioDiffInfoMap = new HashMap<String, ScenarioDiffInfo>();
		for (final ScenarioDiffInfo scenarioDiffInfo : scenarioDiffInfos) {
			scenarioDiffInfoMap.put(scenarioDiffInfo.getName(), scenarioDiffInfo);
		}
		return scenarioDiffInfoMap;
	}
}
