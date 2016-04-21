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

import static org.scenarioo.api.rules.CharacterChecker.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * All file access for the issue sketcher feature.
 */
public class SketcherDao {

	public static final String SKETCHER_DIRECTORY = "scenarioo-application-data/sketcher";

	private static final Logger LOGGER = Logger.getLogger(SketcherDao.class);

	private static final String ORIGINAL_PNG_FILENAME = "original.png";
	private static final String SKETCH_PNG_FILENAME = "sketch.png";
	private static final String SKETCH_SVG_FILENAME = "sketch.svg";
	private static final String STEP_SKETCH_XML_FILENAME = "stepSketch.xml";
	private static final String SCENARIO_SKETCH_XML_FILENAME = "scenarioSketch.xml";
	private static final String ISSUE_XML_FILENAME = "issue.xml";

	private final File sketcherDirectory;

	private final PNGTranscoder transcoder = new PNGTranscoder();


	/**
	 * The {@link ConfigurationRepository} has to be initialized before you create an instance of this DAO.
	 */
	public SketcherDao() {
		final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE.getConfigurationRepository();
		File documentationDataDirectory = configurationRepository.getDocumentationDataDirectory();
		this.sketcherDirectory = new File(documentationDataDirectory, SketcherDao.SKETCHER_DIRECTORY);
	}

	public File getRootDirectory() {
		return sketcherDirectory;
	}

	public File getBranchDirectory(final String branchName) {
		return new File(sketcherDirectory, FilesUtil.encodeName(branchName));
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

	/**
	 * @param pngFilename
	 *            Specify whether you want to load the original PNG file or the sketch PNG file.
	 */
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
		storePngFile(branchName, issueId, scenarioSketchId, stepSketch.getStepSketchId(), stepSketch.getSvgXmlString());
	}

	/**
	 * Create PNG file from SVG file.
	 */
	private void storePngFile(final String branchName, final String issueId, final String scenarioSketchId,
			final String stepSketchId, final String svgString) {
		File pngFile = getStepSketchPngFile(branchName, issueId, scenarioSketchId, stepSketchId,
				SKETCH_PNG_FILENAME);

		try {
			final ImageTagRegistry registry = ImageTagRegistry.getRegistry();
			registry.register(new PNGRegistryEntry());
			final TranscoderInput input = new TranscoderInput(new StringReader(svgString));
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
					stepSketchId), ORIGINAL_PNG_FILENAME);
			FileUtils.copyFile(originalScreenshot, destination);
		} catch (final IOException e) {
			LOGGER.error("Couldn't copy original screenshot to stepsketch!", e);
			throw new RuntimeException(e);
		}
	}

	public List<Issue> loadIssues(final String branchName) {
		final List<File> files = this.getIssueFiles(checkIdentifier(branchName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Issue.class, files);
	}

	public Issue loadIssue(final String branchName, final String issueId) {
		final File file = this.getIssueFile(checkIdentifier(branchName), checkIdentifier(issueId));
		return ScenarioDocuXMLFileUtil.unmarshal(Issue.class, file);
	}

	public List<ScenarioSketch> loadScenarioSketches(final String branchName, final String issueId) {
		final List<File> files = this.getScenarioSketchFiles(checkIdentifier(branchName),
				checkIdentifier(issueId));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ScenarioSketch.class, files);
	}

	public ScenarioSketch loadScenarioSketch(final String branchName, final String issueId,
			final String scenarioSketchId) {
		final File file = this.getScenarioSketchFile(checkIdentifier(branchName), checkIdentifier(issueId),
				checkIdentifier(scenarioSketchId));
		return ScenarioDocuXMLFileUtil.unmarshal(ScenarioSketch.class, file);
	}

	public List<StepSketch> loadStepSketches(final String branchName, final String issueId,
			final String scenarioSketchId) {
		final List<File> files = this.getStepSketchFiles(checkIdentifier(branchName), issueId, scenarioSketchId);
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(StepSketch.class, files);
	}

	public StepSketch loadStepSketch(final String branchName, final String issueId,
			final String scenarioSketchId, final String stepSketchId) {
		final File file = this.getStepSketchXmlFile(checkIdentifier(branchName), checkIdentifier(issueId),
				checkIdentifier(scenarioSketchId), stepSketchId);
		return ScenarioDocuXMLFileUtil.unmarshal(StepSketch.class, file);
	}

}
