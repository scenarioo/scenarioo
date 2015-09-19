/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.design.aggregates.issues.IssueScenarioSketchesList;
import org.scenarioo.model.design.aggregates.IssueScenarioSketches;
import org.scenarioo.model.design.aggregates.ScenarioSketchSteps;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.design.ScenarioSketchIdentifier;
import org.scenarioo.utils.ResourceUtils;

public class IssueAggregationDAO {

	private static final String VERSION_PROPERTY_KEY = "scenarioo.derived.file.format.version";

	final File rootDirectory;
	private final DesignAggregateFiles files;

	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public IssueAggregationDAO(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
		files = new DesignAggregateFiles(rootDirectory);
	}

	/**
	 * @see org.scenarioo.dao.design.aggregates.AggregatedDocuDataReader#loadVersion(java.lang.String, java.lang.String)
	 */
	public String loadVersion(final BuildIdentifier buildIdentifier) {
		File versionFile = files.getVersionFile(buildIdentifier);
		if (versionFile.exists()) {
			Properties properties = new Properties();
			FileReader reader = null;
			try {
				reader = new FileReader(versionFile);
				properties.load(reader);
				return properties.getProperty(VERSION_PROPERTY_KEY);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("file not found: " + versionFile.getAbsolutePath(), e);
			} catch (IOException e) {
				throw new RuntimeException("file not readable: " + versionFile.getAbsolutePath(), e);
			} finally {
				ResourceUtils.close(reader, versionFile.getAbsolutePath());
			}
		} else {
			return "";
		}
	}

	public List<IssueScenarioSketches> loadIssueScenarioSketchesList(final BuildIdentifier buildIdentifier) {
		File file = files.getIssuesAndScenarioSketchesFile(buildIdentifier);
		IssueScenarioSketchesList list = ScenarioDocuXMLFileUtil.unmarshal(IssueScenarioSketchesList.class, file);
		return list.getIssueScenarioSketches();
	}

	public IssueScenarioSketches loadIssueScenarioSketches(final BuildIdentifier buildIdentifier, final String issueName) {
		File file = files.getIssueScenarioSketchesFile(buildIdentifier, issueName);
		IssueScenarioSketches issueScenariosketches = ScenarioDocuXMLFileUtil.unmarshal(IssueScenarioSketches.class, file);
		return issueScenariosketches;
	}

	public ScenarioSketchSteps loadScenarioSketchSteps(final ScenarioSketchIdentifier scenarioSketchIdentifier) {
		File file = files.getScenarioSketchStepsFile(scenarioSketchIdentifier);
		ScenarioSketchSteps scenarioSketchSteps = ScenarioDocuXMLFileUtil.unmarshal(ScenarioSketchSteps.class, file);
		return scenarioSketchSteps;
	}

}
