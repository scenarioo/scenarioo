package org.scenarioo.dao.sketcher;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.model.sketcher.Issue;
import org.scenarioo.model.sketcher.StepSketch;

public class DesignFilesTest {

	private static final File ROOT_DIRECTORY = new File("tmp/scenarioo-application-data/sketcher");
	private static final String BRANCH_NAME = "Test Branch";
	private static final String ISSUE_ID = "issueID";
	private static final String SCENARIO_SKETCH_ID = "scenarioSketchID";
	private static final String STEP_SKETCH_ID = "1";
	private static final StepSketch STEP_SKETCH = createStepSketch(STEP_SKETCH_ID);

	private SketcherFiles designFiles;


	@Before
	public void setup() throws IOException {

		// General given for all tests: given none of the directories in root directory yet exist.
		if (!ROOT_DIRECTORY.exists()) {
			ROOT_DIRECTORY.mkdirs();
		}
		FileUtils.cleanDirectory(ROOT_DIRECTORY);

		designFiles = new SketcherFiles(ROOT_DIRECTORY);
	}

	@AfterClass
	public static void removeGeneratedTestData() throws IOException {
		FileUtils.deleteDirectory(ROOT_DIRECTORY);
	}

	@Test
	public void persistIssue() {
		givenIssueDirectoryDoesNotExist();
		designFiles.persistIssue(BRANCH_NAME, createIssue(ISSUE_ID));
		expectIssueDirectoryExists();
		expectIssuePersisted();
	}

	@Test
	public void persistStepSketch() {
		givenIssueDirectoryDoesNotExist();
		designFiles.persistStepSketch(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID, STEP_SKETCH);
		expectStepSketchDirectoryCreated();
		expectStepSketchPersisted();
	}

	@Test
	public void persistSketchAsSvgAndPng() {
		givenIssueDirectoryDoesNotExist();
		designFiles.persistSketchAsSvgAndPng(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID, STEP_SKETCH);
		expectStepSketchDirectoryCreated();
		expectSketchAsSvgPersisted();
		expectSketchAsPngPersisted();
	}

	private void givenIssueDirectoryDoesNotExist() {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_ID));
		Assert.assertFalse(issueDirectory.exists());
	}

	private void expectIssuePersisted() {
		File issueFile = designFiles.getIssueFile(BRANCH_NAME, ISSUE_ID);
		assertTrue(issueFile.exists());
	}

	private void expectStepSketchDirectoryCreated() {
		File stepSketchDir = designFiles.getStepSketchDirectory(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				STEP_SKETCH_ID);
		assertTrue("Expect step sketch directory (and all parent folders) to be created.", stepSketchDir.exists());
		assertEquals("Expected path for step sketch file", filePath(ROOT_DIRECTORY, "Test+Branch", ISSUE_ID, SCENARIO_SKETCH_ID, STEP_SKETCH_ID), stepSketchDir.getPath());
	}

	private void expectStepSketchPersisted() {
		File stepSketchXmlFile = designFiles.getStepSketchXmlFile(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				STEP_SKETCH.getStepSketchId());
		assertTrue(stepSketchXmlFile.exists());
		assertEquals("stepSketch.xml", stepSketchXmlFile.getName());
	}

	private void expectSketchAsSvgPersisted() {
		File svgFile = designFiles.getStepSketchSvgFile(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				STEP_SKETCH.getStepSketchId());
		assertTrue(svgFile.exists());
		assertEquals(svgFile.getName(), STEP_SKETCH.getSketchFileName());
	}

	private void expectSketchAsPngPersisted() {
		File pngFile = designFiles.getStepSketchPngFile(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				STEP_SKETCH.getStepSketchId(), "sketch.png");
		assertTrue(pngFile.exists());
	}

	private void expectIssueDirectoryExists() {
		File issueDirectory = getIssueDirectory(ISSUE_ID);
		Assert.assertTrue(issueDirectory.exists());
	}

	private File getIssueDirectory(final String issueDirectoryName) {
		File branchDirectory = getBranchDirectory();
		return new File(branchDirectory, issueDirectoryName);
	}

	private File getBranchDirectory() {
		return new File(ROOT_DIRECTORY, FilesUtil.encodeName(BRANCH_NAME));
	}

	private static Issue createIssue(String issueID) {
		Issue issue = new Issue();
		issue.setIssueId(issueID);
		issue.setBranchName(BRANCH_NAME);
		issue.setName("A dummy test issue");
		return issue;
	}

	private static StepSketch createStepSketch(String id) {
		StepSketch stepSketch = new StepSketch();
		stepSketch.setStepSketchId(id);
		stepSketch
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
		return stepSketch;
	}

	/**
	 * This is needed to create expected file pathes, because on windows, file pathes are produced with "\" instead of "/" and the test should run when developing on windows.
	 */
	private static String filePath(File rootDir, String ... directoryNames) {
		File filePath = rootDir;
		for (String dirName : directoryNames) {
			filePath = new File(filePath, dirName);
		}
		return filePath.getPath();
	}

}
