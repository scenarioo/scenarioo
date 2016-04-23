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

package org.scenarioo.business.diffViewer;

import org.apache.log4j.Logger;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;

/**
 * Comparator to compare screenshots of two steps. Results are persisted in a xml file.
 */
public class ScreenshotComparator extends AbstractComparator {

	private static final Logger LOGGER = Logger.getLogger(ScreenshotComparator.class);

	public ScreenshotComparator(String baseBranchName, String baseBuildName, String comparisonName) {
		super(baseBranchName, baseBuildName, comparisonName);
	}

	/**
	 * Compares the screenshots of the given step.
	 * 
	 * @param baseUseCaseName
	 *            the use case of the screenshots.
	 * @param baseScenarioName
	 *            the scenario of the screenshots.
	 * @param baseStepLink
	 *            the step to compare the screenshots.
	 * @return {@link StepDiffInfo} with the summarized diff information.
	 */
	public StepDiffInfo compare(final String baseUseCaseName, final String baseScenarioName, final StepLink baseStepLink) {
		return null;
	}
}
