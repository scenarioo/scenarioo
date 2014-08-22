package org.scenarioo.dao.basic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.rest.base.BuildIdentifier;

public class FileSystemOperationsDaoTest {
	
	private static final File rootDirectory = new File("tmp");
	private static final BuildIdentifier buildIdentifier = new BuildIdentifier("branch", "build");
	
	private static FileSystemOperationsDao fileSystemOperationsDao;
	
	@BeforeClass
	public static void setupClass() {
		rootDirectory.mkdir();
		fileSystemOperationsDao = new FileSystemOperationsDao();
	}
	
	@AfterClass
	public static void removeTemporaryData() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}
	
	@Test
	public void deleteBuild_deletesTheEntireBuildFolderWithAllSubfoldersAndFiles() {
		givenBuildFolderContainingSubfoldersAndFiles();
		
		fileSystemOperationsDao.deleteBuild(rootDirectory, buildIdentifier);
		
		expectBuildFolderIsDeleted();
		expectBranchFolderStillExists();
	}
	
	@Test
	public void createBuildFolderIfItDoesNotExist_createsTheFolderBecauseIfItDoesNotExist() {
		givenBuildFolderDoesNotExist();
		
		fileSystemOperationsDao.createBuildFolderIfItDoesNotExist(rootDirectory, buildIdentifier);
		
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
		File buildFolder = new File(rootDirectory, "/" + buildIdentifier.getBranchName() + "/"
				+ buildIdentifier.getBuildName());
		return buildFolder;
	}
	
}
