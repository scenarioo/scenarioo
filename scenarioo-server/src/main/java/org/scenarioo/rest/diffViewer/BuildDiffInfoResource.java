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
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Path("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}")
public class BuildDiffInfoResource {

	private static final Logger LOGGER = Logger.getLogger(BuildDiffInfoResource.class);

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GET
	@Produces("application/json")
	@Path("/comparisonName/{comparisonName}/buildDiffInfo")
	public BuildDiffInfo getBuildDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName) {
		LOGGER.info("REQUEST: getBuildDiffInfo(" + baseBranchName + ", " + baseBranchName + ", " + comparisonName
				+ ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		return diffViewerDao.loadBuildDiffInfo(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				comparisonName);
	}

	@GET
	@Produces("application/json")
	@Path("/buildDiffInfos")
	public List<BuildDiffInfo> getBuildDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName) {
		LOGGER.info("REQUEST: getBuildDiffInfos(" + baseBranchName + ", " + baseBranchName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		return diffViewerDao
			.loadBuildDiffInfos(buildIdentifier.getBranchName(), buildIdentifier.getBuildName())
			.stream()
			.filter(new Predicate<BuildDiffInfo>() {
				@Override
				public boolean test(BuildDiffInfo buildDiffInfo) {
					return ComparisonCalculationStatus.SUCCESS.equals(buildDiffInfo.getStatus());
				}
			}).collect(Collectors.toList());
	}
}
