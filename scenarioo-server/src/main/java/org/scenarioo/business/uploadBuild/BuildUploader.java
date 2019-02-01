package org.scenarioo.business.uploadBuild;

import java.io.*;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.ZipFileExtractor;
import org.scenarioo.utils.ZipFileExtractor.ZipFileExtractionException;

import com.google.common.base.Preconditions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public class BuildUploader {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private static final Logger LOGGER = Logger.getLogger(BuildUploader.class);

	/**
	 * Thrown if the uploaded ZIP file contains invalid data or if there's a problem while moving the build data into
	 * the correct place.
	 */
	public static class MoveBuildDataException extends Exception {
		public MoveBuildDataException(final String message) {
			super(message);
		}

		public MoveBuildDataException(final String message, final Exception e) {
			super(message, e);
		}
	}

	public static class BuildUploaderException extends Exception {
		public BuildUploaderException(final String message, final Exception e) {
			super(message, e);
		}
	}

	public ResponseEntity uploadBuild(final MultipartFile file) {
		Preconditions.checkArgument(!file.isEmpty());
		Preconditions.checkArgument(MediaType.APPLICATION_OCTET_STREAM_VALUE.equals(file.getContentType()));

		LOGGER.info("Receiving build by POST request.");

		try {
			saveAndExtractBuildAndStartImport(file);
		} catch (BuildUploaderException e) {
			LOGGER.error("An error occured while adding a build by POST request.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add build, see server log for details.\n");
		}

		return ResponseEntity.ok("Build successfully added to Scenarioo.\n");
	}

	private void saveAndExtractBuildAndStartImport(final MultipartFile file) throws BuildUploaderException {

		File documentationDataDirectory = configurationRepository.getDocumentationDataDirectory();
		File temporaryWorkDirectory = new File(documentationDataDirectory, "uploadedBuild_"
				+ Long.toString(new Date().getTime()));
		boolean tempDirCreated = temporaryWorkDirectory.mkdir();
		LOGGER.info("TempDir " + temporaryWorkDirectory.getAbsolutePath() + " created: " + tempDirCreated);
		File uploadedZipFile = new File(temporaryWorkDirectory, "uploadedFile.zip");

		try {
			file.transferTo(uploadedZipFile);
		} catch (IOException e) {
			throw new BuildUploaderException("Failed to write file: " + uploadedZipFile.getAbsolutePath(), e);
		}

		try {
			extractBuildAndStartImport(documentationDataDirectory, temporaryWorkDirectory, uploadedZipFile);
		} catch (ZipFileExtractionException e) {
			throw new BuildUploaderException("An error occured while extracting the Zip file.", e);
		} catch (MoveBuildDataException e) {
			throw new BuildUploaderException("An error occured while moving the build data.", e);
		} finally {
			try {
				FileUtils.deleteDirectory(temporaryWorkDirectory);
			} catch (IOException e) {
				throw new BuildUploaderException("Could not delete directory " + temporaryWorkDirectory, e);
			}
		}
	}

	private void extractBuildAndStartImport(final File documentationDataDirectory, final File temporaryWorkDirectory,
			final File targetFileForUploadedData) throws ZipFileExtractionException, MoveBuildDataException {
		File extractedZipFileDirectory = new File(temporaryWorkDirectory, "extracted");

		ZipFileExtractor.extractFile(targetFileForUploadedData, extractedZipFileDirectory);

		moveDataIntoCorrectDirectory(documentationDataDirectory, extractedZipFileDirectory);

		ScenarioDocuBuildsManager.INSTANCE.updateBuildsIfValidDirectoryConfigured();
	}

	private void moveDataIntoCorrectDirectory(final File documentationDataDirectory,
			final File extractedZipFileDirectory)
			throws MoveBuildDataException {
		File[] dirs = extractedZipFileDirectory.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		if (dirs.length > 1) {
			throw new MoveBuildDataException(
					"The ZIP file must only contain one directory, which has to be the branch directory.");
		}

		File sourceBranchDir = dirs[0];
		File targetBranchDir = new File(documentationDataDirectory, sourceBranchDir.getName());

		if (targetBranchDir.exists()) {
			moveBuildToExistingBranchDir(sourceBranchDir, targetBranchDir);
		} else {
			moveEntireBranchDirectory(sourceBranchDir, targetBranchDir);
		}
	}

	private void moveBuildToExistingBranchDir(final File sourceBranchDir, final File targetBranchDir)
			throws MoveBuildDataException {
		LOGGER.info("Moving build to existing branch folder. Target: " + targetBranchDir);
		try {
			File[] filesToMove = sourceBranchDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					// we don't want to copy branch.xml, it should already exist.
					return !"branch.xml".equals(name);
				}
			});

			if (filesToMove.length != 1 || !filesToMove[0].isDirectory()) {
				throw new MoveBuildDataException("The branch directory must only contain one build directory.");
			}

			if (targetBuildAlreadyExists(filesToMove[0].getName(), targetBranchDir)) {
				throw new MoveBuildDataException("The target build directory already exists");
			}

			FileUtils.moveToDirectory(filesToMove[0], targetBranchDir, false);
		} catch (IOException e) {
			throw new MoveBuildDataException("Problem while moving build data.", e);
		}
	}

	private boolean targetBuildAlreadyExists(final String folderName, final File targetBranchDir) {
		return new File(targetBranchDir, folderName).exists();
	}

	private void moveEntireBranchDirectory(final File sourceBranchDir, final File targetBranchDir)
			throws MoveBuildDataException {
		LOGGER.info("Moving entire branch folder because it does not exist yet. Target: " + targetBranchDir);
		try {
			FileUtils.moveDirectory(sourceBranchDir, targetBranchDir);
		} catch (IOException e) {
			throw new MoveBuildDataException("Problem while moving build data.", e);
		}
	}

}
