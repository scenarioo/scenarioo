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

import org.scenarioo.business.builds.BranchAliasResolver;
import org.scenarioo.dao.sketcher.SketcherDao;
import org.scenarioo.rest.base.FileResponseCreator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/stepsketch")
public class SketchImageResource {

	private final SketcherDao sketcherDao = new SketcherDao();

	@GetMapping(path = "{stepSketchId}/svg/{stepSketchId}", produces = "image/svg+xml")
	public ResponseEntity<InputStreamResource> loadSketch(@PathVariable("branchName") final String branchName,
			@PathVariable("issueId") final String issueId,
			@PathVariable("scenarioSketchId") final String scenarioSketchId,
			@PathVariable("stepSketchId") final String stepSketchId) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);
		File svgFile = sketcherDao.getStepSketchSvgFile(resolvedBranchName, issueId, scenarioSketchId, stepSketchId);
		return FileResponseCreator.createImageFileResponse(svgFile);
	}

	/**
	 * @param pngFileName
	 *            Specify whether you want to load the original PNG file or the sketch PNG file.
	 */
	@GetMapping(path = "{stepSketchId}/image/{pngFile}", produces = "image/png" )
	public ResponseEntity<InputStreamResource> loadPngFile(@PathVariable("branchName") final String branchName,
														   @PathVariable("issueId") final String issueId,
														   @PathVariable("scenarioSketchId") final String scenarioSketchId,
														   @PathVariable("stepSketchId") final String stepSketchId,
														   @PathVariable("pngFile") final String pngFileName) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);
		File pngFile = sketcherDao.getStepSketchPngFile(resolvedBranchName, issueId, scenarioSketchId, stepSketchId, pngFileName);
		return FileResponseCreator.createImageFileResponse(pngFile);
	}

}
