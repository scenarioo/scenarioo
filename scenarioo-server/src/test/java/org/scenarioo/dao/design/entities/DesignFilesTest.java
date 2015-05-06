package org.scenarioo.dao.design.entities;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DesignFilesTest {

	private static final File rootDirectory = new File("tmp");
	private static DesignFiles designFiles;
	private final String branchName = "Test Branch";
	private final String issueName = "This is our first Test Issue";

	@BeforeClass
	public static void setupClass() {
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}

		try {
			FileUtils.cleanDirectory(rootDirectory);
		} catch (IOException e) {
			fail();
		}

		designFiles = new DesignFiles(rootDirectory);
	}

	@AfterClass
	public static void removeTemporaryData() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}

	@Test
	public void createBranchDirectory() {
		File branchDirectory = designFiles.getBranchDirectory(branchName);
		branchDirectory.mkdir();
		assertTrue(branchDirectory.exists());
		assertEquals(branchDirectory.getName(), "Test+Branch");
	}

	@Test
	public void createIssueDirectory() {
		File issueDir = createAndGetIssueDirectory(issueName);
		assertTrue(issueDir.exists());
		assertEquals(issueDir.getName(), "This+is+our+first+Test+Issue");
	}

	@Test
	public void createIssueDirectoryWithUnsafeChars() {
		String issueNameWithUnsafeChars = "This \\is our /first/ Test Issue";
		File issueDir = createAndGetIssueDirectory(issueNameWithUnsafeChars);
		assertTrue(issueDir.exists());
		assertEquals(issueDir.getName(), "This+%5Cis+our+%2Ffirst%2F+Test+Issue");
	}

	/*
	 * @Test
	 * public void createIssueDirectoryWithVeryLongName() {
	 * String tooLongIssueName = "Limits on the Length of File Names and Paths. "
	 * + "There are also restrictions on the length of a file name and the "
	 * + "length of the path. Conflicting numbers are to be found on the Internet "
	 * + "because certain subtleties are often overlooked. I will try to make the "
	 * + "various length restrictions clear.";
	 * File issueDir = createAndGetIssueDirectory(tooLongIssueName);
	 * assertTrue(issueDir.exists());
	 * }
	 */

	@Test
	public void createIssueFile() {
		designFiles.createIssueFile(branchName, issueName);
		File issueFile = designFiles.getIssueFile(branchName, issueName);
		assertTrue(issueFile.exists());
	}

	private File createAndGetIssueDirectory(final String issueName) {
		designFiles.createIssueDirectory(branchName, issueName);
		File issueDir = designFiles.getIssueDirectory(branchName, issueName);
		return issueDir;
	}

}
