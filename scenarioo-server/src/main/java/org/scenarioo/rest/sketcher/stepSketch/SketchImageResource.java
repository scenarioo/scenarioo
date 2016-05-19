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

package org.scenarioo.rest.sketcher.stepSketch;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.sketcher.SketcherDao;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/stepsketch/{stepSketchId}/")
public class SketchImageResource {

	private static final Logger LOGGER = Logger.getLogger(SketchImageResource.class);

	private final SketcherDao sketcherDao = new SketcherDao();

	@GET
	@Path("svg/{stepSketchId}")
	@Produces({ "image/svg+xml" })
	public Object loadSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("stepSketchId") final String stepSketchId) {
		LOGGER.info("REQUEST: loadSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketchId + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		return sketcherDao.getStepSketchSvgFile(resolvedBranchName, issueId, scenarioSketchId, stepSketchId);
	}

	/**
	 * @param pngFileName
	 *            Specify whether you want to load the original PNG file or the sketch PNG file.
	 */
	@GET
	@Path("image/{pngFile}")
	@Produces({ "image/png" })
	@NoCache
	public Object loadPngFile(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("stepSketchId") final String stepSketchId,
			@PathParam("pngFile") final String pngFileName) {
		LOGGER.info("REQUEST: loadPngFile(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketchId + ", " + pngFileName + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		return sketcherDao.getStepSketchPngFile(resolvedBranchName, issueId, scenarioSketchId, stepSketchId, pngFileName);
	}

}