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

package org.scenarioo.dao.design;

import static org.scenarioo.api.rules.CharacterChecker.*;

import java.io.File;
import java.util.List;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.model.design.entities.StepSketch;

/**
 * Gives access to the design domain files in the file system.
 */
public class DesignReader {

	private final DesignFiles designFiles;

	public DesignReader(final File rootDirectory) {
		this.designFiles = new DesignFiles(rootDirectory);
	}

	public List<Issue> loadIssues(final String branchName) {
		final List<File> files = designFiles.getIssueFiles(checkIdentifier(branchName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Issue.class, files);
	}

	public Issue loadIssue(final String branchName, final String issueId) {
		final File file = designFiles.getIssueFile(checkIdentifier(branchName), checkIdentifier(issueId));
		return ScenarioDocuXMLFileUtil.unmarshal(Issue.class, file);
	}

	public List<ScenarioSketch> loadScenarioSketches(final String branchName, final String issueId) {
		final List<File> files = designFiles.getScenarioSketchFiles(checkIdentifier(branchName),
				checkIdentifier(issueId));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ScenarioSketch.class, files);
	}

	public ScenarioSketch loadScenarioSketch(final String branchName, final String issueId,
			final String scenarioSketchId) {
		final File file = designFiles.getScenarioSketchFile(checkIdentifier(branchName), checkIdentifier(issueId),
				checkIdentifier(scenarioSketchId));
		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioSketch.class, file);
	}

	public List<StepSketch> loadSketchSteps(final String branchName, final String issueId,
			final String scenarioSketchId) {
		final List<File> files = designFiles.getSketchStepFiles(checkIdentifier(branchName), checkIdentifier(issueId),
				checkIdentifier(scenarioSketchId));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(StepSketch.class, files);
	}

	public StepSketch loadSketchStep(final String branchName, final String issueId,
			final String scenarioSketchId, final int stepIndex) {
		final File file = designFiles.getSketchStepFile(checkIdentifier(branchName), checkIdentifier(issueId),
				checkIdentifier(scenarioSketchId), stepIndex);
		return ScenarioDocuXMLFileUtil.unmarshal(StepSketch.class, file);
	}

}