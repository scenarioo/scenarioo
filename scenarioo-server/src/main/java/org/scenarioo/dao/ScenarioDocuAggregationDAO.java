package org.scenarioo.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.scenarioo.aggregator.ScenarioDocuAggregator;
import org.scenarioo.api.util.files.XMLFileUtil;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.usecases.PageVariantsCounter;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;

/**
 * DAO for accessing user scenario docu content from filesystem, that is either generated or already precalculated.
 * 
 * The DAO should in general only access data by reading one file and should not have to calculate additional data or
 * read a lot of different files or even strip unwanted data.
 * 
 * Data that is not available directly from a file should be precalculated in {@link ScenarioDocuAggregator} to make it
 * easily available for DAO.
 */
public class ScenarioDocuAggregationDAO {
	
	private static final String VERSION_PROPERTY_KEY = "scenarioo.derived.file.format.version";
	
	private final ScenarioDocuAggregationFiles files;
	
	public ScenarioDocuAggregationDAO(final File rootDirectory) {
		files = new ScenarioDocuAggregationFiles(rootDirectory);
	}
	
	public String loadVersion(final String branchName, final String buildName) {
		File versionFile = files.getVersionFile(branchName, buildName);
		if (versionFile.exists()) {
			Properties properties = new Properties();
			try {
				properties.load(new FileReader(versionFile));
				return properties.getProperty(VERSION_PROPERTY_KEY);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("file not found: "
						+ versionFile.getAbsolutePath(), e);
			} catch (IOException e) {
				throw new RuntimeException("file not readable: "
						+ versionFile.getAbsolutePath(), e);
			}
		} else {
			return "";
		}
	}
	
	public List<UseCaseScenarios> loadUseCaseScenariosList(final String branchName, final String buildName) {
		File file = files.getUseCasesAndScenariosFile(branchName, buildName);
		UseCaseScenariosList list = XMLFileUtil.unmarshal(file, UseCaseScenariosList.class);
		return list.getUseCaseScenarios();
	}
	
	public UseCaseScenarios loadUseCaseScenarios(final String branchName, final String buildName,
			final String usecaseName) {
		File scenariosFile = files.getUseCaseScenariosFile(branchName, buildName, usecaseName);
		return XMLFileUtil.unmarshal(scenariosFile, UseCaseScenarios.class);
	}
	
	public ScenarioPageSteps loadScenarioPageSteps(final String branchName, final String buildName,
			final String usecaseName, final String scenarioName) {
		File file = files.getScenarioStepsFile(branchName, buildName, usecaseName, scenarioName);
		return XMLFileUtil.unmarshal(file, ScenarioPageSteps.class);
	}
	
	public PageVariantsCounter loadPageVariantsCounter(final String branchName, final String buildName) {
		File file = files.getPageVariantsFile(branchName, buildName);
		return XMLFileUtil.unmarshal(file, PageVariantsCounter.class);
	}
	
	public void saveVersion(final String branchName, final String buildName, final String currentFileFormatVersion) {
		File versionFile = files.getVersionFile(branchName, buildName);
		Properties versionProperties = new Properties();
		versionProperties.setProperty(VERSION_PROPERTY_KEY, currentFileFormatVersion);
		saveProperties(versionFile, versionProperties, "Scenarioo derived files format version");
	}
	
	private void saveProperties(final File file, final Properties properties, final String comment) {
		try {
			properties.store(new FileWriter(file), comment);
		} catch (IOException e) {
			throw new RuntimeException("could not write " + file.getAbsolutePath(), e);
		}
	}
	
	public void savePageVariants(final String branchName, final String buildName,
			final PageVariantsCounter pageVariantsCounter) {
		File fileCounter = files.getPageVariantsFile(branchName, buildName);
		XMLFileUtil.marshal(pageVariantsCounter, fileCounter);
	}
	
	public void saveUseCaseScenariosList(final String branchName, final String buildName,
			final UseCaseScenariosList useCaseScenariosList) {
		File file = files.getUseCasesAndScenariosFile(branchName, buildName);
		XMLFileUtil.marshal(useCaseScenariosList, file);
	}
	
	public void saveUseCaseScenarios(final String branchName, final String buildName,
			final UseCaseScenarios useCaseScenarios) {
		File scenariosFile = files.getUseCaseScenariosFile(branchName, buildName, useCaseScenarios
				.getUseCase().getName());
		XMLFileUtil.marshal(useCaseScenarios, scenariosFile);
	}
	
	public void saveScenarioPageSteps(final String branchName, final String buildName,
			final ScenarioPageSteps scenarioPageSteps) {
		String usecaseName = scenarioPageSteps.getUseCase().getName();
		String scenarioName = scenarioPageSteps.getScenario().getName();
		File file = files.getScenarioStepsFile(branchName, buildName, usecaseName, scenarioName);
		XMLFileUtil.marshal(scenarioPageSteps, file);
	}
	
	public boolean isObjectDescriptionSaved(final String branchName, final String buildName,
			final ObjectDescription objectDescription) {
		return isObjectDescriptionSaved(branchName, buildName, objectDescription.getType(), objectDescription.getName());
	}
	
	public boolean isObjectDescriptionSaved(final String branchName, final String buildName, final String type,
			final String name) {
		File objectFile = files.getObjectFile(branchName, buildName, type, name);
		return objectFile.exists();
	}
	
	public void saveObjectDescription(final String branchName, final String buildName,
			final ObjectDescription objectDescription) {
		File objectFile = files.getObjectFile(branchName, buildName, objectDescription);
		objectFile.getParentFile().mkdirs();
		XMLFileUtil.marshal(objectDescription, objectFile);
	}
	
	public ObjectDescription loadObjectDescription(final String branchName, final String buildName,
			final ObjectReference objectRef) {
		File objectFile = files.getObjectFile(branchName, buildName, objectRef);
		return XMLFileUtil.unmarshal(objectFile, ObjectDescription.class);
	}
	
	public void saveObjectIndex(final String branchName, final String buildName, final ObjectIndex objectIndex) {
		File objectFile = files.getObjectIndexFile(branchName, buildName, objectIndex.getObject().getType(),
				objectIndex.getObject().getName());
		objectFile.getParentFile().mkdirs();
		XMLFileUtil.marshal(objectIndex, objectFile);
	}
	
	public ObjectIndex loadObjectIndex(final String branchName, final String buildName,
			final String objectType, final String objectName) {
		File objectFile = files.getObjectIndexFile(branchName, buildName, objectType, objectName);
		return XMLFileUtil.unmarshal(objectFile, ObjectIndex.class);
	}
	
	public ScenarioDocuAggregationFiles getFiles() {
		return files;
	}
	
}
