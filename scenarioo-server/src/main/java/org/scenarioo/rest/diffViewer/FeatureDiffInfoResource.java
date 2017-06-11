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
import org.scenarioo.model.diffViewer.FeatureDiffInfo;
import org.scenarioo.rest.base.BuildIdentifier;

@Path("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}/comparisonName/{comparisonName}")
public class FeatureDiffInfoResource {

	private static final Logger LOGGER = Logger.getLogger(FeatureDiffInfoResource.class);

	private DiffReader diffReader = new DiffReaderXmlImpl();

	@GET
	@Produces("application/json")
	@Path("/featureName/{featureName}/featureDiffInfo")
	public FeatureDiffInfo getFeatureDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
											  @PathParam("baseBuildName") final String baseBuildName,
											  @PathParam("comparisonName") final String comparisonName,
											  @PathParam("featureName") final String featureName) {
		LOGGER.info("REQUEST: getFeatureDiffInfo(" + baseBranchName + ", " + baseBuildName + ", " + comparisonName
				+ ", " + featureName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		return diffReader.loadFeatureDiffInfo(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				comparisonName, featureName);

	}

	@GET
	@Produces("application/json")
	@Path("/featureDiffInfos")
	public Map<String, FeatureDiffInfo> getFeatureDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
															@PathParam("baseBuildName") final String baseBuildName,
															@PathParam("comparisonName") final String comparisonName) {
		LOGGER.info("REQUEST: getFeatureDiffInfos(" + baseBranchName + ", " + baseBuildName + ", " + comparisonName
				+ ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(baseBranchName, baseBuildName);

		final List<FeatureDiffInfo> featureDiffInfos = diffReader.loadFeatureDiffInfos(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(), comparisonName);
		return getFeatureDiffInfoMap(featureDiffInfos);

	}

	private Map<String, FeatureDiffInfo> getFeatureDiffInfoMap(final List<FeatureDiffInfo> featureDiffInfos) {
		final Map<String, FeatureDiffInfo> featureDiffInfoMap = new HashMap<String, FeatureDiffInfo>();
		for (final FeatureDiffInfo featureDiffInfo : featureDiffInfos) {
			featureDiffInfoMap.put(featureDiffInfo.getName(), featureDiffInfo);
		}
		return featureDiffInfoMap;
	}

}
