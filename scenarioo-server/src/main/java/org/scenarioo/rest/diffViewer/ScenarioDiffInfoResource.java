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

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.usecase.UseCasesResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}/comparisonName/{comparisonName}/useCaseName/{useCaseName}")
public class ScenarioDiffInfoResource {

	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);

	private DiffViewerDao DiffViewerDao = new DiffViewerDao();

	@GET
	@Produces("application/json")
	@Path("/scenarioName/{scenarioName}/scenarioDiffInfo")
	public ScenarioDiffInfo getScenarioDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName,
			@PathParam("scenarioName") final String scenarioName) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);
		return DiffViewerDao.loadScenarioDiffInfo(buildIdentifier.getBranchName(),
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
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		final List<ScenarioDiffInfo> scenarioDiffInfos = DiffViewerDao.loadScenarioDiffInfos(
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
