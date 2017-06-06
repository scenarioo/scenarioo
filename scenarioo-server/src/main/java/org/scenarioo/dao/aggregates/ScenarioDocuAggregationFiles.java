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

package org.scenarioo.dao.aggregates;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.utils.NumberFormatCreator;

/**
 * Defines locations of aggregated files containing aggregated (=derived) data from documentation input data.
 */
public class ScenarioDocuAggregationFiles {

	private static NumberFormat THREE_DIGIT_NUM_FORMAT = NumberFormatCreator
			.createNumberFormatWithMinimumIntegerDigits(3);

	private static final String DIRECTORY_NAME_OBJECT_INDEXES = "index";
	private static final String DIRECTORY_NAME_OBJECTS = "objects.derived";
	private static final String DIRECTORY_NAME_CUSTOM_OBJECT_TAB_TREES = "customObjectTabTrees.derived";
	private static final String FILENAME_VERSION_PROPERTIES = "version.derived.properties";
	private static final String FILENAME_FEATURES_XML = "features.derived.xml";
	private static final String FILENAME_SCENARIOS_XML = "scenarios.derived.xml";
	private static final String FILENAME_SCENARIO_PAGE_STEPS_XML = "scenarioPageSteps.derived.xml";
	private static final String FILENAME_LONG_OBJECT_NAMES_INDEX = "longObjectNamesIndex.derived.xml";

	private final ScenarioDocuFiles docuFiles;

	public ScenarioDocuAggregationFiles(final File rootDirectory) {
		docuFiles = new ScenarioDocuFiles(rootDirectory);
	}

	public File getRootDirectory() {
		return docuFiles.getRootDirectory();
	}

	public File getBuildStatesFile() {
		return new File(docuFiles.getRootDirectory(), "builds.states.derived.xml");
	}

	public File getVersionFile(final BuildIdentifier buildIdentifier) {
		return new File(docuFiles.getBuildDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName()),
				FILENAME_VERSION_PROPERTIES);
	}

	public File getFeaturesAndScenariosFile(final BuildIdentifier buildIdentifier) {
		File buildDir = docuFiles.getBuildDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName());
		return new File(buildDir, FILENAME_FEATURES_XML);
	}

	public File getFeatureScenariosFile(final BuildIdentifier buildIdentifier, final String featureName) {
		File caseDir = docuFiles.getFeatureDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				featureName);
		return new File(caseDir, FILENAME_SCENARIOS_XML);
	}

	public File getScenarioStepsFile(final ScenarioIdentifier scenarioIdentifier) {
		File scenarioDir = docuFiles.getScenarioDirectory(scenarioIdentifier.getBuildIdentifier().getBranchName(),
				scenarioIdentifier.getBuildIdentifier().getBuildName(), scenarioIdentifier.getFeatureName(),
				scenarioIdentifier.getScenarioName());
		return new File(scenarioDir, FILENAME_SCENARIO_PAGE_STEPS_XML);
	}

	public File getObjectsDirectory(final BuildIdentifier buildIdentifier) {
		return new File(docuFiles.getBuildDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName()),
				DIRECTORY_NAME_OBJECTS);
	}

	private File getCustomObjectTabTreesDirectory(final BuildIdentifier buildIdentifier) {
		File objectDirectory = getObjectsDirectory(buildIdentifier);
		return new File(objectDirectory, DIRECTORY_NAME_CUSTOM_OBJECT_TAB_TREES);
	}

	public File getObjectsDirectoryForObjectType(final BuildIdentifier buildIdentifier, final String typeName) {
		return new File(getObjectsDirectory(buildIdentifier), FilesUtil.encodeName(typeName));
	}

	public File getObjectsIndexDirectoryForObjectType(final BuildIdentifier buildIdentifier, final String typeName) {
		return new File(getObjectsDirectoryForObjectType(buildIdentifier, typeName), DIRECTORY_NAME_OBJECT_INDEXES);
	}

	public File getObjectFile(final BuildIdentifier buildIdentifier, final String objectType, final String objectName) {
		File objectsDir = getObjectsDirectoryForObjectType(buildIdentifier, objectType);
		return new File(objectsDir, FilesUtil.encodeName(objectName) + ".description.xml");
	}

	public File getObjectListFile(final BuildIdentifier buildIdentifier, final String type) {
		File objectsDir = getObjectsDirectory(buildIdentifier);
		return new File(objectsDir, FilesUtil.encodeName(type) + ".list.xml");
	}

	public File getObjectIndexFile(final BuildIdentifier buildIdentifier, final String type, final String name) {
		File objectsDir = getObjectsIndexDirectoryForObjectType(buildIdentifier, type);
		return new File(objectsDir, FilesUtil.encodeName(name) + ".index.xml");
	}

	public File getCustomObjectTabTreeFile(final BuildIdentifier buildIdentifier, final String tabId) {
		File customObjectTabTreesDir = getCustomObjectTabTreesDirectory(buildIdentifier);
		return new File(customObjectTabTreesDir, FilesUtil.encodeName(tabId) + ".objectTree.derived.xml");
	}

	public File getBuildImportLogFile(final BuildIdentifier buildIdentifier) {
		return new File(docuFiles.getBuildDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName()),
				"import.derived.log");
	}

	public List<File> getObjectFiles(final BuildIdentifier buildIdentifier, final String typeName) {
		return FilesUtil.getListOfFiles(getObjectsDirectoryForObjectType(buildIdentifier, typeName));
	}

	/**
	 * File to store short name aliases for file names for long object names.
	 */
	public File getLongObjectNamesIndexFile(final BuildIdentifier buildIdentifier) {
		return new File(docuFiles.getBuildDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName()),
				FILENAME_LONG_OBJECT_NAMES_INDEX);
	}

	/**
	 * Directory to store additional step navigation details inside
	 */
	public File getStepNavigationsDirectory(final ScenarioIdentifier scenarioIdentifier) {
		File stepsDir = docuFiles.getStepsDirectory(scenarioIdentifier.getBranchName(),
				scenarioIdentifier.getBuildName(), scenarioIdentifier.getFeatureName(),
				scenarioIdentifier.getScenarioName());
		return new File(stepsDir, "navigation.derived");
	}

	/**
	 * File to store navigation details of a step.
	 */
	public File getStepNavigationFile(final ScenarioIdentifier scenarioIdentifier, final int stepIndex) {
		File stepNavigationsDir = getStepNavigationsDirectory(scenarioIdentifier);
		return new File(stepNavigationsDir, THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".navigation.xml");
	}


	public File getBuildDirectory(final BuildIdentifier buildIdentifier) {
		return docuFiles.getBuildDirectory(buildIdentifier.getBranchName(), buildIdentifier.getBuildName());
	}

}
