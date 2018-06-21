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
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.base.BuildIdentifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}")
public class BuildDiffInfoResource {

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GetMapping("/comparisonName/{comparisonName}/buildDiffInfo")
	public BuildDiffInfo getBuildDiffInfo(@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName,
			@PathVariable("comparisonName") final String comparisonName) {

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		return diffViewerDao.loadBuildDiffInfo(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				comparisonName);
	}

	@GetMapping("/buildDiffInfos")
	public List<BuildDiffInfo> getBuildDiffInfos(@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName) {
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		return diffViewerDao
			.loadBuildDiffInfos(buildIdentifier.getBranchName(), buildIdentifier.getBuildName())
			.stream()
			.filter(buildDiffInfo ->
				ComparisonCalculationStatus.SUCCESS.equals(buildDiffInfo.getStatus())
			).collect(Collectors.toList());
	}
}
