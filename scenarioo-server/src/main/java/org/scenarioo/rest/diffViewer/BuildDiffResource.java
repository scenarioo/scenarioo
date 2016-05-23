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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.dao.diffViewer.impl.DiffReaderXmlImpl;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Handles requests for build diff information.
 */
@Path("/rest/diffViewer/{baseBranchName}/{baseBuildName}")
public class BuildDiffResource {

	private static final Logger LOGGER = Logger.getLogger(BuildDiffResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReaderXmlImpl(configurationRepository.getDiffViewerDirectory());

	@GET
	@Produces("application/json")
	@Path("/{comparisonName}/buildDiffInfo")
	public BuildDiffInfo getBuildDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName) {
		LOGGER.info("REQUEST: getBuildDiffInfo(" + baseBranchName + ", " + baseBranchName + ", " + comparisonName
				+ ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		return diffReader.loadBuildDiffInfo(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
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

		return diffReader.loadBuildDiffInfos(buildIdentifier.getBranchName(), buildIdentifier.getBuildName());
	}
}
