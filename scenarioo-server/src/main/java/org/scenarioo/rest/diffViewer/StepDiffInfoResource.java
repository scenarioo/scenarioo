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
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}/")
public class StepDiffInfoResource {

	private static final Logger LOGGER = Logger.getLogger(StepDiffInfoResource.class);

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GET
	@Produces("application/json")
	@Path("comparisonName/{comparisonName}/useCaseName/{useCaseName}/scenarioName/{scenarioName}/stepIndex/{stepIndex}/stepDiffInfo")
	public StepDiffInfo getStepDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName,
			@PathParam("scenarioName") final String scenarioName,
			@PathParam("stepIndex") final String stepIndex) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		return diffViewerDao.loadStepDiffInfo(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				comparisonName, useCaseName, scenarioName, Integer.parseInt(stepIndex));
	}

	@GET
	@Produces("application/json")
	@Path("comparisonName/{comparisonName}/useCaseName/{useCaseName}/scenarioName/{scenarioName}/stepDiffInfos")
	public Map<Integer, StepDiffInfo> getStepDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName,
			@PathParam("scenarioName") final String scenarioName) {

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName, baseBuildName);

		final List<StepDiffInfo> stepDiffInfos = diffViewerDao.loadStepDiffInfos(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(), comparisonName, useCaseName, scenarioName);

		return getStepDiffInfoMap(stepDiffInfos);
	}

	private Map<Integer, StepDiffInfo> getStepDiffInfoMap(final List<StepDiffInfo> stepDiffInfos) {
		final Map<Integer, StepDiffInfo> stepDiffInfoMap = new HashMap<Integer, StepDiffInfo>();
		for (final StepDiffInfo stepDiffInfo : stepDiffInfos) {
			stepDiffInfoMap.put(stepDiffInfo.getIndex(), stepDiffInfo);
		}
		return stepDiffInfoMap;
	}
}
