package org.scenarioo.dao.aggregates;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.rest.base.BuildIdentifier;

public class ScenarioDocuAggregationDAOTest {
	
	private static final File rootDirectory = new File("tmp");
	private static final BuildIdentifier buildIdentifier = new BuildIdentifier("branch", "build");
	
	private static ScenarioDocuAggregationDAO scenarioDocuAggregationDAO;
	
	@BeforeClass
	public static void setupClass() {
		rootDirectory.mkdir();
		scenarioDocuAggregationDAO = new ScenarioDocuAggregationDAO(rootDirectory);
	}
	
	@AfterClass
	public static void removeTemporaryData() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}
	
	@Test
	public void deleteBuildDeletesTheEntireFolderWithAllSubfoldersAndFiles() {
		givenBuildFolderContainingSubfoldersAndFiles();
		
		scenarioDocuAggregationDAO.deleteBuild(buildIdentifier);
		
		expectBuildFolderIsDeleted();
		expectBranchFolderStillExists();
	}
	
	private void expectBranchFolderStillExists() {
		File branchFolder = new File(rootDirectory, "/" + buildIdentifier.getBranchName());
		assertTrue(branchFolder.exists());
	}
	
	private void expectBuildFolderIsDeleted() {
		File buildFolder = getBuildFolder();
		assertFalse(buildFolder.exists());
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
	
	private File getBuildFolder() {
		File buildFolder = new File(rootDirectory, "/" + buildIdentifier.getBranchName() + "/"
				+ buildIdentifier.getBuildName());
		return buildFolder;
	}
	
}
