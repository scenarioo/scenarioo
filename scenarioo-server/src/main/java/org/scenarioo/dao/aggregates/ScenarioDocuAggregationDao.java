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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.business.aggregator.ScenarioDocuAggregator;
import org.scenarioo.business.builds.BuildLink;
import org.scenarioo.business.lastSuccessfulScenarios.LastSuccessfulScenariosBuildUpdater;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummaries;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
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
public class ScenarioDocuAggregationDao implements AggregatedDocuDataReader {
	
	private static final Logger LOGGER = Logger.getLogger(ScenarioDocuAggregationDao.class);
	
	private static final String VERSION_PROPERTY_KEY = "scenarioo.derived.file.format.version";
	
	final File rootDirectory;
	private final ScenarioDocuAggregationFiles files;
	private final ScenarioDocuReader scenarioDocuReader;
	
	private LongObjectNamesResolver longObjectNameResolver = null;
	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public ScenarioDocuAggregationDao(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
		files = new ScenarioDocuAggregationFiles(rootDirectory);
		scenarioDocuReader = new ScenarioDocuReader(rootDirectory);
	}
	
	public ScenarioDocuAggregationDao(final File rootDirectory, final LongObjectNamesResolver longObjectNameResolver) {
		this(rootDirectory);
		this.longObjectNameResolver = longObjectNameResolver;
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadVersion(java.lang.String, java.lang.String)
	 */
	@Override
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
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadUseCaseScenariosList(org.scenarioo.rest.base.BuildIdentifier)
	 */
	@Override
	public List<UseCaseScenarios> loadUseCaseScenariosList(final BuildIdentifier buildIdentifier) {
		File file = files.getUseCasesAndScenariosFile(buildIdentifier);
		UseCaseScenariosList list = ScenarioDocuXMLFileUtil.unmarshal(UseCaseScenariosList.class, file);
		return list.getUseCaseScenarios();
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadUseCaseScenarios(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public UseCaseScenarios loadUseCaseScenarios(final BuildIdentifier buildIdentifier, final String useCaseName) {
		File scenariosFile = files.getUseCaseScenariosFile(buildIdentifier, useCaseName);
		UseCaseScenarios useCaseWithScenarios = ScenarioDocuXMLFileUtil
				.unmarshal(UseCaseScenarios.class, scenariosFile);
		enrichWithBuildDatesIfThisIsTheLastSuccessfulScenariosBuild(buildIdentifier, useCaseName, useCaseWithScenarios);
		return useCaseWithScenarios;
	}
	
	private void enrichWithBuildDatesIfThisIsTheLastSuccessfulScenariosBuild(final BuildIdentifier buildIdentifier,
			final String useCaseName, final UseCaseScenarios useCaseWithScenarios) {
		if (!LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME.equals(buildIdentifier
				.getBuildName())) {
			return;
		}
		LastSuccessfulScenariosIndex index = LastSuccessfulScenariosIndexDao.loadLastSuccessfulScenariosIndex(
				files.getRootDirectory(), buildIdentifier.getBranchName());
		
		Date latestImportedBuildDate = index.getLatestImportedBuildDate();
		
		for (ScenarioSummary scenario : useCaseWithScenarios.getScenarios()) {
			// TODO Add the "old build date" to the ScenarioSummary on aggregation time and as a separate field.
			Date buildDate = index.getBuildDateForScenario(useCaseName, scenario.getScenario().getName());
			if (buildDate == null || buildDate.equals(latestImportedBuildDate)) {
				continue;
			}
			scenario.getScenario().setDescription(
					"Scenario is from an old build (" + dateFormatter.format(buildDate) + ")! "
							+ scenario.getScenario().getDescription());
		}
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadScenarioPageSteps(org.scenarioo.rest.base.ScenarioIdentifier)
	 */
	@Override
	public ScenarioPageSteps loadScenarioPageSteps(final ScenarioIdentifier scenarioIdentifier) {
		File file = files.getScenarioStepsFile(scenarioIdentifier);
		try {
			return ScenarioDocuXMLFileUtil.unmarshal(ScenarioPageSteps.class, file);
		} catch (ResourceNotFoundException e) {
			return null;
		}
	}
	
	public void saveVersion(final BuildIdentifier buildIdentifier, final String currentFileFormatVersion) {
		File versionFile = files.getVersionFile(buildIdentifier);
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

	public void saveUseCaseScenariosList(final BuildIdentifier buildIdentifier,
			final UseCaseScenariosList useCaseScenariosList) {
		File file = files.getUseCasesAndScenariosFile(buildIdentifier);
		ScenarioDocuXMLFileUtil.marshal(useCaseScenariosList, file);
	}
	
	public void saveUseCaseScenarios(final BuildIdentifier buildIdentifier, final UseCaseScenarios useCaseScenarios) {
		File scenariosFile = files.getUseCaseScenariosFile(buildIdentifier, useCaseScenarios.getUseCase().getName());
		ScenarioDocuXMLFileUtil.marshal(useCaseScenarios, scenariosFile);
	}
	
	public void saveScenarioPageSteps(final BuildIdentifier buildIdentifier, final ScenarioPageSteps scenarioPageSteps) {
		String usecaseName = scenarioPageSteps.getUseCase().getName();
		String scenarioName = scenarioPageSteps.getScenario().getName();
		ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName);
		File file = files.getScenarioStepsFile(scenarioIdentifier);
		ScenarioDocuXMLFileUtil.marshal(scenarioPageSteps, file);
	}
	
	public boolean isObjectDescriptionSaved(final BuildIdentifier buildIdentifier,
			final ObjectDescription objectDescription) {
		return isObjectDescriptionSaved(buildIdentifier, objectDescription.getType(),
				resolveObjectFileName(objectDescription.getName()));
	}
	
	public boolean isObjectDescriptionSaved(final BuildIdentifier buildIdentifier, final String type, final String name) {
		File objectFile = files.getObjectFile(buildIdentifier, type, resolveObjectFileName(name));
		return objectFile.exists();
	}
	
	public void saveObjectDescription(final BuildIdentifier buildIdentifier, final ObjectDescription objectDescription) {
		File objectFile = files.getObjectFile(buildIdentifier, objectDescription.getType(),
				resolveObjectFileName(objectDescription.getName()));
		objectFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(objectDescription, objectFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadObjectDescription(org.scenarioo.rest.base.BuildIdentifier,
	 *      org.scenarioo.model.docu.entities.generic.ObjectReference)
	 */
	@Override
	public ObjectDescription loadObjectDescription(final BuildIdentifier buildIdentifier,
			final ObjectReference objectRef) {
		File objectFile = files.getObjectFile(buildIdentifier, objectRef.getType(),
				resolveObjectFileName(objectRef.getName()));
		return loadObjectDescription(objectFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadObjectDescription(java.io.File)
	 */
	@Override
	public ObjectDescription loadObjectDescription(final File file) {
		return ScenarioDocuXMLFileUtil.unmarshal(ObjectDescription.class, file);
	}
	
	public void saveObjectIndex(final BuildIdentifier buildIdentifier, final ObjectIndex objectIndex) {
		File objectFile = files.getObjectIndexFile(buildIdentifier, objectIndex.getObject().getType(),
				resolveObjectFileName(objectIndex.getObject().getName()));
		objectFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(objectIndex, objectFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadObjectIndex(org.scenarioo.rest.base.BuildIdentifier,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public ObjectIndex loadObjectIndex(final BuildIdentifier buildIdentifier, final String objectType,
			final String objectName) {
		String objectFileName = resolveObjectFileName(objectName);
		File objectFile = files.getObjectIndexFile(buildIdentifier, objectType, objectFileName);
		return ScenarioDocuXMLFileUtil.unmarshal(ObjectIndex.class, objectFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadObjectsList(org.scenarioo.rest.base.BuildIdentifier,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ObjectList<ObjectDescription> loadObjectsList(final BuildIdentifier buildIdentifier, final String type) {
		File objectListFile = files.getObjectListFile(buildIdentifier, type);
		return ScenarioDocuXMLFileUtil.unmarshal(ObjectList.class, objectListFile);
	}
	
	public void saveObjectsList(final BuildIdentifier buildIdentifier, final String type,
			final ObjectList<ObjectDescription> objectList) {
		File objectListFile = files.getObjectListFile(buildIdentifier, type);
		ScenarioDocuXMLFileUtil.marshal(objectList, objectListFile);
	}
	
	public void saveCustomObjectTabTree(final BuildIdentifier buildIdentifier, final String tabId,
			final CustomObjectTabTree tree) {
		File customObjectTabTreeFile = files.getCustomObjectTabTreeFile(buildIdentifier, tabId);
		customObjectTabTreeFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(tree, customObjectTabTreeFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadCustomObjectTabTree(org.scenarioo.rest.base.BuildIdentifier,
	 *      java.lang.String)
	 */
	@Override
	public CustomObjectTabTree loadCustomObjectTabTree(final BuildIdentifier buildIdentifier, final String tabId) {
		File customObjectTabTreeFile = files.getCustomObjectTabTreeFile(buildIdentifier, tabId);
		return ScenarioDocuXMLFileUtil.unmarshal(CustomObjectTabTree.class, customObjectTabTreeFile);
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
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadObjectIndexIfExistant(org.scenarioo.rest.base.BuildIdentifier,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public ObjectIndex loadObjectIndexIfExistant(final BuildIdentifier buildIdentifier, final String objectType,
			final String objectName) {
		String objectFileName = resolveObjectFileName(objectName);
		File objectFile = files.getObjectIndexFile(buildIdentifier, objectType, objectFileName);
		if (objectFile.exists()) {
			return loadObjectIndex(buildIdentifier, objectType, objectName);
		} else {
			return null;
		}
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadBuildImportSummaries()
	 */
	@Override
	public List<BuildImportSummary> loadBuildImportSummaries() {
		File buildImportSummariesFile = files.getBuildStatesFile();
		if (!buildImportSummariesFile.exists()) {
			return new ArrayList<BuildImportSummary>();
		} else {
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
	
	public void saveLongObjectNamesIndex(final BuildIdentifier buildIdentifier,
			final LongObjectNamesResolver longObjectNamesResolver) {
		File longObjectNamesFile = files.getLongObjectNamesIndexFile(buildIdentifier);
		ScenarioDocuXMLFileUtil.marshal(longObjectNamesResolver, longObjectNamesFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadLongObjectNamesIndex(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public LongObjectNamesResolver loadLongObjectNamesIndex(final BuildIdentifier buildIdentifier) {
		File longObjectNamesFile = files.getLongObjectNamesIndexFile(buildIdentifier);
		return ScenarioDocuXMLFileUtil.unmarshal(LongObjectNamesResolver.class, longObjectNamesFile);
	}
	
	public File getBuildImportLogFile(final BuildIdentifier buildIdentifier) {
		return files.getBuildImportLogFile(buildIdentifier);
	}
	
	public void saveStepNavigation(final BuildIdentifier buildIdentifier, final StepLink stepLink,
			final StepNavigation stepNavigation) {
		File stepNavigationFile = files.getStepNavigationFile(
				ScenarioIdentifier.fromStepLink(buildIdentifier, stepLink), stepLink.getStepIndex());
		stepNavigationFile.getParentFile().mkdirs();
		ScenarioDocuXMLFileUtil.marshal(stepNavigation, stepNavigationFile);
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadStepNavigation(org.scenarioo.rest.base.BuildIdentifier,
	 *      org.scenarioo.model.docu.aggregates.steps.StepLink)
	 */
	@Override
	public StepNavigation loadStepNavigation(final BuildIdentifier build, final StepLink step) {
		return loadStepNavigation(ScenarioIdentifier.fromStepLink(build, step), step.getStepIndex());
	}
	
	/**
	 * @see org.scenarioo.dao.aggregates.AggregatedDocuDataReader#loadStepNavigation(org.scenarioo.rest.base.BuildIdentifier,
	 *      java.lang.String, java.lang.String, int)
	 */
	@Override
	public StepNavigation loadStepNavigation(final ScenarioIdentifier scenarioIdentifier, final int stepIndex) {
		File stepNavigationFile = files.getStepNavigationFile(scenarioIdentifier, stepIndex);
		return ScenarioDocuXMLFileUtil.unmarshal(StepNavigation.class, stepNavigationFile);
	}
	
	/**
	 * Delete the most important derived files, such that the build is considered as unprocessed again.
	 */
	public void deleteDerivedFiles(final BuildIdentifier buildIdentifier) {
		File versionFile = files.getVersionFile(buildIdentifier);
		versionFile.delete();
		File logFile = getBuildImportLogFile(buildIdentifier);
		logFile.delete();
		File longObjectNamesFile = files.getLongObjectNamesIndexFile(buildIdentifier);
		longObjectNamesFile.delete();
	}
	
	@Override
	public List<BuildLink> loadBuildLinks(final String branchName) {
		List<ObjectFromDirectory<Build>> builds = scenarioDocuReader.loadBuilds(branchName);
		
		List<BuildLink> result = new ArrayList<BuildLink>();
		for (ObjectFromDirectory<Build> build : builds) {
			BuildLink link = new BuildLink(build.getObject(), FilesUtil.decodeName(build.getDirectoryName()));
			setSpecialDisplayNameForLastSuccessfulScenariosBuild(link);
			result.add(link);
		}
		
		return result;
	}
	
	private void setSpecialDisplayNameForLastSuccessfulScenariosBuild(final BuildLink link) {
		if (LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME.equals(link.getBuild().getName())) {
			link.setDisplayName(LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_DISPLAY_NAME);
		}
	}

	public Build loadBuild(final BuildIdentifier buildIdentifier) {
		return scenarioDocuReader.loadBuild(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName());
	}

	public void saveBuild(final String branchName, final Build build) {
		ScenarioDocuWriter writer = new ScenarioDocuWriter(this.rootDirectory,
				branchName, build.getName());
		writer.saveBuildDescription(build);
	}
}
