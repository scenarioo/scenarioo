package org.scenarioo.dao.basic;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemOperationsDaoTest {

	private final File rootDirectory = new File("tmp");
	private final BuildIdentifier buildIdentifier = new BuildIdentifier("branch", "build");

	private static FileSystemOperationsDao fileSystemOperationsDao;

	@BeforeEach
	void setupClass() {
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}

		try {
			FileUtils.cleanDirectory(rootDirectory);
		} catch (IOException e) {
			fail();
		}

		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(rootDirectory);

		fileSystemOperationsDao = new FileSystemOperationsDao();
	}

	@AfterEach
	void removeTemporaryData() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}

	@Test
	void deleteBuild_deletesTheEntireBuildFolderWithAllSubfoldersAndFiles() {
		givenBuildFolderContainingSubfoldersAndFiles();

		fileSystemOperationsDao.deleteBuild(buildIdentifier);

		expectBuildFolderIsDeleted();
		expectBranchFolderStillExists();
	}

	@Test
	void createBuildFolderIfItDoesNotExist_createsTheFolderBecauseIfItDoesNotExist() {
		givenBuildFolderDoesNotExist();

		fileSystemOperationsDao.createBuildFolderIfItDoesNotExist(buildIdentifier);

		expectBuildFolderExists();
	}

	private void givenBuildFolderContainingSubfoldersAndFiles() {
		File buildFolder = getBuildFolder();
		buildFolder.mkdirs();
		assertTrue(buildFolder.exists());

		File subFolder = new File(buildFolder, "/subfolder");
		subFolder.mkdir();
		assertTrue(subFolder.exists());

		File someFile = new File(buildFolder, "/somefile.txt");
		try {
			someFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(someFile.exists());
	}

	private void givenBuildFolderDoesNotExist() {
		File buildFolder = getBuildFolder();
		assertFalse(buildFolder.exists());
	}

	private void expectBranchFolderStillExists() {
		File branchFolder = new File(rootDirectory, "/" + buildIdentifier.getBranchName());
		assertTrue(branchFolder.exists());
	}

	private void expectBuildFolderIsDeleted() {
		File buildFolder = getBuildFolder();
		assertFalse(buildFolder.exists());
	}

	private void expectBuildFolderExists() {
		File buildFolder = getBuildFolder();
		assertTrue(buildFolder.exists());
	}

	private File getBuildFolder() {
		return new File(rootDirectory, "/" + buildIdentifier.getBranchName() + "/"
				+ buildIdentifier.getBuildName());
	}

}
