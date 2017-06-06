package org.scenarioo.business.lastSuccessfulScenarios;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.business.builds.AvailableBuildsList;
import org.scenarioo.business.builds.BuildImporter;
import org.scenarioo.dao.aggregates.LastSuccessfulScenariosIndexDao;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.*;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenario;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.TestFileUtils;

public class LastSuccessfulScenariosBuildTest {

	private static final String SOURCE_USE_CASE_DESCRIPTION = "feature from imported build";
	private static final String NOT_MODIFIED_USE_CASE_DESCRIPTION = "feature file was not modified";
	private static final String BUILD_DATE_FOR_TEST_KEY = "buildDateForTest";
	private static final String SCENARIO_SUCCESS = "a successful scenario";
	private static final String SCENARIO_FAILED = "a failed scenario";
	private static final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final String[] features = new String[] { "Log in", "Send message", "Sign out" };
	private final String[] scenarios = new String[] { "login successful", "login failed", "password forgotten" };
	private final Date DATE_NOW = new Date();
	private final Date DATE_YESTERDAY = getCalendarForNowMinusOneDay().getTime();
	private final Date DATE_TOMORROW = getCalendarForNowPlusOneDay().getTime();
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	private static final String FILE_NAME_FEATURE = "feature.xml";

	private LastSuccessfulScenariosBuild lastSuccessfulScenarioBuild;
	private ConfigurationRepository configurationRepository;
	private final File rootDirectory = new File("tmp");
	private BuildImportSummary buildImportSummary;

	@Before
	public void setupTest() {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(rootDirectory);
		configurationRepository = RepositoryLocator.INSTANCE.getConfigurationRepository();
		lastSuccessfulScenarioBuild = new LastSuccessfulScenariosBuild();
	}

	@Test
	public void theBuildSummaryParameterMustNotBeNull() {
		givenBuildImportSummaryIsNull();

		try {
			whenUpdatingLastSuccessfulScenarioBuild();
			fail();
		} catch (NullPointerException e) {
			assertEquals("summary must not be null", e.getMessage());
		}
	}

	@Test
	public void theIdentifierInTheSummaryParameterMustNotBeNull() {
		givenBuildImportSummaryWithNullIdentifier();

		try {
			whenUpdatingLastSuccessfulScenarioBuild();
			fail();
		} catch (NullPointerException e) {
			assertEquals("build identifier must not be null", e.getMessage());
		}
	}

	@Test
	public void ifTheImportedBuildDoesNotHaveSuccessfulImportStatusNothingIsDone() {
		givenLastSuccessfulScenarioBuildIsDisabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusFailed();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectLastSuccessfulScenarioBuildDirectoryExists();
	}

	@Test
	public void ifTheLastSuccessfulScenarioBuildIsDisabledInTheConfigAnExistingSuchBuildIsDeleted() {
		givenLastSuccessfulScenarioBuildIsDisabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectLastSuccessfulScenarioBuildDirectoryDoesNotExist();
		expectBranchDirectoryExists();
	}

	@Test
	public void ifTheLastSuccessfulScenarioFolderDoesNotExistYetItIsCreated() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderDoesNotExist();
		givenBuildImportSummaryWithStatusSuccess();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectLastSuccessfulScenarioBuildDirectoryExists();
	}

	@Test
	public void ifTheBuildXmlFileDoesNotExistItIsCreated() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectBuildXmlWithValidContentExists();
	}

	@Ignore@Test
	public void allFeaturesAreCopied() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasThreeFeatures();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectTheThreeFeaturesExistInTheLastSuccessfulScenarioBuild();
	}

	@Test
	public void theBuildIsNotUpdatedWithItself() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccessForLastSuccessfulScenarioBuild();

		whenUpdatingLastSuccessfulScenarioBuild();

		// TODO: assert this somehow...
		// expectImportIsNotStarted();
	}

	@Test
	public void onlySuccessfulScenariosAreCopied() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenBuildImportSummaryWithStatusSuccess();
		givenFeatureWithASuccessfulAndAFailedScenario();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectOnlytheSuccessfulScenarioIsCopied();
	}

	@Test
	public void onlyScenariosThatAreNewerThanTheExistingScenarioAreCopied() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenBuildImportSummaryWithStatusSuccess();
		givenLastSuccessfulScenarioBuildWithScenarioFromYesterdayNowAndTomorrow();
		givenImportedBuildHasThreeScenarios();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectOnlyTheNewerBuildWasOverwritten();
	}

	@Ignore@Test
	public void ifTheImportedFeatureIsTheLatestOneAllFeaturesThatDoNotExistAnymoreAreDeleted() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenBuildImportSummaryWithStatusSuccess();
		givenLatestBuildInLastSuccessfulScenariosBuildIsFromYesterdayAndContainsThreeFeatures();
		givenImportedBuildIsFromTodayAndFirstFeatureDoesNotExistAnymore();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectFirstFeatureDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex();
		expectLatestImportedBuildDateIsNow();
	}

	@Test
	public void ifTheImportedFeatureIsTheLatestOneAllScenariosThatDoNotExistAnymoreAreDeleted() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenBuildImportSummaryWithStatusSuccess();
		givenLatestBuildInLastSuccessfulScenariosBuildIsFromYesterdayAndContainsThreeFeatures();
		givenImportedBuildIsFromTodayAndFirstScenarioInFirstFeatureDoesNotExistAnymore();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectFirstScenarioInFirstFeatureDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex();
		expectLatestImportedBuildDateIsNow();
	}

	@Ignore@Test
	public void theFeatureXmlFileIsCopiedIfItDoesNotExistYet() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasOneFeatureWithAFeatureXmlFile();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectFeatureXmlFileWasCopied();
	}

	@Ignore@Test
	public void theFeatureXmlFileIsAlsoCopiedIfThereAreNoSuccessfulTestInTheFeature() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenLastSuccessfulBuildHasOneFeatureWithOneFailedScenarioAndAFeatureXmlFile();
		givenImportedBuildHasOneFeatureWithOneFailedScenarioAndAFeatureXmlFile();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectFeatureXmlFileWasCopied();
	}

	@Ignore@Test
	public void theStatusInTheCopiedFeatureXmlFileIsAlwaysSetToSuccess() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasOneFeatureWithAFeatureXmlFileWithStatusFailed();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectCopiedFeatureXmlFileHasStatusSuccess();
	}

	@Ignore@Test
	public void theFeatureXmlOfTheLatestBuildIsUsed() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenLastSuccessfulScenarioBuildWithFeatureFromYesterdayNowAndTomorrow();
		givenImportedBuildHasThreeFeaturesWithOneScenarioEach();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectFeatureXmlFileWasCopiedForTheFirstTwoFeatures();
	}

	@Test
	public void derivedFilesAndFoldersAreNotCopied() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasDerivedFilesAndFoldersOnAllLevelsPlusOneScenario();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectDerivedFilesAndDirectoriesDoNotExistInLastSuccessfulScenariosBuildAndScenarioWasCopied();
	}

	@Test
	public void scenariosWithoutScenarioXmlFileAreTreatedAsIfTheyDidNotExist() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasAScenarioWihoutScenarioXmlFile();

		whenUpdatingLastSuccessfulScenarioBuild();

		expectOnlyTheSecondAndThirdScenarioAreAddedToTheLastSuccessfulBuild();
	}

	private void givenBuildImportSummaryIsNull() {
		buildImportSummary = null;
	}

	private void givenBuildImportSummaryWithNullIdentifier() {
		buildImportSummary = new BuildImportSummary();
		buildImportSummary.setIdentifier(null);
	}

	private void givenBuildImportSummaryWithStatusFailed() {
		buildImportSummary = getSummaryWith(BuildImportStatus.FAILED);
	}

	private void givenBuildImportSummaryWithStatusSuccess() {
		buildImportSummary = getSummaryWith(BuildImportStatus.SUCCESS);
		buildImportSummary.setIdentifier(BUILD_IDENTIFIER);
		buildImportSummary.setBuildDescription(createBuildDescription());
	}

	private Build createBuildDescription() {
		Build build = new Build();
		build.setDate(DATE_NOW);
		return build;
	}

	private void givenBuildImportSummaryWithStatusSuccessForLastSuccessfulScenarioBuild() {
		givenBuildImportSummaryWithStatusSuccess();
		buildImportSummary.getIdentifier().setBuildName(
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
	}

	private void givenLastSuccessfulScenarioBuildIsDisabledInConfiguration() {
		setConfigValueCreateLastSuccessfulScenarioBuildTo(false);
	}

	private void givenLastSuccessfulScenarioBuildIsEnabledInConfiguration() {
		setConfigValueCreateLastSuccessfulScenarioBuildTo(true);
	}

	private void givenFeatureWithASuccessfulAndAFailedScenario() {
		String featureName = features[0];
		createScenario(SCENARIO_FAILED, Status.FAILED, featureName);
		createScenario(SCENARIO_SUCCESS, Status.SUCCESS, featureName);
	}

	private void createScenario(final String scenarioName, final Status status,
			final String featureName) {
		ScenarioDocuWriter writer = new ScenarioDocuWriter(rootDirectory, BUILD_IDENTIFIER.getBranchName(),
				BUILD_IDENTIFIER.getBuildName());
		Scenario scenario = new Scenario();
		scenario.setName(scenarioName);
		scenario.setStatus(status);
		writer.saveScenario(featureName, scenario);
		writer.flush();
	}

	private void setConfigValueCreateLastSuccessfulScenarioBuildTo(final boolean value) {
		Configuration configuration = configurationRepository.getConfiguration();
		configuration.setCreateLastSuccessfulScenarioBuild(value);
		configurationRepository.updateConfiguration(configuration);
	}

	private void givenLastSuccessfulScenarioBuildFolderExists() {
		File branchDirectory = getBranchDirectory();
		branchDirectory.mkdirs();
		assertTrue(branchDirectory.exists());

		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		lastSuccessfulScenarioBuildDirectory.mkdir();
		assertTrue(lastSuccessfulScenarioBuildDirectory.exists());
	}

	private void givenLastSuccessfulScenarioBuildFolderDoesNotExist() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		if (lastSuccessfulScenarioBuildDirectory.exists()) {
			try {
				FileUtils.deleteDirectory(lastSuccessfulScenarioBuildDirectory);
			} catch (IOException e) {
				fail("can't delete directory " + lastSuccessfulScenarioBuildDirectory);
			}
		}
		assertFalse(lastSuccessfulScenarioBuildDirectory.exists());
	}

	private void givenImportedBuildHasThreeFeatures() {
		createThreeFeatures();
	}

	private void givenImportedBuildHasThreeFeaturesWithOneScenarioEach() {
		createThreeFeaturesWithOneScenario(SOURCE_USE_CASE_DESCRIPTION);
	}

	private void givenImportedBuildIsFromTodayAndFirstFeatureDoesNotExistAnymore() {
		buildImportSummary.getBuildDescription().setDate(DATE_NOW);
		createFeatureTwoAndThree();
	}

	private void givenImportedBuildIsFromTodayAndFirstScenarioInFirstFeatureDoesNotExistAnymore() {
		buildImportSummary.getBuildDescription().setDate(DATE_NOW);
		File featureDirectory = getDirectoryOfFirstFeature();
		createOnlySecondAndThirdScenario(featureDirectory);
	}

	private void givenImportedBuildHasThreeScenarios() {
		File featureDirectory = getDirectoryOfFirstFeature();
		createThreeScenarios(featureDirectory);
	}

	private File getDirectoryOfFirstFeature() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		return new File(importedBuildDirectory, encode(features[0]));
	}

	private void givenImportedBuildHasDerivedFilesAndFoldersOnAllLevelsPlusOneScenario() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createDerivedFileAndDirectory(importedBuildDirectory);

		File featureDirectory = new File(importedBuildDirectory, encode(features[0]));
		createDerivedFileAndDirectory(featureDirectory);

		File scenarioDirectory = new File(featureDirectory, encode(scenarios[0]));
		createDerivedFileAndDirectory(scenarioDirectory);

		createScenario(scenarios[0], Status.SUCCESS, encode(decode(featureDirectory.getName())));
	}

	private void createDerivedFileAndDirectory(final File directory) {
		File derivedDirectory = new File(directory, "directory.derived");
		File derivedDirectory2 = new File(directory, "directory.derived.something");
		File derivedFile = new File(directory, "file.derived");
		File derivedFile2 = new File(directory, "file.derived.log");

		derivedDirectory.mkdirs();
		derivedDirectory2.mkdirs();
		createNewFile(derivedFile);
		createNewFile(derivedFile2);

		assertTrue(derivedDirectory.exists());
		assertTrue(derivedDirectory2.exists());
		assertTrue(derivedFile.exists());
		assertTrue(derivedFile2.exists());
	}

	private void givenImportedBuildHasAScenarioWihoutScenarioXmlFile() {
		File featureDirectory = getDirectoryOfFirstFeature();

		createScenarioWithoutXmlFile(featureDirectory, scenarios[0]);
		createOnlySecondAndThirdScenario(featureDirectory);
	}

	private void givenImportedBuildHasOneFeatureWithAFeatureXmlFile() {
		createFeatureWithOneScenario(Status.SUCCESS);
	}

	private void givenImportedBuildHasOneFeatureWithOneFailedScenarioAndAFeatureXmlFile() {
		createFeatureWithOneScenario(Status.FAILED);
	}

	private void createFeatureWithOneScenario(final Status scenarioStatus) {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createFeature(importedBuildDirectory, features[0], Status.SUCCESS);

		createScenario(scenarios[0], scenarioStatus, features[0]);
	}

	private void givenImportedBuildHasOneFeatureWithAFeatureXmlFileWithStatusFailed() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createFeature(importedBuildDirectory, features[0], Status.FAILED);

		createScenario(scenarios[0], Status.SUCCESS, features[0]);
	}

	private void givenLastSuccessfulScenarioBuildWithScenarioFromYesterdayNowAndTomorrow() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		File featureDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(features[0]));

		Calendar calendar = getCalendarForNowMinusOneDay();

		for (String scenarioName : scenarios) {
			createSuccessfulScenario(featureDirectory, scenarioName);
			index.setScenarioBuildDate(features[0], scenarioName, calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}

		saveLastSuccessfulScenariosIndex(index);
	}

	private void givenLastSuccessfulScenarioBuildWithFeatureFromYesterdayNowAndTomorrow() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();

		createFeatureXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				features[0], NOT_MODIFIED_USE_CASE_DESCRIPTION);
		createFeatureXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				features[1], NOT_MODIFIED_USE_CASE_DESCRIPTION);
		createFeatureXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				features[2], NOT_MODIFIED_USE_CASE_DESCRIPTION);

		addScenarioToLastSuccessfulBuild(features[0], scenarios[0], DATE_YESTERDAY, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		addScenarioToLastSuccessfulBuild(features[1], scenarios[0], DATE_YESTERDAY, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		addScenarioToLastSuccessfulBuild(features[1], scenarios[1], DATE_NOW, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		addScenarioToLastSuccessfulBuild(features[2], scenarios[0], DATE_TOMORROW, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);

		saveLastSuccessfulScenariosIndex(index);
	}

	private void givenLastSuccessfulBuildHasOneFeatureWithOneFailedScenarioAndAFeatureXmlFile() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();

		createFeatureXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				features[0], NOT_MODIFIED_USE_CASE_DESCRIPTION);

		addScenarioToLastSuccessfulBuild(features[0], scenarios[0], DATE_YESTERDAY, index,
				lastSuccessfulScenariosBuildDirectory, Status.FAILED);

		saveLastSuccessfulScenariosIndex(index);
	}

	private void addScenarioToLastSuccessfulBuild(final String featureName, final String scenarioName,
			final Date buildDate, final LastSuccessfulScenariosIndex index,
			final File lastSuccessfulScenariosBuildDirectory, final Status status) {
		File featureDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(featureName));
		createScenario(featureDirectory, scenarioName, null, status);
		if (Status.SUCCESS.equals(status)) {
			index.setScenarioBuildDate(featureName, scenarioName, buildDate);
		}
	}

	private Calendar getCalendarForNowMinusOneDay() {
		return getCalendarNowPlusDays(-1);
	}

	private Calendar getCalendarForNowPlusOneDay() {
		return getCalendarNowPlusDays(1);
	}

	private Calendar getCalendarNowPlusDays(final int plusDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DATE_NOW);
		calendar.add(Calendar.DATE, plusDays);
		return calendar;
	}

	private void givenLatestBuildInLastSuccessfulScenariosBuildIsFromYesterdayAndContainsThreeFeatures() {
		Calendar yesterday = getCalendarForNowMinusOneDay();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		index.setLatestImportedBuildDate(yesterday.getTime());

		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File featureDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(features[0]));
		for (String scenarioName : scenarios) {
			createSuccessfulScenario(featureDirectory, scenarioName);
			index.setScenarioBuildDate(features[0], scenarioName, yesterday.getTime());
		}

		saveLastSuccessfulScenariosIndex(index);
	}

	private File getLastSuccessfulScenariosIndexFile() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		return new File(lastSuccessfulScenarioBuildDirectory, "lastSuccessfulScenariosIndex.derived");
	}

	private void createThreeScenarios(final File featureFolder) {
		for (String scenario : scenarios) {
			createSuccessfulScenario(featureFolder, scenario, DATE_NOW);
		}
	}

	private void createOnlySecondAndThirdScenario(final File featureDirectory) {
		createSuccessfulScenario(featureDirectory, scenarios[1], DATE_NOW);
		createSuccessfulScenario(featureDirectory, scenarios[2], DATE_NOW);
	}

	private void createSuccessfulScenario(final File featureFolder, final String scenario) {
		createSuccessfulScenario(featureFolder, scenario, null);
	}

	private void saveLastSuccessfulScenariosIndex(final LastSuccessfulScenariosIndex index) {
		File lastSuccessfulScenariosIndexFile = getLastSuccessfulScenariosIndexFile();
		ScenarioDocuXMLFileUtil.marshal(index, lastSuccessfulScenariosIndexFile);
	}

	private LastSuccessfulScenariosIndex getLastSuccessfulScenariosIndex() {
		File lastSuccessfulScenariosIndexFile = getLastSuccessfulScenariosIndexFile();

		if (lastSuccessfulScenariosIndexFile.exists()) {
			return ScenarioDocuXMLFileUtil.unmarshal(LastSuccessfulScenariosIndex.class,
					lastSuccessfulScenariosIndexFile);
		}

		return new LastSuccessfulScenariosIndex();
	}

	private void createThreeFeatures() {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		for (String feature : features) {
			createFeature(buildFolder, feature, Status.SUCCESS);
		}
	}

	private void createThreeFeaturesWithOneScenario(final String featureDescription) {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		for (String feature : features) {
			createFeatureWithScenario(buildFolder, feature, featureDescription);
		}
	}

	private void createFeatureTwoAndThree() {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createFeature(buildFolder, features[1], Status.SUCCESS);
		createFeature(buildFolder, features[2], Status.SUCCESS);
	}

	private void createFeature(final File importedBuildDirectory, final String featureName, final Status status) {
		File featureDirectory = new File(importedBuildDirectory, encode(featureName));
		featureDirectory.mkdirs();
		assertTrue(featureDirectory.exists());

		ImportFeature importFeature = new ImportFeature();
		importFeature.setStatus(status);
		importFeature.setName(featureName);

		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(rootDirectory, BUILD_IDENTIFIER.getBranchName(),
				BUILD_IDENTIFIER.getBuildName());
		scenarioDocuWriter.saveFeature(importFeature);
		scenarioDocuWriter.flush();

		File featureFile = new File(featureDirectory, FILE_NAME_FEATURE);
		assertTrue(featureFile.exists());
	}

	private void createFeatureWithScenario(final File importedBuildDirectory, final String featureName,
			final String featureDescription) {
		createFeatureXmlFile(rootDirectory, BUILD_IDENTIFIER.getBuildName(), featureName, featureDescription);
		File featureDirectory = new File(importedBuildDirectory, encode(featureName));
		createSuccessfulScenario(featureDirectory, scenarios[0], DATE_NOW);
	}

	private void createFeatureXmlFile(final File rootDirectory, final String buildName, final String featureName,
			final String featureDescription) {
		ImportFeature importFeature = new ImportFeature();
		importFeature.setDescription(featureDescription);
		importFeature.setName(featureName);

		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(rootDirectory, BUILD_IDENTIFIER.getBranchName(),
				buildName);
		scenarioDocuWriter.saveFeature(importFeature);
		scenarioDocuWriter.flush();
	}

	private void createSuccessfulScenario(final File featureFolder, final String scenarioName, final Date date) {
		createScenario(featureFolder, scenarioName, date, Status.SUCCESS);
	}

	private void createScenario(final File featureFolder, final String scenarioName, final Date date,
			final Status status) {
		File scenarioDirectory = new File(featureFolder, encode(scenarioName));
		scenarioDirectory.mkdirs();
		assertTrue(scenarioDirectory.exists());

		File scenarioFile = new File(scenarioDirectory, FILE_NAME_SCENARIO);
		Scenario scenario = new Scenario();
		scenario.setName(scenarioName);
		scenario.setStatus(status);
		scenario.addDetail(BUILD_DATE_FOR_TEST_KEY, date);
		ScenarioDocuXMLFileUtil.marshal(scenario, scenarioFile);
	}

	private void createScenarioWithoutXmlFile(final File featureDirectory, final String scenarioName) {
		File scenarioDirectory = new File(featureDirectory, encode(scenarioName));
		scenarioDirectory.mkdirs();
		assertTrue(scenarioDirectory.exists());

		File scenarioFile = new File(scenarioDirectory, FILE_NAME_SCENARIO);
		assertFalse(scenarioFile.exists());
	}

	private File getImportedBuildDirectory(final BuildIdentifier buildIdentifier) {
		return new File(rootDirectory, buildIdentifier.getBranchName() + "/" + buildIdentifier.getBuildName());
	}

	private File getBranchDirectory() {
		return new File(rootDirectory, BUILD_IDENTIFIER.getBranchName());
	}

	private File getLastSuccessfulScenariosBuildDirectory() {
		try {
			return new File(rootDirectory, BUILD_IDENTIFIER.getBranchName()
					+ "/"
					+ FilesUtil.encodeName(LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME));
		} catch (IllegalStateException e) {
			fail("url encoding failed");
			return null;
		}
	}

	private BuildImportSummary getSummaryWith(final BuildImportStatus buildImportStatus) {
		BuildImportSummary summary = new BuildImportSummary();
		summary.setStatus(buildImportStatus);
		summary.setIdentifier(BUILD_IDENTIFIER);
		return summary;
	}

	private void whenUpdatingLastSuccessfulScenarioBuild() {
		BuildImporter buildImporter = new BuildImporter();
		AvailableBuildsList availableBuildsList = new AvailableBuildsList();
		lastSuccessfulScenarioBuild.updateLastSuccessfulScenarioBuild(buildImportSummary, buildImporter,
				availableBuildsList);
	}

	private void expectLastSuccessfulScenarioBuildDirectoryDoesNotExist() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		assertFalse(lastSuccessfulScenarioBuildDirectory.exists());
	}

	private void expectBranchDirectoryExists() {
		File branchDirectory = getBranchDirectory();
		assertTrue(branchDirectory.exists());
	}

	private void expectLastSuccessfulScenarioBuildDirectoryExists() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		assertTrue(lastSuccessfulScenarioBuildDirectory.exists());
	}

	private void expectTheThreeFeaturesExistInTheLastSuccessfulScenarioBuild() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		for (String feature : features) {
			assertFeatureExists(lastSuccessfulScenarioBuildDirectory, feature);
		}
	}

	private void assertFeatureExists(final File lastSuccessfulScenarioBuildDirectory, final String feature) {
		File featureFile = new File(lastSuccessfulScenarioBuildDirectory, encode(feature));
		assertTrue(featureFile.exists());
	}

	private void expectDerivedFilesAndDirectoriesDoNotExistInLastSuccessfulScenariosBuildAndScenarioWasCopied() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File featureDirectory = new File(lastSuccessfulScenarioBuildDirectory, encode(features[0]));
		File scenarioDirectory = new File(featureDirectory, encode(scenarios[0]));
		File scenarioFile = new File(scenarioDirectory, FILE_NAME_SCENARIO);

		assertFolderDoesNotContainAnyDerivedFilesOrDirectories(lastSuccessfulScenarioBuildDirectory);
		assertFolderDoesNotContainAnyDerivedFilesOrDirectories(featureDirectory);
		assertFolderDoesNotContainAnyDerivedFilesOrDirectories(scenarioDirectory);

		assertTrue(scenarioFile.exists());
	}

	private void assertFolderDoesNotContainAnyDerivedFilesOrDirectories(final File directory) {
		String[] derivedFilesAndDirectories = directory.list(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				File fileOrDirectory = new File(dir, name);
				return fileOrDirectory.getName().contains(".derived")
						&& !fileOrDirectory.getName().equals(
								encode(LastSuccessfulScenariosIndexDao.LAST_SUCCESSFUL_SCENARIOS_INDEX_FILENAME));
			}
		});

		assertTrue(derivedFilesAndDirectories == null || derivedFilesAndDirectories.length == 0);
	}

	private void expectBuildXmlWithValidContentExists() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File buildXml = new File(lastSuccessfulScenarioBuildDirectory, "build.xml");
		assertTrue(buildXml.exists());

		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(rootDirectory);
		Build build = scenarioDocuReader.loadBuild(BUILD_IDENTIFIER.getBranchName(),
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);

		assertEquals(Status.SUCCESS.getKeyword(), build.getStatus());
		assertEquals(LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME, build.getName());
		assertNotNull(build.getDate());
		assertEquals("various", build.getRevision());
	}

	private void expectOnlytheSuccessfulScenarioIsCopied() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();

		File featureDirectory = new File(lastSuccessfulScenarioBuildDirectory, encode(features[0]));
		assertTrue(featureDirectory.exists());

		File successfulScenario = new File(featureDirectory, encode(SCENARIO_SUCCESS));
		assertTrue(successfulScenario.exists());

		File failedScenario = new File(featureDirectory, encode(SCENARIO_FAILED));
		assertFalse(failedScenario.exists());
	}

	private void expectOnlyTheNewerBuildWasOverwritten() {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(DATE_NOW);
		tomorrow.add(Calendar.DATE, 1);

		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File featureDirectory = new File(lastSuccessfulScenarioBuildDirectory, encode(features[0]));

		assertScenarioHasDate(featureDirectory, features[0], scenarios[0], DATE_NOW, index, true);
		assertScenarioHasDate(featureDirectory, features[0], scenarios[1], DATE_NOW, index, false);
		assertScenarioHasDate(featureDirectory, features[0], scenarios[2], tomorrow.getTime(), index, false);
	}

	private void assertScenarioHasDate(final File featureDirectory, final String featureName,
			final String scenarioName, final Date expectedDate, final LastSuccessfulScenariosIndex index,
			final boolean expectScenarioWasCopied) {
		assertEquals(expectedDate, index.getScenarioBuildDate(featureName, scenarioName));

		if (expectScenarioWasCopied) {
			File scenarioFile = new File(featureDirectory, encode(scenarioName) + "/" + FILE_NAME_SCENARIO);
			Scenario scenario = ScenarioDocuXMLFileUtil.unmarshal(Scenario.class, scenarioFile);
			Date actualDate = ((XMLGregorianCalendar) scenario.getDetails().get(BUILD_DATE_FOR_TEST_KEY))
					.toGregorianCalendar().getTime();
			assertEquals(expectedDate, actualDate);
		}
	}

	private void expectFirstFeatureDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File firstFeatureDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(features[0]));
		assertFalse(firstFeatureDirectory.exists());

		LastSuccessfulScenariosIndex lastSuccessfulScenariosIndex = getLastSuccessfulScenariosIndex();
		assertNull(lastSuccessfulScenariosIndex.getFeature(features[0]));
	}

	private void expectLatestImportedBuildDateIsNow() {
		LastSuccessfulScenariosIndex lastSuccessfulScenariosIndex = getLastSuccessfulScenariosIndex();
		assertEquals(DATE_NOW, lastSuccessfulScenariosIndex.getLatestImportedBuildDate());
	}

	private void expectFirstScenarioInFirstFeatureDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File firstFeatureDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(features[0]));
		File firstScenarioDirectory = new File(firstFeatureDirectory, encode(scenarios[0]));
		assertFalse(firstScenarioDirectory.exists());

		LastSuccessfulScenariosIndex lastSuccessfulScenariosIndex = getLastSuccessfulScenariosIndex();
		assertNotNull(lastSuccessfulScenariosIndex.getFeature(features[0]));
		assertNull(lastSuccessfulScenariosIndex.getFeature(features[0]).getScenario(scenarios[0]));
	}

	private void expectOnlyTheSecondAndThirdScenarioAreAddedToTheLastSuccessfulBuild() {
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File firstFeatureDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(features[0]));

		expectScenarioDoesNotExistInLastSuccessfulBuild(firstFeatureDirectory, index, scenarios[0]);
		expectScenarioExistsInLastSuccessfulBuild(firstFeatureDirectory, index, scenarios[1]);
		expectScenarioExistsInLastSuccessfulBuild(firstFeatureDirectory, index, scenarios[2]);
	}

	private void expectScenarioDoesNotExistInLastSuccessfulBuild(final File featureDirectory,
			final LastSuccessfulScenariosIndex index, final String scenarioName) {
		File scenarioDirectory = new File(featureDirectory, encode(scenarioName));
		assertFalse(scenarioDirectory.exists());

		assertNull(getScenarioFromIndex(featureDirectory, index, scenarioName));
	}

	private void expectScenarioExistsInLastSuccessfulBuild(final File featureDirectory,
			final LastSuccessfulScenariosIndex index, final String scenarioName) {
		File scenarioFile = new File(featureDirectory, encode(scenarioName) + "/" + FILE_NAME_SCENARIO);
		assertTrue(scenarioFile.exists());

		assertNotNull(getScenarioFromIndex(featureDirectory, index, scenarioName));
	}

	private void expectFeatureXmlFileWasCopied() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File featureFile = new File(lastSuccessfulScenariosBuildDirectory, encode(features[0]) + "/"
				+ FILE_NAME_FEATURE);
		assertTrue(featureFile.exists());
	}

	private void expectCopiedFeatureXmlFileHasStatusSuccess() {
		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(rootDirectory);
		ImportFeature importFeature = scenarioDocuReader.loadUsecase(BUILD_IDENTIFIER.getBranchName(),
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME, features[0]);
		assertEquals(Status.SUCCESS.getKeyword(), importFeature.getStatus());
	}

	private void expectFeatureXmlFileWasCopiedForTheFirstTwoFeatures() {
		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(rootDirectory);

		assertFeatureInLastSuccessfulScenariosBuildHasDescription(scenarioDocuReader, features[0],
				SOURCE_USE_CASE_DESCRIPTION);
		assertFeatureInLastSuccessfulScenariosBuildHasDescription(scenarioDocuReader, features[1],
				SOURCE_USE_CASE_DESCRIPTION);
		assertFeatureInLastSuccessfulScenariosBuildHasDescription(scenarioDocuReader, features[2],
				NOT_MODIFIED_USE_CASE_DESCRIPTION);
	}

	private void assertFeatureInLastSuccessfulScenariosBuildHasDescription(final ScenarioDocuReader scenarioDocuReader,
			final String featureName, final String expectedDescription) {
		ImportFeature importFeature = scenarioDocuReader.loadUsecase(BUILD_IDENTIFIER.getBranchName(),
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME, featureName);
		assertEquals(expectedDescription, importFeature.getDescription());
	}

	private LastSuccessfulScenario getScenarioFromIndex(final File featureDirectory,
			final LastSuccessfulScenariosIndex index, final String scenarioName) {
		return index.getFeature(decode(featureDirectory.getName())).getScenario(scenarioName);
	}

	private void createNewFile(final File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			fail("could not create file " + file);
		}
	}


	//TODO REMOVE
	private String encode(final String string) {
		return FilesUtil.encodeName(string);
	}
	//TODO REMOVE
	private String decode(final String string) {
		return FilesUtil.decodeName(string);
	}

}
