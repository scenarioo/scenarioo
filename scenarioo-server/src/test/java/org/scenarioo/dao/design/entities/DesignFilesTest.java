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
import org.scenarioo.dao.sketcher.SketcherFiles;
import org.scenarioo.model.design.entities.StepSketch;

public class DesignFilesTest {

	private static final File rootDirectory = new File("tmp");
	private static SketcherFiles designFiles;

	private static final String BRANCH_NAME = "Test Branch";
	private static final String ISSUE_ID = "ABCD1234";
	private static final String SCENARIO_SKETCH_ID = "1111AAAA";
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

		designFiles = new SketcherFiles(rootDirectory);
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
	public void createStepSketchDirectory() {
		givenScenarioSketchDirectoryExists();

		String stepSketchId = "1";
		designFiles.createStepSketchDirectory(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID, stepSketchId);
		File stepSketchDir = designFiles.getStepSketchDirectory(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				stepSketchId);
		assertTrue(stepSketchDir.exists());
		assertEquals(stepSketchDir.getPath(),
				"tmp/Test+Branch/ABCD1234/1111AAAA/1");
	}

	@Test
	public void writeStepSketchToFile() {
		givenScenarioSketchDirectoryExists();
		givenStepSketchDirectoryExists();
		designFiles.persistStepSketch(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID, SKETCH_STEP);
		File stepSketchXmlFile = designFiles.getStepSketchXmlFile(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				SKETCH_STEP.getStepSketchId());
		assertTrue(stepSketchXmlFile.exists());
		assertEquals("stepSketch.xml", stepSketchXmlFile.getName());
	}

	@Test
	public void writeSvgToFile() {
		givenStepSketchDirectoryExists();
		designFiles.persistSketchAsSvgAndPng(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID, SKETCH_STEP);
		File svgFile = designFiles.getStepSketchSvgFile(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID,
				SKETCH_STEP.getStepSketchId());
		assertTrue(svgFile.exists());
		assertEquals(svgFile.getName(), SKETCH_STEP.getSketchFileName());
	}

	@Test
	public void createIssueDirectory_withExistingBranchDirectoryAndNewIssueName_createsIssueDirectory() {
		givenBranchDirectoryExists();
		givenIssueDirectoryDoesNotExist();

		whenCreatingIssueDirectory();

		expectIssueDirectoryExists();
	}

	private void givenBranchDirectoryExists() {
		File branchDirectory = getBranchDirectory();
		branchDirectory.mkdirs();
		Assert.assertTrue(branchDirectory.exists());
	}

	private void givenIssueDirectoryDoesNotExist() {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_ID));
		Assert.assertFalse(issueDirectory.exists());
	}

	private void givenScenarioSketchDirectoryExists() {
		File scenarioSketchDirectory = getScenarioSketchDirectory(ISSUE_ID);
		scenarioSketchDirectory.mkdirs();
		Assert.assertTrue(scenarioSketchDirectory.exists());
	}

	private void givenStepSketchDirectoryExists() {
		designFiles.createStepSketchDirectory(BRANCH_NAME, ISSUE_ID, SCENARIO_SKETCH_ID, "1");
	}

	private File getIssueDirectory(final String issueDirectoryName) {
		File branchDirectory = getBranchDirectory();
		File issueDirectory = new File(branchDirectory, issueDirectoryName);
		return issueDirectory;
	}

	private File getScenarioSketchDirectory(final String scenarioSketchDirectory) {
		File issueDirectory = getIssueDirectory(FilesUtil.encodeName(ISSUE_ID));
		return new File(issueDirectory, scenarioSketchDirectory);
	}

	private File getBranchDirectory() {
		File branchDirectory = new File(rootDirectory, FilesUtil.encodeName(BRANCH_NAME));
		return branchDirectory;
	}

	private void whenCreatingIssueDirectory() {
		createIssueDirectory(ISSUE_ID);
	}

	private void createIssueDirectory(final String issueName) {
		designFiles.createIssueDirectoryIfNotNecessary(BRANCH_NAME, issueName);
	}

	private void expectIssueDirectoryExists() {
		File issueDirectory = getIssueDirectory(ISSUE_ID);
		Assert.assertTrue(issueDirectory.exists());
	}

}
