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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}")
public class ScenarioDiffInfoResource {

	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GetMapping("comparisonName/{comparisonName}/useCaseName/{useCaseName}/scenarioName/{scenarioName}/scenarioDiffInfo")
	public ScenarioDiffInfo getScenarioDiffInfo(@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName,
			@PathVariable("comparisonName") final String comparisonName,
			@PathVariable("useCaseName") final String useCaseName,
			@PathVariable("scenarioName") final String scenarioName) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);
		return diffViewerDao.loadScenarioDiffInfo(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(),
				comparisonName, useCaseName, scenarioName);
	}

	@GetMapping("comparisonName/{comparisonName}/useCaseName/{useCaseName}/scenarioDiffInfos")
	public Map<String, ScenarioDiffInfo> getScenarioDiffInfos(@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName,
			@PathVariable("comparisonName") final String comparisonName,
			@PathVariable("useCaseName") final String useCaseName) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		final List<ScenarioDiffInfo> scenarioDiffInfos = diffViewerDao.loadScenarioDiffInfos(
				buildIdentifier.getBranchName(), buildIdentifier.getBuildName(), comparisonName, useCaseName);
		return getScenarioDiffInfoMap(scenarioDiffInfos);
	}

	private Map<String, ScenarioDiffInfo> getScenarioDiffInfoMap(final List<ScenarioDiffInfo> scenarioDiffInfos) {
		final Map<String, ScenarioDiffInfo> scenarioDiffInfoMap = new HashMap<>();
		for (final ScenarioDiffInfo scenarioDiffInfo : scenarioDiffInfos) {
			scenarioDiffInfoMap.put(scenarioDiffInfo.getName(), scenarioDiffInfo);
		}
		return scenarioDiffInfoMap;
	}
}
