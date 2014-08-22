package org.scenarioo.business.builds;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

public class LastSuccessfulScenarioBuildTest {
	
	private static final BuildIdentifier BUILD_IDENTIFIER = new BuildIdentifier("branch", "build");
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
		givenBuildImportSummaryWithStatusFailed();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		// TODO Assert this somehow...
	}
	
	@Test
	public void ifTheLastSuccessfulScenarioBuildIsDisabledInTheConfigAnExistingSuchBuildIsDeleted() {
		givenLastSuccessfulScenarioBuildIsDisabledInConfiguration();
		givenLastSuccessfulScenarioBuildFolderExistsInCurrentBranch();
		givenBuildImportSummaryWithStatusSuccess();
		
		whenUpdatingLastSuccessfulScenarioBuild();
		
		expectLastSuccessfulScenarioBuildDirectoryDoesNotExist();
		expectBranchDirectoryExists();
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
		Configuration configuration = configurationRepository.getConfiguration();
		configuration.setCreateLastSuccessfulScenarioBuild(false);
		configurationRepository.updateConfiguration(configuration);
	}
	
	private void givenLastSuccessfulScenarioBuildFolderExistsInCurrentBranch() {
		File branchDirectory = getBranchDirectory();
		branchDirectory.mkdirs();
		assertTrue(branchDirectory.exists());
		
		File lastSuccessfulScenarioBuildDirectory = getLastSuccessfulScenarioBuildDirectory();
		lastSuccessfulScenarioBuildDirectory.mkdir();
		assertTrue(lastSuccessfulScenarioBuildDirectory.exists());
	}
	
	private File getBranchDirectory() {
		return new File(rootDirectory, BUILD_IDENTIFIER.getBranchName());
	}
	
	private File getLastSuccessfulScenarioBuildDirectory() {
		return new File(rootDirectory, BUILD_IDENTIFIER.getBranchName() + "/"
				+ LastSuccessfulScenarioBuild.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
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
	
}
