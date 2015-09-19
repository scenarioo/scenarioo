package org.scenarioo.dao.design.entities;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.dao.design.DesignFiles;
import org.scenarioo.model.design.entities.StepSketch;

public class DesignFilesTest {

	private static final File rootDirectory = new File("tmp");
	private static DesignFiles designFiles;
	private final String branchName = "Test Branch";
	private final String issueName = "This is our first Test Issue";
	private final String proposalName = "This is our first Test Proposal";

	private static StepSketch sketchStep = new StepSketch();

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

		sketchStep.setSketchStepName(1);
		sketchStep
				.setSketch("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
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

	@Test
	public void createSketchStepDirectory() {
		designFiles.createSketchStepDirectory(branchName, issueName, proposalName);
		File sketchStepDir = designFiles.getSketchStepsDirectory(branchName, issueName, proposalName);
		assertTrue(sketchStepDir.exists());
		assertEquals(sketchStepDir.getPath(),
				"tmp/Test+Branch/This+is+our+first+Test+Issue/This+is+our+first+Test+Proposal/sketchSteps");
	}

	@Test
	public void writeSketchStepToFile() {
		designFiles.writeSketchStepToFile(branchName, issueName, proposalName, sketchStep);
		File sketchStepFile = designFiles.getSketchStepFile(branchName, issueName, proposalName,
				sketchStep.getSketchStepName());
		assertTrue(sketchStepFile.exists());
		assertEquals(sketchStepFile.getName(), "001.xml");
	}

	@Test
	public void createSVGDirectory() {
		designFiles.createSketchStepSVGDirectory(branchName, issueName, proposalName);
		File svgDir = designFiles.getSVGDirectory(branchName, issueName, proposalName);
		assertTrue(svgDir.exists());
		assertEquals(svgDir.getPath(),
				"tmp/Test+Branch/This+is+our+first+Test+Issue/This+is+our+first+Test+Proposal/sketchSteps/svg");
	}

	@Test
	public void writeSVGToFile() {
		designFiles.writeSVGToFile(branchName, issueName, proposalName, sketchStep);
		File svgFile = designFiles.getSVGFile(branchName, issueName, proposalName,
				sketchStep.getSketchFileName());
		assertTrue(svgFile.exists());
		assertEquals(svgFile.getName(), sketchStep.getSketchFileName());
	}

	private File createAndGetIssueDirectory(final String issueName) {
		designFiles.createIssueDirectory(branchName, issueName);
		File issueDir = designFiles.getIssueDirectory(branchName, issueName);
		return issueDir;
	}

}
