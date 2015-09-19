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

package org.scenarioo.dao.design.aggregates;

import java.io.File;

import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.design.ScenarioSketchIdentifier;

/**
 * Defines locations of aggregated files containing aggregated (=derived) data from design data.
 */
public class DesignAggregateFiles {

	private static final String FILENAME_VERSION_PROPERTIES = "version.derived.properties";
	private static final String FILENAME_ISSUES_XML = "issues.derived.xml";
	private static final String FILENAME_SCENARIOSKETCHES_XML = "scenarioSketches.derived.xml";
	private static final String FILENAME_SCENARIOSKETCH_STEPS_XML = "scenarioSketchSteps.derived.xml";

	private final DesignFiles designFiles;

	public DesignAggregateFiles(final File rootDirectory) {
		designFiles = new DesignFiles(rootDirectory);
	}

	public File getVersionFile(final BuildIdentifier buildIdentifier) {
		return new File(designFiles.getBranchDirectory(buildIdentifier.getBranchName()),
				FILENAME_VERSION_PROPERTIES);
	}

	public File getIssuesAndScenarioSketchesFile(final BuildIdentifier buildIdentifier) {
		File branchDir = designFiles.getBranchDirectory(buildIdentifier.getBranchName());
		return new File(branchDir, FILENAME_ISSUES_XML);
	}

	public File getIssueScenarioSketchesFile(final BuildIdentifier buildIdentifier, final String issueName) {
		File issueDir = designFiles.getIssueDirectory(buildIdentifier.getBranchName(), issueName);
		return new File(issueDir, FILENAME_SCENARIOSKETCHES_XML);
	}

	public File getScenarioSketchStepsFile(final ScenarioSketchIdentifier scenarioSketchIdentifier) {
		File scenarioDir = designFiles.getScenarioSketchDirectory(scenarioSketchIdentifier.getBranchName(),
				scenarioSketchIdentifier.getIssueName(),
				scenarioSketchIdentifier.getScenarioSketchName());
		return new File(scenarioDir, FILENAME_SCENARIOSKETCH_STEPS_XML);
	}

}
