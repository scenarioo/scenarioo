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

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.rest.base.BuildIdentifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}/")
public class UseCaseDiffInfoResource {

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GetMapping("comparisonName/{comparisonName}/useCaseName/{useCaseName}/useCaseDiffInfo")
	public UseCaseDiffInfo getUseCaseDiffInfo(@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName,
			@PathVariable("comparisonName") final String comparisonName,
			@PathVariable("useCaseName") final String useCaseName) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		return diffViewerDao.loadUseCaseDiffInfo(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				comparisonName, useCaseName);
	}

	@GetMapping("comparisonName/{comparisonName}/useCaseDiffInfos")
	public Map<String, UseCaseDiffInfo> getUseCaseDiffInfos(@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName,
			@PathVariable("comparisonName") final String comparisonName) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		final List<UseCaseDiffInfo> useCaseDiffInfos = diffViewerDao.loadUseCaseDiffInfos(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(), comparisonName);
		return getUseCaseDiffInfoMap(useCaseDiffInfos);
	}

	private Map<String, UseCaseDiffInfo> getUseCaseDiffInfoMap(final List<UseCaseDiffInfo> useCaseDiffInfos) {
		final Map<String, UseCaseDiffInfo> useCaseDiffInfoMap = new HashMap<>();
		for (final UseCaseDiffInfo useCaseDiffInfo : useCaseDiffInfos) {
			useCaseDiffInfoMap.put(useCaseDiffInfo.getName(), useCaseDiffInfo);
		}
		return useCaseDiffInfoMap;
	}

}
