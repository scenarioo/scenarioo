package org.scenarioo.dao.design.entities;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.dao.design.DesignFiles;
import org.scenarioo.model.design.entities.StepSketch;

public class DesignFilesTest {

	private static final File rootDirectory = new File("tmp");
	private static DesignFiles designFiles;

	private static final String BRANCH_NAME = "Test Branch";
	private static final String ISSUE_NAME = "This is our first Test Issue";
	private static final String ISSUE_NAME_WITH_UNSAFE_CHARACTERS = "This \\is our /first/ Test Issue";
	private static final String SCENARIO_SKETCH = "This is our first Test Scenario Sketch";
	private static final StepSketch SKETCH_STEP;

	static {
		SKETCH_STEP = new StepSketch();
		SKETCH_STEP.setStepSketchId("1");
		SKETCH_STEP
				.setSvgXmlString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" height=\"100%\" "
						+ "width=\"100%\" version=\"1.1\" "
						+ "xmlns=\"http://www.w3.org/2000/svg\" style=\"background-color:#fff; "
						+ "border:2px solid #ddd;\" id=\"drawingPad\">"
						+ "<desc>Created with svg.js [http://svgjs.com]</desc>"
						+ "<defs id=\"SvgjsDefs1000\"></defs>"
						+ "<rect fill=\"#00ff33\" y=\"23\" x=\"29\" height=\"106\" width=\"306\" "
						+ "id=\"SvgjsRect1005\"></rect>"
						+ "<ellipse fill=\"#ff0066\" cy=\"50\" cx=\"109\" ry=\"45\" rx=\"45\" "
						+ "id=\"SvgjsEllipse1006\"></ellipse></svg>");
	}

	@Before
	public void setupClass() {
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
		FileUtils.deleteDirectory(rootDirectory);
	}

	@Test
	public void createBranchDirectory() {
		File branchDirectory = designFiles.getBranchDirectory(BRANCH_NAME);
		branchDirectory.mkdir();
		assertTrue(branchDirectory.exists());
		assertEquals(branchDirectory.getName(), "Test+Branch");
	}


	@Test
	public void createIssueFile() {
		givenIssueDirectoryExists();

		designFiles.createIssueFile(BRANCH_NAME, ISSUE_NAME);
		File issueFile = designFiles.getIssueFile(BRANCH_NAME, ISSUE_NAME);
		assertTrue(issueFile.exists());
	}

	@Test
	public void createStepSketchDirectory() {
		givenScenarioSketchDirectoryExists();

		designFiles.createStepSketchDirectory(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH);
		File stepSketchDir = designFiles.getStepSketchesDirectory(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH);
		assertTrue(stepSketchDir.exists());
		assertEquals(stepSketchDir.getPath(),
				"tmp/Test+Branch/This+is+our+first+Test+Issue/This+is+our+first+Test+Scenario+Sketch/stepSketches");
	}

	@Test
	public void writeStepSketchToFile() {
		givenScenarioSketchDirectoryExists();
		designFiles.writeStepSketchToFile(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH, SKETCH_STEP);
		File stepSketchFile = designFiles.getStepSketchFile(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH,
				SKETCH_STEP.getStepSketchId());
		assertTrue(stepSketchFile.exists());
		assertEquals(stepSketchFile.getName(), "1.xml");
	}

	@Test
	public void createSVGDirectory() {
		designFiles.createStepSketchSVGDirectory(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH);
		File svgDir = designFiles.getSVGDirectory(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH);
		assertTrue(svgDir.exists());
		assertEquals(svgDir.getPath(),
				"tmp/Test+Branch/This+is+our+first+Test+Issue/This+is+our+first+Test+Scenario+Sketch/stepSketches/svg");
	}

	@Test
	public void writeSVGToFile() {
		designFiles.writeSVGToFile(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH, SKETCH_STEP);
		File svgFile = designFiles.getSVGFile(BRANCH_NAME, ISSUE_NAME, SCENARIO_SKETCH,
				SKETCH_STEP.getSketchFileName());
		assertTrue(svgFile.exists());
		assertEquals(svgFile.getName(), SKETCH_STEP.getSketchFileName());
	}

	// TODO #174 Fix or remove
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
	public void createIssueDirectory_withExistingBranchDirectoryAndNewIssueName_createsIssueDirectory() {
		givenBranchDirectoryExists();
		givenIssueDirectoryDoesNotExist();

		whenCreatingIssueDirectory();

		expectIssueDirectoryExists();
	}

	@Test
	public void createIssueDirectory_withBranchNameThatHasSpecialCharacters_createsIssueDirectory() {
		givenBranchDirectoryExists();
		givenIssueDirectoryDoesNotExist();

		whenCreatingIssueDirectoryForIssueNameWithSpecialCharacters();

		expectIssueDirectoryExistsWithEncodedUnsafeCharacters();
	}

	private void givenBranchDirectoryExists() {
		File branchDirectory = getBranchDirectory();
		branchDirectory.mkdirs();
		Assert.assertTrue(branchDirectory.exists());
	}

	private void givenIssueDirectoryDoesNotExist() {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_NAME));
		Assert.assertFalse(issueDirectory.exists());
	}

	private void givenIssueDirectoryExists() {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_NAME));
		issueDirectory.mkdirs();
		Assert.assertTrue(issueDirectory.exists());
	}

	private void givenScenarioSketchDirectoryExists() {
		File scenarioSketchDirectory = getScenarioSketchDirectory(FilesUtil.encodeName(ISSUE_NAME));
		scenarioSketchDirectory.mkdirs();
		Assert.assertTrue(scenarioSketchDirectory.exists());
	}

	private File getIssueDirectory(final String issueDirectoryName) {
		File branchDirectory = getBranchDirectory();
		File issueDirectory = new File(branchDirectory, issueDirectoryName);
		return issueDirectory;
	}

	private File getScenarioSketchDirectory(final String scenarioSketchDirectory) {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_NAME));
		return new File(issueDirectory, scenarioSketchDirectory);
	}

	private File getBranchDirectory() {
		File branchDirectory = new File(rootDirectory, FilesUtil.encodeName(BRANCH_NAME));
		return branchDirectory;
	}

	private void whenCreatingIssueDirectory() {
		createIssueDirectory(ISSUE_NAME);
	}

	private void whenCreatingIssueDirectoryForIssueNameWithSpecialCharacters() {
		createIssueDirectory(ISSUE_NAME_WITH_UNSAFE_CHARACTERS);
	}

	private void createIssueDirectory(final String issueName) {
		designFiles.createIssueDirectory(BRANCH_NAME, issueName);
	}

	private void expectIssueDirectoryExists() {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_NAME));
		Assert.assertTrue(issueDirectory.exists());
	}

	private void expectIssueDirectoryExistsWithEncodedUnsafeCharacters() {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_NAME_WITH_UNSAFE_CHARACTERS));
		Assert.assertTrue(issueDirectory.exists());
	}

}
