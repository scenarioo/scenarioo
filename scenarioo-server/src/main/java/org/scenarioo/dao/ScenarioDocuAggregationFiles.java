package org.scenarioo.dao;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;

/**
 * Defines locations of aggregated files containing aggregated (=derived) data from documentation input data.
 */
public class ScenarioDocuAggregationFiles {
	
	public static final String DIRECTORY_NAME_OBJECTS = "objects.derived";
	public static final String FILENAME_VERSION_PROPERTIES = "version.derived.properties";
	public static final String FILENAME_USECASES_XML = "usecases.derived.xml";
	public static final String FILENAME_SCENARIOS_XML = "scenarios.derived.xml";
	public static final String FILENAME_SCENARIO_PAGE_STEPS_XML = "scenarioPageSteps.derived.xml";
	public static final String FILENAME_PAGE_VARIANT_COUNTERS_XML = "pageVariantCounters.derived.xml";
	
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
		return new File(getObjectsDirectory(branchName, buildName), typeName);
	}
	
	public File getObjectsIndexDirectoryForObjectType(final String branchName, final String buildName,
			final String typeName) {
		return new File(getObjectsDirectoryForObjectType(branchName, buildName, typeName), "index");
	}
	
	public File getObjectFile(final String branchName, final String buildName, final ObjectDescription objectDescription) {
		return getObjectFile(branchName, buildName, objectDescription.getType(), objectDescription.getName());
	}
	
	public File getObjectFile(final String branchName, final String buildName, final String objectType,
			final String objectName) {
		File objectsDir = getObjectsDirectoryForObjectType(branchName, buildName, objectType);
		return new File(objectsDir, encodeName(objectName) + ".description.xml");
	}
	
	public File getObjectFile(final String branchName, final String buildName, final ObjectReference objectRef) {
		return getObjectFile(branchName, buildName, objectRef.getType(), objectRef.getName());
	}
	
	public File getObjectIndexFile(final String branchName, final String buildName, final String type, final String name) {
		File objectsDir = getObjectsIndexDirectoryForObjectType(branchName, buildName, type);
		return new File(objectsDir, encodeName(name) + ".index.xml");
	}
	
	/**
	 * TODO: make sure this helper method is used for encoding all file names (in case of strange characters inside,
	 * also use it in API!).
	 */
	private String encodeName(final String name) {
		try {
			return URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"Unsupported UTF-8 charset. Scenarioo needs to run on a JVM or server environment that supports this.",
					e);
		}
	}
	
}
