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
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.NumberFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/rest/diffViewer/baseBranchName/{baseBranchName}/baseBuildName/{baseBuildName}")
public class StepDiffScreenshotResource {

	private static final Logger LOGGER = Logger.getLogger(StepDiffScreenshotResource.class);

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GetMapping(path = "comparisonName/{comparisonName}/useCaseName/{usecaseName}/scenarioName/{scenarioName}/stepIndex/{stepIndex}/stepDiffScreenshot", produces = "image/png")
	public File getDiffScreenshot(
			@PathVariable("baseBranchName") final String baseBranchName,
			@PathVariable("baseBuildName") final String baseBuildName,
			@PathVariable("comparisonName") final String comparisonName,
			@PathVariable("usecaseName") final String usecaseName,
			@PathVariable("scenarioName") final String scenarioName,
			@PathVariable("stepIndex") final int stepIndex) {

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
			baseBranchName,
			baseBuildName);

		final String imageFileName = NumberFormatter.formatMinimumThreeDigits(stepIndex) + ".png";

		return diffViewerDao.getScreenshotFile(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
			comparisonName, usecaseName, scenarioName, imageFileName);
	}

}
