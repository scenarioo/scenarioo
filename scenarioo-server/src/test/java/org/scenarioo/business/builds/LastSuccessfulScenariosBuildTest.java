package org.scenarioo.business.builds;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.LastSuccessfulScenariosBuildRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

public class LastSuccessfulScenariosBuildTest {
	
	private static final String BUILD_DATE_FOR_TEST_KEY = "buildDateForTest";
	private static final String SCENARIO_SUCCESS = "a successful scenario";
	private static final String SCENARIO_FAILED = "a failed scenario";
	private static final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final String[] useCases = new String[] { "Log in", "Send message", "Sign out" };
	private final String[] scenarios = new String[] { "login successful", "login failed", "password forgotten" };
	private final Date DATE_NOW = new Date();
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	
	private LastSuccessfulScenariosBuild lastSuccessfulScenarioBuild;
	private ConfigurationRepository configurationRepository;
	private final File rootDirectory = new File("tmp");
	private BuildImportSummary buildImportSummary;
	
	@Before
	public void setupTest() {
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}
		try {
			FileUtils.cleanDirectory(rootDirectory);
		} catch (IOException e) {
			fail();
		}
		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(rootDirectory);
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
	public void noDerivedSubfoldersOfTheBuildFolderAreCopied() {
		givenLastSuccessfulScenarioBuildIsEnabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExists();
		givenBuildImportSummaryWithStatusSuccess();
		givenImportedBuildHasADerivedFolder();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectDerivedFolderWasNotCopied();
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
	public void theUseCaseXmlOfTheLatestBuildIsUsed() {
		// TODO
	}
	
	@Test
	public void derivedFilesInTheUseCaseFolderAreNotCopied() {
		// TODO
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
				LastSuccessfulScenariosBuildRepository.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
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
		File useCaseDirectory = new File(importedBuildDirectory, encode(useCases[0]));
		return useCaseDirectory;
	}
	
	private void givenImportedBuildHasADerivedFolder() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		File derivedFolder = new File(importedBuildDirectory, "something.derived");
		derivedFolder.mkdirs();
		assertTrue(derivedFolder.exists());
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
	
	private Calendar getCalendarForNowMinusOneDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DATE_NOW);
		calendar.add(Calendar.DATE, -1);
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
	
	private void createOnlySecondAndThirdScenario(final File useCaseFolder) {
		createSuccessfulScenario(useCaseFolder, scenarios[1], DATE_NOW);
		createSuccessfulScenario(useCaseFolder, scenarios[2], DATE_NOW);
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
			createUseCase(buildFolder, useCase);
		}
	}
	
	private void createUseCaseTwoAndThree() {
		File buildFolder = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		createUseCase(buildFolder, useCases[1]);
		createUseCase(buildFolder, useCases[2]);
	}
	
	private void createUseCase(final File importedBuildDirectory, final String useCaseName) {
		File useCaseDirectory = new File(importedBuildDirectory, encode(useCaseName));
		useCaseDirectory.mkdirs();
		assertTrue(useCaseDirectory.exists());
	}
	
	private void createSuccessfulScenario(final File useCaseFolder, final String scenarioName, final Date date) {
		File scenarioDirectory = new File(useCaseFolder, encode(scenarioName));
		scenarioDirectory.mkdirs();
		assertTrue(scenarioDirectory.exists());
		
		File scenarioFile = new File(scenarioDirectory, FILE_NAME_SCENARIO);
		Scenario scenario = new Scenario();
		scenario.setName(scenarioName);
		scenario.setStatus(Status.SUCCESS);
		scenario.addDetail(BUILD_DATE_FOR_TEST_KEY, date);
		ScenarioDocuXMLFileUtil.marshal(scenario, scenarioFile);
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
					+ URLEncoder.encode(LastSuccessfulScenariosBuildRepository.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME,
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
	
	private void expectDerivedFolderWasNotCopied() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		String[] folderNames = lastSuccessfulScenarioBuildDirectory.list();
		for (String folderName : folderNames) {
			File folder = new File(lastSuccessfulScenarioBuildDirectory, folderName);
			if (folder.isDirectory()) {
				assertFalse(folderName.endsWith(".derived"));
			}
		}
	}
	
	private void expectBuildXmlWithValidContentExists() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenariosBuildDirectory();
		File buildXml = new File(lastSuccessfulScenarioBuildDirectory, "build.xml");
		assertTrue(buildXml.exists());
		
		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(rootDirectory);
		Build build = scenarioDocuReader.loadBuild(BUILD_IDENTIFIER.getBranchName(),
				LastSuccessfulScenariosBuildRepository.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		
		assertEquals(Status.SUCCESS.getKeyword(), build.getStatus());
		assertEquals("latest successful scenarios.derived", build.getName());
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
	
	private String encode(final String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			fail();
			return null;
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
	
}
