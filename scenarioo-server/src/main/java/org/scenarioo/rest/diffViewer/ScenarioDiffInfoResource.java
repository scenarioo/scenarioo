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

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.dao.diffViewer.impl.DiffReaderXmlImpl;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.usecase.UseCasesResource;

/**
 * Handles requests for scenario diff information.
 */
@Path("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}/comparisonName/{comparisonName}/useCaseName/{useCaseName}")
public class ScenarioDiffInfoResource {

	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReaderXmlImpl(configurationRepository.getDiffViewerDirectory());

	@GET
	@Produces("application/json")
	@Path("/scenarioName/{scenarioName}/scenarioDiffInfo")
	public ScenarioDiffInfo getScenarioDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName,
			@PathParam("scenarioName") final String scenarioName) {
		LOGGER.info("REQUEST: getScenarioDiffInfo(" + baseBranchName + ", " + baseBuildName + ", " + comparisonName
				+ ", " + useCaseName + ", " + scenarioName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		return diffReader.loadScenarioDiffInfo(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(),
				comparisonName, useCaseName, scenarioName);
	}

	@GET
	@Produces("application/json")
	@Path("/scenarioDiffInfos")
	public Map<String, ScenarioDiffInfo> getScenarioDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName) {
		LOGGER.info("REQUEST: getScenarioDiffInfos(" + baseBranchName + ", " + baseBranchName + ", " + comparisonName
				+ ", " + useCaseName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		final List<ScenarioDiffInfo> scenarioDiffInfos = diffReader.loadScenarioDiffInfos(
				buildIdentifier.getBranchName(), buildIdentifier.getBuildName(), comparisonName, useCaseName);
		return getScenarioDiffInfoMap(scenarioDiffInfos);

	}

	private Map<String, ScenarioDiffInfo> getScenarioDiffInfoMap(final List<ScenarioDiffInfo> scenarioDiffInfos) {
		final Map<String, ScenarioDiffInfo> scenarioDiffInfoMap = new HashMap<String, ScenarioDiffInfo>();
		for (final ScenarioDiffInfo scenarioDiffInfo : scenarioDiffInfos) {
			scenarioDiffInfoMap.put(scenarioDiffInfo.getName(), scenarioDiffInfo);
		}
		return scenarioDiffInfoMap;
	}
}
