package ngusd.aggregator;

import java.io.File;

import ngusd.api.files.ScenarioDocuFiles;

/**
 * Defines locations of aggregated files containing aggregated (=derived) data from documentation input data.
 */
public class ScenarioDocuAggregatorFiles {
	
	public static final String FILENAME_VERSION_PROPERTIES = "version.derived.properties";
	public static final String FILENAME_USECASES_XML = "usecases.derived.xml";
	public static final String FILENAME_SCENARIOS_XML = "scenarios.derived.xml";
	public static final String FILENAME_SCENARIO_PAGE_STEPS_XML = "scenarioPageSteps.derived.xml";
	public static final String FILENAME_PAGE_VARIANT_COUNTERS_XML = "pageVariantCounters.derived.xml";
	
	private ScenarioDocuFiles docuFiles;
	
	public ScenarioDocuAggregatorFiles(final File rootDirectory) {
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
}
