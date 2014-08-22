package org.scenarioo.business.builds;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.LastSuccessfulScenarioBuildRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

public class LastSuccessfulScenarioBuildTest {
	
	private static final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
	private final String[] useCases = new String[] { "Log%20in", "Send%20message", "Sign%20out" };
	
	private LastSuccessfulScenarioBuild lastSuccessfulScenarioBuild;
	private ConfigurationRepository configurationRepository;
	private final File rootDirectory = new File("tmp");
	private BuildImportSummary buildImportSummary;
	
	@Before
	public void setupTest() {
		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(rootDirectory);
		configurationRepository = RepositoryLocator.INSTANCE.getConfigurationRepository();
		lastSuccessfulScenarioBuild = new LastSuccessfulScenarioBuild();
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
	}
	
	private void givenLastSuccessfulScenarioBuildIsDisabledInConfiguration() {
		setConfigValueCreateLastSuccessfulScenarioBuildTo(false);
	}
	
	private void givenLastSuccessfulScenarioBuildIsEnabledInConfiguration() {
		setConfigValueCreateLastSuccessfulScenarioBuildTo(true);
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
		
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
		lastSuccessfulScenarioBuildDirectory.mkdir();
		assertTrue(lastSuccessfulScenarioBuildDirectory.exists());
	}
	
	private void givenLastSuccessfulScenarioBuildFolderDoesNotExist() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
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
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		for (String useCase : useCases) {
			createUseCase(importedBuildDirectory, useCase);
		}
	}
	
	private void givenImportedBuildHasADerivedFolder() {
		File importedBuildDirectory = getImportedBuildDirectory(buildImportSummary.getIdentifier());
		File derivedFolder = new File(importedBuildDirectory, "something.derived");
		derivedFolder.mkdirs();
		assertTrue(derivedFolder.exists());
	}
	
	private void createUseCase(final File importedBuildDirectory, final String usecaseNameUrlEncoded) {
		File useCaseDirectory = new File(importedBuildDirectory, usecaseNameUrlEncoded);
		useCaseDirectory.mkdirs();
		assertTrue(useCaseDirectory.exists());
	}
	
	private File getImportedBuildDirectory(final BuildIdentifier buildIdentifier) {
		return new File(rootDirectory, buildIdentifier.getBranchName() + "/" + buildIdentifier.getBuildName());
	}
	
	private File getBranchDirectory() {
		return new File(rootDirectory, BUILD_IDENTIFIER.getBranchName());
	}
	
	private File getLastSuccessfulScenarioBuildDirectory() {
		return new File(rootDirectory, BUILD_IDENTIFIER.getBranchName() + "/"
				+ LastSuccessfulScenarioBuildRepository.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
	}
	
	private BuildImportSummary getSummaryWith(final BuildImportStatus buildImportStatus) {
		BuildImportSummary summary = new BuildImportSummary();
		summary.setStatus(buildImportStatus);
		summary.setIdentifier(BUILD_IDENTIFIER);
		return summary;
	}
	
	private void whenUpdatingLastSuccessfulScenarioBuild() {
		lastSuccessfulScenarioBuild.updateLastSuccessfulScenarioBuild(buildImportSummary);
	}
	
	private void expectLastSuccessfulScenarioBuildDirectoryDoesNotExist() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
		assertFalse(lastSuccessfulScenarioBuildDirectory.exists());
	}
	
	private void expectBranchDirectoryExists() {
		File branchDirectory = getBranchDirectory();
		assertTrue(branchDirectory.exists());
	}
	
	private void expectLastSuccessfulScenarioBuildDirectoryExists() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
		assertTrue(lastSuccessfulScenarioBuildDirectory.exists());
	}
	
	private void expectTheThreeUseCasesExistInTheLastSuccessfulScenarioBuild() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
		for (String useCase : useCases) {
			assertUseCaseExists(lastSuccessfulScenarioBuildDirectory, useCase);
		}
	}
	
	private void assertUseCaseExists(final File lastSuccessfulScenarioBuildDirectory, final String useCase) {
		File useCaseFile = new File(lastSuccessfulScenarioBuildDirectory, useCase);
		assertTrue(useCaseFile.exists());
	}
	
	private void expectDerivedFolderWasNotCopied() {
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
		String[] folderNames = lastSuccessfulScenarioBuildDirectory.list();
		for (String folderName : folderNames) {
			assertFalse(folderName.endsWith(".derived"));
		}
	}
	
}
