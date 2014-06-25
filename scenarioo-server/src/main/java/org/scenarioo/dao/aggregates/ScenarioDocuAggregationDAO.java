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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.business.aggregator.ScenarioDocuAggregator;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummaries;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.utils.ResourceUtils;

/**
 * DAO for accessing user scenario docu content from filesystem, that is either generated or already precalculated.
 * 
 * The DAO should in general only access data by reading one file and should not have to calculate additional data or
 * read a lot of different files or even strip unwanted data.
 * 
 * Data that is not available directly from a file should be precalculated in {@link ScenarioDocuAggregator} to make it
 * easily available for DAO.
 * 
 * If accessing data for a specific build you have to make sure to use the constructor that initializes the
 * {@link LongObjectNamesResolver} with the object names as available for the specific build you want to access.
 */
public class ScenarioDocuAggregationDAO {
	
	private static final Logger LOGGER = Logger.getLogger(ScenarioDocuAggregationDAO.class);
	
	private static final String VERSION_PROPERTY_KEY = "scenarioo.derived.file.format.version";
	
	private final ScenarioDocuAggregationFiles files;
	
	private LongObjectNamesResolver longObjectNameResolver = null;
	
	public ScenarioDocuAggregationDAO(final File rootDirectory) {
		files = new ScenarioDocuAggregationFiles(rootDirectory);
	}
	
	public ScenarioDocuAggregationDAO(final File rootDirectory, final LongObjectNamesResolver longObjectNameResolver) {
		files = new ScenarioDocuAggregationFiles(rootDirectory);
		this.longObjectNameResolver = longObjectNameResolver;
	}
	
	public String loadVersion(final String branchName, final String buildName) {
		File versionFile = files.getVersionFile(branchName, buildName);
		if (versionFile.exists()) {
			Properties properties = new Properties();
			FileReader reader = null;
			try {
				reader = new FileReader(versionFile);
				properties.load(reader);
				return properties.getProperty(VERSION_PROPERTY_KEY);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("file not found: "
						+ versionFile.getAbsolutePath(), e);
			} catch (IOException e) {
				throw new RuntimeException("file not readable: "
						+ versionFile.getAbsolutePath(), e);
			} finally {
				ResourceUtils.close(reader, versionFile.getAbsolutePath());
			}
		} else {
			return "";
		}
	}
	
	public List<UseCaseScenarios> loadUseCaseScenariosList(final String branchName, final String buildName) {
		File file = files.getUseCasesAndScenariosFile(branchName, buildName);
		UseCaseScenariosList list = ScenarioDocuXMLFileUtil.unmarshal(UseCaseScenariosList.class, file);
		return list.getUseCaseScenarios();
	}
	
	public UseCaseScenarios loadUseCaseScenarios(final String branchName, final String buildName,
			final String usecaseName) {
		File scenariosFile = files.getUseCaseScenariosFile(branchName, buildName, usecaseName);
		return ScenarioDocuXMLFileUtil.unmarshal(UseCaseScenarios.class, scenariosFile);
	}
	
	public ScenarioPageSteps loadScenarioPageSteps(final String branchName, final String buildName,
			final String usecaseName, final String scenarioName) {
		File file = files.getScenarioStepsFile(branchName, buildName, usecaseName, scenarioName);
		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioPageSteps.class, file);
	}
	
	public void saveVersion(final String branchName, final String buildName, final String currentFileFormatVersion) {
		File versionFile = files.getVersionFile(branchName, buildName);
		Properties versionProperties = new Properties();
		versionProperties.setProperty(VERSION_PROPERTY_KEY, currentFileFormatVersion);
		saveProperties(versionFile, versionProperties, "Scenarioo derived files format version");
	}
	
	private void saveProperties(final File file, final Properties properties, final String comment) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			properties.store(fileWriter, comment);
		} catch (IOException e) {
			throw new RuntimeException("could not write " + file.getAbsolutePath(), e);
		} finally {
			ResourceUtils.close(fileWriter, file.getAbsolutePath());
		}
	}
	
	public void saveUseCaseScenariosList(final String branchName, final String buildName,
			final UseCaseScenariosList useCaseScenariosList) {
		File file = files.getUseCasesAndScenariosFile(branchName, buildName);
		ScenarioDocuXMLFileUtil.marshal(useCaseScenariosList, file);
	}
	
	public void saveUseCaseScenarios(final String branchName, final String buildName,
			final UseCaseScenarios useCaseScenarios) {
		File scenariosFile = files.getUseCaseScenariosFile(branchName, buildName, useCaseScenarios
				.getUseCase().getName());
		ScenarioDocuXMLFileUtil.marshal(useCaseScenarios, scenariosFile);
	}
	
	public void saveScenarioPageSteps(final String branchName, final String buildName,
			final ScenarioPageSteps scenarioPageSteps) {
		String usecaseName = scenarioPageSteps.getUseCase().getName();
		String scenarioName = scenarioPageSteps.getScenario().getName();
		File file = files.getScenarioStepsFile(branchName, buildName, usecaseName, scenarioName);
		ScenarioDocuXMLFileUtil.marshal(scenarioPageSteps, file);
	}
	
	public boolean isObjectDescriptionSaved(final String branchName, final String buildName,
			final ObjectDescription objectDescription) {
		return isObjectDescriptionSaved(branchName, buildName, objectDescription.getType(),
				resolveObjectFileName(objectDescription.getName()));
	}
	
	public boolean isObjectDescriptionSaved(final String branchName, final String buildName, final String type,
			final String name) {
		File objectFile = files.getObjectFile(branchName, buildName, type, resolveObjectFileName(name));
		return objectFile.exists();
	}
	
	public void saveObjectDescription(final String branchName, final String buildName,
			final ObjectDescription objectDescription) {
		File objectFile = files.getObjectFile(branchName, buildName, objectDescription.getType(),
				resolveObjectFileName(objectDescription.getName()));
		objectFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(objectDescription, objectFile);
	}
	
	public ObjectDescription loadObjectDescription(final String branchName, final String buildName,
			final ObjectReference objectRef) {
		File objectFile = files.getObjectFile(branchName, buildName, objectRef.getType(),
				resolveObjectFileName(objectRef.getName()));
		return loadObjectDescription(objectFile);
	}
	
	public ObjectDescription loadObjectDescription(final File file) {
		return ScenarioDocuXMLFileUtil.unmarshal(ObjectDescription.class, file);
	}
	
	public void saveObjectIndex(final String branchName, final String buildName, final ObjectIndex objectIndex) {
		File objectFile = files.getObjectIndexFile(branchName, buildName, objectIndex.getObject().getType(),
				resolveObjectFileName(objectIndex.getObject().getName()));
		objectFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(objectIndex, objectFile);
	}
	
	/**
	 * @param resolvedObjectName
	 *            Object name, if too long, shortened using {@link LongObjectNamesResolver}
	 */
	public ObjectIndex loadObjectIndex(final String branchName, final String buildName,
			final String objectType, final String objectName) {
		String objectFileName = resolveObjectFileName(objectName);
		File objectFile = files.getObjectIndexFile(branchName, buildName, objectType, objectFileName);
		return ScenarioDocuXMLFileUtil.unmarshal(ObjectIndex.class, objectFile);
	}
	
	@SuppressWarnings("unchecked")
	public ObjectList<ObjectDescription> loadObjectsList(final String branchName, final String buildName,
			final String type) {
		File objectListFile = files.getObjectListFile(branchName, buildName, type);
		return ScenarioDocuXMLFileUtil.unmarshal(ObjectList.class, objectListFile);
	}
	
	public void saveObjectsList(final String branchName, final String buildName, final String type,
			final ObjectList<ObjectDescription> objectList) {
		File objectListFile = files.getObjectListFile(branchName, buildName, type);
		ScenarioDocuXMLFileUtil.marshal(objectList, objectListFile);
	}
	
	public ScenarioDocuAggregationFiles getFiles() {
		return files;
	}
	
	private String resolveObjectFileName(final String objectName) {
		if (longObjectNameResolver == null) {
			throw new IllegalStateException(
					"Not allowed to access objects without having LongObjectNameResolver initialized properly on the DAO. Please use ScenarioDocuAggregationDAO constructor with addtional parameter to pass a LongObjectNamesResolver for current build that you try to access.");
		}
		return longObjectNameResolver.resolveObjectFileName(objectName);
	}
	
	public ObjectIndex loadObjectIndexIfExistant(final String branchName, final String buildName,
			final String objectType, final String objectName) {
		String objectFileName = resolveObjectFileName(objectName);
		File objectFile = files.getObjectIndexFile(branchName, buildName, objectType, objectFileName);
		if (objectFile.exists()) {
			return loadObjectIndex(branchName, buildName, objectType, objectName);
		}
		else {
			return null;
		}
	}
	
	public List<BuildImportSummary> loadBuildImportSummaries() {
		File buildImportSummariesFile = files.getBuildStatesFile();
		if (!buildImportSummariesFile.exists()) {
			return new ArrayList<BuildImportSummary>();
		}
		else {
			try {
				BuildImportSummaries summaries = ScenarioDocuXMLFileUtil.unmarshal(BuildImportSummaries.class,
						buildImportSummariesFile);
				return summaries.getBuildSummaries();
			} catch (Exception e) {
				LOGGER.error(
						"Failed to load saved build import states, the system is recovering by recreating the list from file system.",
						e);
				return new ArrayList<BuildImportSummary>();
			}
		}
	}
	
	public void saveBuildImportSummaries(final List<BuildImportSummary> summariesToSave) {
		BuildImportSummaries summaries = new BuildImportSummaries(summariesToSave);
		ScenarioDocuXMLFileUtil.marshal(summaries, files.getBuildStatesFile());
	}
	
	public void saveLongObjectNamesIndex(final String branchName, final String buildName,
			final LongObjectNamesResolver longObjectNamesResolver) {
		File longObjectNamesFile = files.getLongObjectNamesIndexFile(branchName, buildName);
		ScenarioDocuXMLFileUtil.marshal(longObjectNamesResolver, longObjectNamesFile);
	}
	
	public LongObjectNamesResolver loadLongObjectNamesIndex(final String branchName, final String buildName) {
		File longObjectNamesFile = files.getLongObjectNamesIndexFile(branchName, buildName);
		return ScenarioDocuXMLFileUtil.unmarshal(LongObjectNamesResolver.class, longObjectNamesFile);
	}
	
	public File getBuildImportLogFile(final String branchName, final String buildName) {
		return files.getBuildImportLogFile(branchName, buildName);
	}
	
	public void saveStepNavigation(final BuildIdentifier build, final StepLink stepLink,
			final StepNavigation stepNavigation) {
		File stepNavigationFile = files.getStepNavigationFile(build, stepLink.getUseCaseName(),
				stepLink.getScenarioName(), stepLink.getStepIndex());
		stepNavigationFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(stepNavigation, stepNavigationFile);
	}
	
	public StepNavigation loadStepNavigation(final BuildIdentifier build, final StepLink step) {
		return loadStepNavigation(build, step.getUseCaseName(), step.getScenarioName(), step.getStepIndex());
	}
	
	public StepNavigation loadStepNavigation(final BuildIdentifier build, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		File stepNavigationFile = files.getStepNavigationFile(build, useCaseName, scenarioName, stepIndex);
		return ScenarioDocuXMLFileUtil.unmarshal(StepNavigation.class, stepNavigationFile);
	}
	
	/**
	 * Delete the most important derived files, such that the build is considered as unprocessed again.
	 */
	public void deleteDerivedFiles(final String branchName, final String buildName) {
		File versionFile = files.getVersionFile(branchName, buildName);
		versionFile.delete();
		File logFile = getBuildImportLogFile(branchName, buildName);
		logFile.delete();
		File longObjectNamesFile = files.getLongObjectNamesIndexFile(branchName, buildName);
		longObjectNamesFile.delete();
	}
	
}
