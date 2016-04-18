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
import org.junit.Test;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.business.builds.AvailableBuildsList;
import org.scenarioo.business.builds.BuildImporter;
import org.scenarioo.dao.aggregates.LastSuccessfulScenariosIndexDao;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenario;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.TestFileUtils;

public class LastSuccessfulScenariosBuildTest {
	
	private static final String SOURCE_USE_CASE_DESCRIPTION = "use case from imported build";
	private static final String NOT_MODIFIED_USE_CASE_DESCRIPTION = "use case file was not modified";
	private static final String BUILD_DATE_FOR_TEST_KEY = "buildDateForTest";
	private static final String SCENARIO_SUCCESS = "a successful scenario";
	private static final String SCENARIO_FAILED = "a failed scenario";
	private static final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final String[] useCases = new String[] { "Log in", "Send message", "Sign out" };
	private final String[] scenarios = new String[] { "login successful", "login failed", "password forgotten" };
	private final Date DATE_NOW = new Date();
	private final Date DATE_YESTERDAY = getCalendarForNowMinusOneDay().getTime();
	private final Date DATE_TOMORROW = getCalendarForNowPlusOneDay().getTime();
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	private static final String FILE_NAME_USECASE = "usecase.xml";
	
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
	
	@Test
	public void allUseCasesAreCopied() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasThreeUseCases();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectTheThreeUseCasesExistInTheLastSuccessfulScenarioBuild();
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
		givenUseCaseWithASuccessfulAndAFailedScenario();
		
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
	
	@Test
	public void ifTheImportedUseCaseIsTheLatestOneAllUseCasesThatDoNotExistAnymoreAreDeleted() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenBuildImportSummaryWithStatusSuccess();
		givenLatestBuildInLastSuccessfulScenariosBuildIsFromYesterdayAndContainsThreeUseCases();
		givenImportedBuildIsFromTodayAndFirstUseCaseDoesNotExistAnymore();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectFirstUseCaseDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex();
		expectLatestImportedBuildDateIsNow();
	}
	
	@Test
	public void ifTheImportedUseCaseIsTheLatestOneAllScenariosThatDoNotExistAnymoreAreDeleted() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenBuildImportSummaryWithStatusSuccess();
		givenLatestBuildInLastSuccessfulScenariosBuildIsFromYesterdayAndContainsThreeUseCases();
		givenImportedBuildIsFromTodayAndFirstScenarioInFirstUseCaseDoesNotExistAnymore();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectFirstScenarioInFirstUseCaseDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex();
		expectLatestImportedBuildDateIsNow();
	}
	
	@Test
	public void theUseCaseXmlFileIsCopiedIfItDoesNotExistYet() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasOneUseCaseWithAUseCaseXmlFile();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectUseCaseXmlFileWasCopied();
	}
	
	@Test
	public void theUseCaseXmlFileIsAlsoCopiedIfThereAreNoSuccessfulTestInTheUseCase() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenLastSuccessfulBuildHasOneUseCaseWithOneFailedScenarioAndAUseCaseXmlFile();
		givenImportedBuildHasOneUseCaseWithOneFailedScenarioAndAUseCaseXmlFile();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectUseCaseXmlFileWasCopied();
	}
	
	@Test
	public void theStatusInTheCopiedUseCaseXmlFileIsAlwaysSetToSuccess() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasOneUseCaseWithAUseCaseXmlFileWithStatusFailed();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectCopiedUseCaseXmlFileHasStatusSuccess();
	}
	
	@Test
	public void theUseCaseXmlOfTheLatestBuildIsUsed() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenLastSuccessfulScenarioBuildWithUseCaseFromYesterdayNowAndTomorrow();
		givenImportedBuildHasThreeUseCasesWithOneScenarioEach();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectUseCaseXmlFileWasCopiedForTheFirstTwoUseCases();
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
	
	private void givenUseCaseWithASuccessfulAndAFailedScenario() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		String useCaseName = useCases[0];
		File useCaseDirectory = new File(importedBuildDirectory, encode(useCaseName));
		createScenario(useCaseDirectory, SCENARIO_FAILED, Status.FAILED, useCaseName);
		createScenario(useCaseDirectory, SCENARIO_SUCCESS, Status.SUCCESS, useCaseName);
	}
	
	private void createScenario(final File useCaseDirectory, final String scenarioName, final Status status,
			final String useCaseName) {
		ScenarioDocuWriter writer = new ScenarioDocuWriter(rootDirectory, BUILD_IDENTIFIER.getBranchName(),
				BUILD_IDENTIFIER.getBuildName());
		Scenario scenario = new Scenario();
		scenario.setName(scenarioName);
		scenario.setStatus(status);
		writer.saveScenario(useCaseName, scenario);
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
	
	private void givenImportedBuildHasThreeUseCases() {
		createThreeUseCases();
	}
	
	private void givenImportedBuildHasThreeUseCasesWithOneScenarioEach() {
		createThreeUseCasesWithOneScenario(SOURCE_USE_CASE_DESCRIPTION);
	}
	
	private void givenImportedBuildIsFromTodayAndFirstUseCaseDoesNotExistAnymore() {
		buildImportSummary.getBuildDescription().setDate(DATE_NOW);
		createUseCaseTwoAndThree();
	}
	
	private void givenImportedBuildIsFromTodayAndFirstScenarioInFirstUseCaseDoesNotExistAnymore() {
		buildImportSummary.getBuildDescription().setDate(DATE_NOW);
		File useCaseDirectory = getDirectoryOfFirstUseCase();
		createOnlySecondAndThirdScenario(useCaseDirectory);
	}
	
	private void givenImportedBuildHasThreeScenarios() {
		File useCaseDirectory = getDirectoryOfFirstUseCase();
		createThreeScenarios(useCaseDirectory);
	}
	
	private File getDirectoryOfFirstUseCase() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		return new File(importedBuildDirectory, encode(useCases[0]));
	}
	
	private void givenImportedBuildHasDerivedFilesAndFoldersOnAllLevelsPlusOneScenario() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createDerivedFileAndDirectory(importedBuildDirectory);
		
		File useCaseDirectory = new File(importedBuildDirectory, encode(useCases[0]));
		createDerivedFileAndDirectory(useCaseDirectory);
		
		File scenarioDirectory = new File(useCaseDirectory, encode(scenarios[0]));
		createDerivedFileAndDirectory(scenarioDirectory);
		
		createScenario(useCaseDirectory, scenarios[0], Status.SUCCESS, decode(useCaseDirectory.getName()));
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
		File useCaseDirectory = getDirectoryOfFirstUseCase();
		
		createScenarioWithoutXmlFile(useCaseDirectory, scenarios[0]);
		createOnlySecondAndThirdScenario(useCaseDirectory);
	}
	
	private void givenImportedBuildHasOneUseCaseWithAUseCaseXmlFile() {
		createUseCaseWithOneScenario(Status.SUCCESS);
	}
	
	private void givenImportedBuildHasOneUseCaseWithOneFailedScenarioAndAUseCaseXmlFile() {
		createUseCaseWithOneScenario(Status.FAILED);
	}
	
	private void createUseCaseWithOneScenario(final Status scenarioStatus) {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createUseCase(importedBuildDirectory, useCases[0], Status.SUCCESS);
		
		File useCaseDirectory = getDirectoryOfFirstUseCase();
		createScenario(useCaseDirectory, scenarios[0], scenarioStatus, useCases[0]);
	}
	
	private void givenImportedBuildHasOneUseCaseWithAUseCaseXmlFileWithStatusFailed() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createUseCase(importedBuildDirectory, useCases[0], Status.FAILED);
		
		File useCaseDirectory = getDirectoryOfFirstUseCase();
		createScenario(useCaseDirectory, scenarios[0], Status.SUCCESS, useCases[0]);
	}
	
	private void givenLastSuccessfulScenarioBuildWithScenarioFromYesterdayNowAndTomorrow() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		File useCaseDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(useCases[0]));
		
		Calendar calendar = getCalendarForNowMinusOneDay();
		
		for (String scenarioName : scenarios) {
			createSuccessfulScenario(useCaseDirectory, scenarioName);
			index.setScenarioBuildDate(useCases[0], scenarioName, calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		
		saveLastSuccessfulScenariosIndex(index);
	}
	
	private void givenLastSuccessfulScenarioBuildWithUseCaseFromYesterdayNowAndTomorrow() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		
		createUseCaseXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				useCases[0], NOT_MODIFIED_USE_CASE_DESCRIPTION);
		createUseCaseXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				useCases[1], NOT_MODIFIED_USE_CASE_DESCRIPTION);
		createUseCaseXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				useCases[2], NOT_MODIFIED_USE_CASE_DESCRIPTION);
		
		addScenarioToLastSuccessfulBuild(useCases[0], scenarios[0], DATE_YESTERDAY, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		addScenarioToLastSuccessfulBuild(useCases[1], scenarios[0], DATE_YESTERDAY, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		addScenarioToLastSuccessfulBuild(useCases[1], scenarios[1], DATE_NOW, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		addScenarioToLastSuccessfulBuild(useCases[2], scenarios[0], DATE_TOMORROW, index,
				lastSuccessfulScenariosBuildDirectory, Status.SUCCESS);
		
		saveLastSuccessfulScenariosIndex(index);
	}
	
	private void givenLastSuccessfulBuildHasOneUseCaseWithOneFailedScenarioAndAUseCaseXmlFile() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		
		createUseCaseXmlFile(rootDirectory, LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
				useCases[0], NOT_MODIFIED_USE_CASE_DESCRIPTION);
		
		addScenarioToLastSuccessfulBuild(useCases[0], scenarios[0], DATE_YESTERDAY, index,
				lastSuccessfulScenariosBuildDirectory, Status.FAILED);
		
		saveLastSuccessfulScenariosIndex(index);
	}
	
	private void addScenarioToLastSuccessfulBuild(final String useCaseName, final String scenarioName,
			final Date buildDate, final LastSuccessfulScenariosIndex index,
			final File lastSuccessfulScenariosBuildDirectory, final Status status) {
		File useCaseDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(useCaseName));
		createScenario(useCaseDirectory, scenarioName, null, status);
		if (Status.SUCCESS.equals(status)) {
			index.setScenarioBuildDate(useCaseName, scenarioName, buildDate);
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
	
	private void givenLatestBuildInLastSuccessfulScenariosBuildIsFromYesterdayAndContainsThreeUseCases() {
		Calendar yesterday = getCalendarForNowMinusOneDay();
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		index.setLatestImportedBuildDate(yesterday.getTime());
		
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File useCaseDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(useCases[0]));
		for (String scenarioName : scenarios) {
			createSuccessfulScenario(useCaseDirectory, scenarioName);
			index.setScenarioBuildDate(useCases[0], scenarioName, yesterday.getTime());
		}
		
		saveLastSuccessfulScenariosIndex(index);
	}
	
	private File getLastSuccessfulScenariosIndexFile() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		return new File(lastSuccessfulScenarioBuildDirectory, "lastSuccessfulScenariosIndex.derived");
	}
	
	private void createThreeScenarios(final File useCaseFolder) {
		for (String scenario : scenarios) {
			createSuccessfulScenario(useCaseFolder, scenario, DATE_NOW);
		}
	}
	
	private void createOnlySecondAndThirdScenario(final File useCaseDirectory) {
		createSuccessfulScenario(useCaseDirectory, scenarios[1], DATE_NOW);
		createSuccessfulScenario(useCaseDirectory, scenarios[2], DATE_NOW);
	}
	
	private void createSuccessfulScenario(final File useCaseFolder, final String scenario) {
		createSuccessfulScenario(useCaseFolder, scenario, null);
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
	
	private void createThreeUseCases() {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		for (String useCase : useCases) {
			createUseCase(buildFolder, useCase, Status.SUCCESS);
		}
	}
	
	private void createThreeUseCasesWithOneScenario(final String useCaseDescription) {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		for (String useCase : useCases) {
			createUseCaseWithScenario(buildFolder, useCase, useCaseDescription);
		}
	}
	
	private void createUseCaseTwoAndThree() {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createUseCase(buildFolder, useCases[1], Status.SUCCESS);
		createUseCase(buildFolder, useCases[2], Status.SUCCESS);
	}
	
	private void createUseCase(final File importedBuildDirectory, final String useCaseName, final Status status) {
		File useCaseDirectory = new File(importedBuildDirectory, encode(useCaseName));
		useCaseDirectory.mkdirs();
		assertTrue(useCaseDirectory.exists());
		
		UseCase useCase = new UseCase();
		useCase.setStatus(status);
		useCase.setName(useCaseName);
		
		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(rootDirectory, BUILD_IDENTIFIER.getBranchName(),
				BUILD_IDENTIFIER.getBuildName());
		scenarioDocuWriter.saveUseCase(useCase);
		scenarioDocuWriter.flush();
		
		File useCaseFile = new File(useCaseDirectory, FILE_NAME_USECASE);
		assertTrue(useCaseFile.exists());
	}
	
	private void createUseCaseWithScenario(final File importedBuildDirectory, final String useCaseName,
			final String useCaseDescription) {
		createUseCaseXmlFile(rootDirectory, BUILD_IDENTIFIER.getBuildName(), useCaseName, useCaseDescription);
		File useCaseDirectory = new File(importedBuildDirectory, encode(useCaseName));
		createSuccessfulScenario(useCaseDirectory, scenarios[0], DATE_NOW);
	}
	
	private void createUseCaseXmlFile(final File rootDirectory, final String buildName, final String useCaseName,
			final String useCaseDescription) {
		UseCase useCase = new UseCase();
		useCase.setDescription(useCaseDescription);
		useCase.setName(useCaseName);
		
		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(rootDirectory, BUILD_IDENTIFIER.getBranchName(),
				buildName);
		scenarioDocuWriter.saveUseCase(useCase);
		scenarioDocuWriter.flush();
	}
	
	private void createSuccessfulScenario(final File useCaseFolder, final String scenarioName, final Date date) {
		createScenario(useCaseFolder, scenarioName, date, Status.SUCCESS);
	}
	
	private void createScenario(final File useCaseFolder, final String scenarioName, final Date date,
			final Status status) {
		File scenarioDirectory = new File(useCaseFolder, encode(scenarioName));
		scenarioDirectory.mkdirs();
		assertTrue(scenarioDirectory.exists());
		
		File scenarioFile = new File(scenarioDirectory, FILE_NAME_SCENARIO);
		Scenario scenario = new Scenario();
		scenario.setName(scenarioName);
		scenario.setStatus(status);
		scenario.addDetail(BUILD_DATE_FOR_TEST_KEY, date);
		ScenarioDocuXMLFileUtil.marshal(scenario, scenarioFile);
	}
	
	private void createScenarioWithoutXmlFile(final File useCaseDirectory, final String scenarioName) {
		File scenarioDirectory = new File(useCaseDirectory, encode(scenarioName));
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
					+ URLEncoder.encode(LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
							"UTF-8"));
		} catch (UnsupportedEncodingException e) {
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
	
	private void expectTheThreeUseCasesExistInTheLastSuccessfulScenarioBuild() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		for (String useCase : useCases) {
			assertUseCaseExists(lastSuccessfulScenarioBuildDirectory, useCase);
		}
	}
	
	private void assertUseCaseExists(final File lastSuccessfulScenarioBuildDirectory, final String useCase) {
		File useCaseFile = new File(lastSuccessfulScenarioBuildDirectory, encode(useCase));
		assertTrue(useCaseFile.exists());
	}
	
	private void expectDerivedFilesAndDirectoriesDoNotExistInLastSuccessfulScenariosBuildAndScenarioWasCopied() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File useCaseDirectory = new File(lastSuccessfulScenarioBuildDirectory, encode(useCases[0]));
		File scenarioDirectory = new File(useCaseDirectory, encode(scenarios[0]));
		File scenarioFile = new File(scenarioDirectory, FILE_NAME_SCENARIO);
		
		assertFolderDoesNotContainAnyDerivedFilesOrDirectories(lastSuccessfulScenarioBuildDirectory);
		assertFolderDoesNotContainAnyDerivedFilesOrDirectories(useCaseDirectory);
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
		
		File useCaseDirectory = new File(lastSuccessfulScenarioBuildDirectory, encode(useCases[0]));
		assertTrue(useCaseDirectory.exists());
		
		File successfulScenario = new File(useCaseDirectory, encode(SCENARIO_SUCCESS));
		assertTrue(successfulScenario.exists());
		
		File failedScenario = new File(useCaseDirectory, encode(SCENARIO_FAILED));
		assertFalse(failedScenario.exists());
	}
	
	private void expectOnlyTheNewerBuildWasOverwritten() {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(DATE_NOW);
		tomorrow.add(Calendar.DATE, 1);
		
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File useCaseDirectory = new File(lastSuccessfulScenarioBuildDirectory, encode(useCases[0]));
		
		assertScenarioHasDate(useCaseDirectory, useCases[0], scenarios[0], DATE_NOW, index, true);
		assertScenarioHasDate(useCaseDirectory, useCases[0], scenarios[1], DATE_NOW, index, false);
		assertScenarioHasDate(useCaseDirectory, useCases[0], scenarios[2], tomorrow.getTime(), index, false);
	}
	
	private void assertScenarioHasDate(final File useCaseDirectory, final String useCaseName,
			final String scenarioName, final Date expectedDate, final LastSuccessfulScenariosIndex index,
			final boolean expectScenarioWasCopied) {
		assertEquals(expectedDate, index.getScenarioBuildDate(useCaseName, scenarioName));
		
		if (expectScenarioWasCopied) {
			File scenarioFile = new File(useCaseDirectory, encode(scenarioName) + "/" + FILE_NAME_SCENARIO);
			Scenario scenario = ScenarioDocuXMLFileUtil.unmarshal(Scenario.class, scenarioFile);
			Date actualDate = ((XMLGregorianCalendar) scenario.getDetails().get(BUILD_DATE_FOR_TEST_KEY))
					.toGregorianCalendar().getTime();
			assertEquals(expectedDate, actualDate);
		}
	}
	
	private void expectFirstUseCaseDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File firstUseCaseDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(useCases[0]));
		assertFalse(firstUseCaseDirectory.exists());
		
		LastSuccessfulScenariosIndex lastSuccessfulScenariosIndex = getLastSuccessfulScenariosIndex();
		assertNull(lastSuccessfulScenariosIndex.getUseCase(useCases[0]));
	}
	
	private void expectLatestImportedBuildDateIsNow() {
		LastSuccessfulScenariosIndex lastSuccessfulScenariosIndex = getLastSuccessfulScenariosIndex();
		assertEquals(DATE_NOW, lastSuccessfulScenariosIndex.getLatestImportedBuildDate());
	}
	
	private void expectFirstScenarioInFirstUseCaseDoesNotExistAnymoreInLastSuccessfulBuildsFolderAndInTheIndex() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File firstUseCaseDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(useCases[0]));
		File firstScenarioDirectory = new File(firstUseCaseDirectory, encode(scenarios[0]));
		assertFalse(firstScenarioDirectory.exists());
		
		LastSuccessfulScenariosIndex lastSuccessfulScenariosIndex = getLastSuccessfulScenariosIndex();
		assertNotNull(lastSuccessfulScenariosIndex.getUseCase(useCases[0]));
		assertNull(lastSuccessfulScenariosIndex.getUseCase(useCases[0]).getScenario(scenarios[0]));
	}
	
	private void expectOnlyTheSecondAndThirdScenarioAreAddedToTheLastSuccessfulBuild() {
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex();
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File firstUseCaseDirectory = new File(lastSuccessfulScenariosBuildDirectory, encode(useCases[0]));
		
		expectScenarioDoesNotExistInLastSuccessfulBuild(firstUseCaseDirectory, index, scenarios[0]);
		expectScenarioExistsInLastSuccessfulBuild(firstUseCaseDirectory, index, scenarios[1]);
		expectScenarioExistsInLastSuccessfulBuild(firstUseCaseDirectory, index, scenarios[2]);
	}
	
	private void expectScenarioDoesNotExistInLastSuccessfulBuild(final File useCaseDirectory,
			final LastSuccessfulScenariosIndex index, final String scenarioName) {
		File scenarioDirectory = new File(useCaseDirectory, encode(scenarioName));
		assertFalse(scenarioDirectory.exists());
		
		assertNull(getScenarioFromIndex(useCaseDirectory, index, scenarioName));
	}
	
	private void expectScenarioExistsInLastSuccessfulBuild(final File useCaseDirectory,
			final LastSuccessfulScenariosIndex index, final String scenarioName) {
		File scenarioFile = new File(useCaseDirectory, encode(scenarioName) + "/" + FILE_NAME_SCENARIO);
		assertTrue(scenarioFile.exists());
		
		assertNotNull(getScenarioFromIndex(useCaseDirectory, index, scenarioName));
	}
	
	private void expectUseCaseXmlFileWasCopied() {
		File lastSuccessfulScenariosBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File useCaseFile = new File(lastSuccessfulScenariosBuildDirectory, encode(useCases[0]) + "/"
				+ FILE_NAME_USECASE);
		assertTrue(useCaseFile.exists());
	}
	
	private void expectCopiedUseCaseXmlFileHasStatusSuccess() {
		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(rootDirectory);
		UseCase useCase = scenarioDocuReader.loadUsecase(BUILD_IDENTIFIER.getBranchName(),
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME, useCases[0]);
		assertEquals(Status.SUCCESS.getKeyword(), useCase.getStatus());
	}
	
	private void expectUseCaseXmlFileWasCopiedForTheFirstTwoUseCases() {
		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(rootDirectory);
		
		assertUseCaseInLastSuccessfulScenariosBuildHasDescription(scenarioDocuReader, useCases[0],
				SOURCE_USE_CASE_DESCRIPTION);
		assertUseCaseInLastSuccessfulScenariosBuildHasDescription(scenarioDocuReader, useCases[1],
				SOURCE_USE_CASE_DESCRIPTION);
		assertUseCaseInLastSuccessfulScenariosBuildHasDescription(scenarioDocuReader, useCases[2],
				NOT_MODIFIED_USE_CASE_DESCRIPTION);
	}
	
	private void assertUseCaseInLastSuccessfulScenariosBuildHasDescription(final ScenarioDocuReader scenarioDocuReader,
			final String useCaseName, final String expectedDescription) {
		UseCase useCase = scenarioDocuReader.loadUsecase(BUILD_IDENTIFIER.getBranchName(),
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME, useCaseName);
		assertEquals(expectedDescription, useCase.getDescription());
	}
	
	private LastSuccessfulScenario getScenarioFromIndex(final File useCaseDirectory,
			final LastSuccessfulScenariosIndex index, final String scenarioName) {
		return index.getUseCase(decode(useCaseDirectory.getName())).getScenario(scenarioName);
	}
	
	private void createNewFile(final File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			fail("could not create file " + file);
		}
	}
	
	private String encode(final String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			fail();
			return null;
		}
	}
	
	private String decode(final String string) {
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
