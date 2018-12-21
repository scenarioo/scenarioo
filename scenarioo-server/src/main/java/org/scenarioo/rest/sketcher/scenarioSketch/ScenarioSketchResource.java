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

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.BranchAliasResolver;
import org.scenarioo.dao.sketcher.SketcherDao;
import org.scenarioo.model.sketcher.ScenarioSketch;
import org.scenarioo.utils.IdGenerator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/rest/branch/{branchName}/issue/{issueId}/scenariosketch")
public class ScenarioSketchResource {

	private static final Logger LOGGER = Logger.getLogger(ScenarioSketchResource.class);

	private final SketcherDao sketcherDao = new SketcherDao();

	@PostMapping
	public ResponseEntity storeScenarioSketch(@PathVariable("branchName") final String branchName,
											  @RequestBody final ScenarioSketch scenarioSketch) {
		LOGGER.info("REQUEST: storeScenarioSketch(" + branchName + ", " + scenarioSketch + ")");

		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);

		Date now = new Date();
		scenarioSketch.setScenarioSketchId(IdGenerator.generateRandomId());
		scenarioSketch.setDateCreated(now);
		scenarioSketch.setDateModified(now);

		sketcherDao.persistScenarioSketch(resolvedBranchName, scenarioSketch.getIssueId(), scenarioSketch);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(scenarioSketch);
	}

	@PostMapping("/{scenarioSketchId}")
	public ResponseEntity updateScenarioSketch(@PathVariable("branchName") final String branchName,
										 @PathVariable("issueId") final String issueId,
										 @PathVariable("scenarioSketchId") final String scenarioSketchId,
										 @RequestBody  final ScenarioSketch updatedScenarioSketch) {
		LOGGER.info("REQUEST: updateScenarioSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ")");

		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);

		final ScenarioSketch scenarioSketch = sketcherDao.loadScenarioSketch(resolvedBranchName, issueId, scenarioSketchId);
		scenarioSketch.setDateModified(new Date());
		scenarioSketch.setAuthor(updatedScenarioSketch.getAuthor());

		sketcherDao.persistScenarioSketch(resolvedBranchName, scenarioSketchId, scenarioSketch);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedScenarioSketch);
	}

}
