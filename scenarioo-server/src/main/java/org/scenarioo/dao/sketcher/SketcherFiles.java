/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.dao.sketcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.batik.ext.awt.image.codec.png.PNGRegistryEntry;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.sketcher.Issue;
import org.scenarioo.model.sketcher.ScenarioSketch;
import org.scenarioo.model.sketcher.StepSketch;

/**
 * TODO #478 remove inconsistencies and improve design:
 * 1. rename to DesignFiles ? Or better 'DesignDataFiles'.
 * 2. Also rename the package.
 * 3. Merge with SketcherReader and name it DesignDataDao (because it also contains write functionality not just file path getters)
 * 4. Document the responsibility of this class here
 * 5. consider my review comment for internal method #savePng
 */
public class SketcherFiles {

	private static final Logger LOGGER = Logger.getLogger(SketcherFiles.class);

	private static final String SKETCH_PNG_FILENAME = "sketch.png";
	private static final String SKETCH_SVG_FILENAME = "sketch.svg";
	private static final String STEP_SKETCH_XML_FILENAME = "stepSketch.xml";
	private static final String SCENARIO_SKETCH_XML_FILENAME = "scenarioSketch.xml";
	private static final String ISSUE_XML_FILENAME = "issue.xml";

	private final File rootDirectory;

	private final PNGTranscoder transcoder = new PNGTranscoder();

	public SketcherFiles(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public File getBranchDirectory(final String branchName) {
		return new File(rootDirectory, FilesUtil.encodeName(branchName));
	}

	public File getIssueDirectory(final String branchName, final String issueId) {
		return new File(getBranchDirectory(branchName), issueId);
	}

	public File getIssueFile(final String branchName, final String issueId) {
		return new File(getIssueDirectory(branchName, issueId), ISSUE_XML_FILENAME);
	}

	public List<File> getIssueFiles(final String branchName) {
		return FilesUtil.getListOfFilesFromSubdirs(getBranchDirectory(branchName), ISSUE_XML_FILENAME);
	}

	public File getScenarioSketchDirectory(final String branchName, final String issueId,
			final String scenarioSketchId) {
		return new File(getIssueDirectory(branchName, issueId), scenarioSketchId);
	}

	public File getScenarioSketchFile(final String branchName, final String issueId, final String scenarioSketchId) {
		return new File(getScenarioSketchDirectory(branchName, issueId, scenarioSketchId),
				SCENARIO_SKETCH_XML_FILENAME);
	}

	public List<File> getScenarioSketchFiles(final String branchName, final String issueId) {
		return FilesUtil.getListOfFilesFromSubdirs(getIssueDirectory(branchName, issueId),
				SCENARIO_SKETCH_XML_FILENAME);
	}

	public File getStepSketchDirectory(final String branchName, final String issueId,
			final String scenarioSketchId, final String stepSketchId) {
		return new File(getScenarioSketchDirectory(branchName, issueId, scenarioSketchId), stepSketchId);
	}

	public File getStepSketchXmlFile(final String branchName, final String issueId, final String scenarioSketchId,
			final String stepSketchId) {
		return new File(getStepSketchDirectory(branchName, issueId, scenarioSketchId, stepSketchId),
				STEP_SKETCH_XML_FILENAME);
	}

	public File getStepSketchXmlFile(final String branchName, final String issueId,
			final String scenarioSketchId, final StepSketch stepSketch) {
		return getStepSketchXmlFile(branchName, issueId, scenarioSketchId, stepSketch.getStepSketchId());
	}

	public File getStepSketchSvgFile(final String branchName, final String issueId, final String scenarioSketchId,
			final String stepSketchId) {
		return new File(getStepSketchDirectory(branchName, issueId, scenarioSketchId, stepSketchId),
				SKETCH_SVG_FILENAME);
	}

	// TODO #478: another inconcistency, about png filename:
	// strange that we have to pass the png filename here as a parameter.
	// Looks like a placebo parameter (also in the REST service)
	// because in the store method (see below) this name is allways put to a constant value.
	// Shouldnt this be the same as for svg, where this class knows best about the filename.
	public File getStepSketchPngFile(final String branchName, final String issueId, final String scenarioSketchId,
			final String stepSketchId, final String pngFilename) {
		return new File(getStepSketchDirectory(branchName, issueId, scenarioSketchId, stepSketchId), pngFilename);
	}

	public List<File> getStepSketchFiles(final String branchName, final String issueId,
			final String scenarioSketchId) {
		return FilesUtil.getListOfFilesFromSubdirs(getScenarioSketchDirectory(branchName, issueId, scenarioSketchId),
				STEP_SKETCH_XML_FILENAME);
	}


	public void persistIssue(final String branchName, final Issue issue) {
		final File destinationFile = getIssueFile(branchName, issue.getIssueId());
		createParentDirectoryIfNeeded(destinationFile);
		ScenarioDocuXMLFileUtil.marshal(issue, destinationFile);
	}

	public void persistScenarioSketch(final String branchName, final String issueId,
			final ScenarioSketch scenarioSketch) {
		final File scenarioSketchFile = getScenarioSketchFile(branchName, issueId, scenarioSketch.getScenarioSketchId());
		createParentDirectoryIfNeeded(scenarioSketchFile);
		ScenarioDocuXMLFileUtil.marshal(scenarioSketch, scenarioSketchFile);
	}

	/**
	 * Ensures that a file's parent directory is created if it does not exist yet.
	 * Will throw an exception, if the directory was not created properly.
     */
	private void createParentDirectoryIfNeeded(final File file) {
		File directory = file.getParentFile();
		directory.mkdirs();
		if (!directory.exists()) {
			throw new RuntimeException("Directory was not able to be created (check user rights for the directory and available disk space): " + directory.getAbsolutePath());
		}
	}

	public void persistStepSketch(final String branchName, final String issueId,
			final String scenarioSketchId, final StepSketch stepSketch) {
		final File stepSketchXmlFile = getStepSketchXmlFile(branchName, issueId, scenarioSketchId, stepSketch);
		createParentDirectoryIfNeeded(stepSketchXmlFile);
		ScenarioDocuXMLFileUtil.marshal(stepSketch, stepSketchXmlFile);
	}

	public void persistSketchAsSvgAndPng(final String branchName, final String issueId,
			final String scenarioSketchId, final StepSketch stepSketch) {
		storeSvgFile(branchName, issueId, scenarioSketchId, stepSketch);
		storePngFile(branchName, issueId, scenarioSketchId, stepSketch.getStepSketchId());
	}

	/**
	 * Create Png file from Svg file.
	 *
	 * Precondition: SVG file allready has to exist!
	 *
	 * Review comment by rolf: isnt that a performance issue? Why do we read the SVG from file system, if we allready have it in the memory as a string?
     */
	private void storePngFile(final String branchName, final String issueId, final String scenarioSketchId,
			final String stepSketchId) {
		File svgFile = getStepSketchSvgFile(branchName, issueId, scenarioSketchId, stepSketchId);
		File pngFile = getStepSketchPngFile(branchName, issueId, scenarioSketchId, stepSketchId,
				SKETCH_PNG_FILENAME);

		try {
			final FileInputStream istream = new FileInputStream(svgFile);
			final ImageTagRegistry registry = ImageTagRegistry.getRegistry();
			registry.register(new PNGRegistryEntry());
			final TranscoderInput input = new TranscoderInput(istream);
			final FileOutputStream outputStream = new FileOutputStream(pngFile);
			final TranscoderOutput output = new TranscoderOutput(outputStream);
			transcoder.transcode(input, output);
			outputStream.flush();
			outputStream.close();
		} catch (final Exception e) {
			LOGGER.error("Could not save PNG version of sketch.", e);
			throw new RuntimeException(e);
		}
	}

	private void storeSvgFile(final String branchName, final String issueId, final String scenarioSketchId,
			final StepSketch stepSketch) {
		File svgFile = getStepSketchSvgFile(branchName, issueId, scenarioSketchId, stepSketch.getStepSketchId());
		createParentDirectoryIfNeeded(svgFile);
		try {
			final FileWriter writer = new FileWriter(svgFile);
			writer.write(stepSketch.getSvgXmlString());
			writer.close();

			stepSketch.setSketchFileName(svgFile.getName());
			final File destinationFile = getStepSketchXmlFile(branchName, issueId, scenarioSketchId,
					stepSketch.getStepSketchId());
			ScenarioDocuXMLFileUtil.marshal(stepSketch, destinationFile);
		} catch (final IOException e) {
			LOGGER.error("Could not write SVG file.", e);
			throw new RuntimeException(e);
		}
	}

	public void copyOriginalScreenshot(final File originalScreenshot, final String branchName, final String issueId,
			final String scenarioSketchId, final String stepSketchId) {
		try {
			final File destination = new File(getStepSketchDirectory(branchName, issueId, scenarioSketchId,
					stepSketchId), "original.png");
			FileUtils.copyFile(originalScreenshot, destination);
		} catch (final IOException e) {
			LOGGER.error("Couldn't copy original screenshot to stepsketch!", e);
			throw new RuntimeException(e);
		}
	}

}
