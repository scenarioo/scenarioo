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

package org.scenarioo.rest.sketcher.scenarioSketch;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.sketcher.SketcherDao;
import org.scenarioo.model.sketcher.ScenarioSketch;
import org.scenarioo.utils.IdGenerator;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch")
public class ScenarioSketchResource {

	private static final Logger LOGGER = Logger.getLogger(ScenarioSketchResource.class);

	private final SketcherDao sketcherDao = new SketcherDao();

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response storeScenarioSketch(@PathParam("branchName") final String branchName,
			final ScenarioSketch scenarioSketch) {
		LOGGER.info("REQUEST: storeScenarioSketch(" + branchName + ", " + scenarioSketch + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		Date now = new Date();
		scenarioSketch.setScenarioSketchId(IdGenerator.generateRandomId());
		scenarioSketch.setDateCreated(now);
		scenarioSketch.setDateModified(now);

		sketcherDao.persistScenarioSketch(resolvedBranchName, scenarioSketch.getIssueId(), scenarioSketch);

		return Response.ok(scenarioSketch, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/{scenarioSketchId}")
	public Response updateScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final ScenarioSketch updatedScenarioSketch) {
		LOGGER.info("REQUEST: updateScenarioSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		final ScenarioSketch scenarioSketch = sketcherDao.loadScenarioSketch(resolvedBranchName, issueId, scenarioSketchId);
		scenarioSketch.setDateModified(new Date());
		scenarioSketch.setAuthor(updatedScenarioSketch.getAuthor());

		sketcherDao.persistScenarioSketch(resolvedBranchName, scenarioSketchId, scenarioSketch);

		return Response.ok(updatedScenarioSketch, MediaType.APPLICATION_JSON).build();
	}

}
