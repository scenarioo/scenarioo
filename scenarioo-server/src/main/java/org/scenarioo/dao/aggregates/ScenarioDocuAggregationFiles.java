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
import java.util.List;

import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;

/**
 * Defines locations of aggregated files containing aggregated (=derived) data from documentation input data.
 */
public class ScenarioDocuAggregationFiles {
	
	private static final String DIRECTORY_NAME_OBJECT_INDEXES = "index";
	private static final String DIRECTORY_NAME_OBJECTS = "objects.derived";
	private static final String FILENAME_VERSION_PROPERTIES = "version.derived.properties";
	private static final String FILENAME_USECASES_XML = "usecases.derived.xml";
	private static final String FILENAME_SCENARIOS_XML = "scenarios.derived.xml";
	private static final String FILENAME_SCENARIO_PAGE_STEPS_XML = "scenarioPageSteps.derived.xml";
	private static final String FILENAME_PAGE_VARIANT_COUNTERS_XML = "pageVariantCounters.derived.xml";
	
	private ScenarioDocuFiles docuFiles;
	
	public ScenarioDocuAggregationFiles(final File rootDirectory) {
		docuFiles = new ScenarioDocuFiles(rootDirectory);
	}
	
	public File getVersionFile(final String branchName, final String buildName) {
		return new File(docuFiles.getBuildDirectory(branchName, buildName), FILENAME_VERSION_PROPERTIES);
	}
	
	public File getPageVariantsFile(final String branchName, final String buildName) {
		File buildDir = docuFiles.getBuildDirectory(branchName, buildName);
		return new File(buildDir, FILENAME_PAGE_VARIANT_COUNTERS_XML);
	}
	
	public File getUseCasesAndScenariosFile(final String branchName, final String buildName) {
		File buildDir = docuFiles.getBuildDirectory(branchName, buildName);
		return new File(buildDir, FILENAME_USECASES_XML);
	}
	
	public File getUseCaseScenariosFile(final String branchName, final String buildName, final String useCaseName) {
		File caseDir = docuFiles.getUseCaseDirectory(branchName, buildName, useCaseName);
		return new File(caseDir, FILENAME_SCENARIOS_XML);
	}
	
	public File getScenarioStepsFile(final String branchName, final String buildName, final String usecaseName,
			final String scenarioName) {
		File scenarioDir = docuFiles.getScenarioDirectory(branchName, buildName, usecaseName, scenarioName);
		return new File(scenarioDir, FILENAME_SCENARIO_PAGE_STEPS_XML);
	}
	
	public File getObjectsDirectory(final String branchName, final String buildName) {
		return new File(docuFiles.getBuildDirectory(branchName, buildName), DIRECTORY_NAME_OBJECTS);
	}
	
	public File getObjectsDirectoryForObjectType(final String branchName, final String buildName, final String typeName) {
		return new File(getObjectsDirectory(branchName, buildName), FilesUtil.encodeName(typeName));
	}
	
	public File getObjectsIndexDirectoryForObjectType(final String branchName, final String buildName,
			final String typeName) {
		return new File(getObjectsDirectoryForObjectType(branchName, buildName, typeName),
				DIRECTORY_NAME_OBJECT_INDEXES);
	}
	
	public File getObjectFile(final String branchName, final String buildName, final ObjectDescription objectDescription) {
		return getObjectFile(branchName, buildName, objectDescription.getType(), objectDescription.getName());
	}
	
	public File getObjectFile(final String branchName, final String buildName, final String objectType,
			final String objectName) {
		File objectsDir = getObjectsDirectoryForObjectType(branchName, buildName, objectType);
		return new File(objectsDir, FilesUtil.encodeName(objectName) + ".description.xml");
	}
	
	public File getObjectFile(final String branchName, final String buildName, final ObjectReference objectRef) {
		return getObjectFile(branchName, buildName, objectRef.getType(), objectRef.getName());
	}
	
	public File getObjectListFile(final String branchName, final String buildName, final String type) {
		File objectsDir = getObjectsDirectory(branchName, buildName);
		return new File(objectsDir, FilesUtil.encodeName(type) + ".list.xml");
	}
	
	public File getObjectIndexFile(final String branchName, final String buildName, final String type, final String name) {
		File objectsDir = getObjectsIndexDirectoryForObjectType(branchName, buildName, type);
		return new File(objectsDir, FilesUtil.encodeName(name) + ".index.xml");
	}
	
	public List<File> getObjectFiles(final String branchName, final String buildName, final String typeName) {
		return FilesUtil.getListOfFiles(getObjectsDirectoryForObjectType(branchName, buildName, typeName));
	}
	
}
