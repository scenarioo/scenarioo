package org.scenarioo.business.uploadBuild;


import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

import com.google.common.base.Preconditions;

public class BuildUploader {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private static final Logger LOGGER = Logger.getLogger(BuildUploader.class);

	public Response uploadBuild(final MultipartFormDataInput formData, final String apiKey) {
		checkApiKeyIsCorrect(apiKey);
		InputPart inputPart = validateFormDataAndGetInputPart(formData);
		Preconditions.checkArgument(MediaType.APPLICATION_OCTET_STREAM_TYPE.equals(inputPart.getMediaType()),
				"Media type of input part must be application/octet-stream but is " + inputPart.getMediaType());

		LOGGER.info("Receiving build by POST request.");

		saveAndExtractBuildAndStartImport(inputPart);

		return Response.ok("Build successfully added to Scenarioo.").build();
	}

	private InputPart validateFormDataAndGetInputPart(final MultipartFormDataInput formData) {
		Preconditions.checkNotNull(formData);
		Preconditions.checkNotNull(formData.getFormDataMap());
		Preconditions.checkNotNull(formData.getFormDataMap().get("file"));
		Preconditions.checkArgument(formData.getFormDataMap().get("file").size() == 1);

		return formData.getFormDataMap().get("file").get(0);
	}

	private void checkApiKeyIsCorrect(final String apiKey) {
		Preconditions.checkNotNull(apiKey, "GET-Parameter apiKey is missing");

		String apiKeyInConfig = configurationRepository.getConfiguration().getSecretConfig().getApiKey();

		if (StringUtils.isBlank(apiKeyInConfig)) {
			throw new IllegalStateException(
					"No API key defined in config. Please define an API key by editing config.xml directly.");
		}

		if (apiKeyInConfig.length() < 30) {
			throw new IllegalStateException(
					"The API key in the config is too short. Please define an API key that has at least 30 characters.");
		}

		if (!apiKeyInConfig.equals(apiKey)) {
			throw new IllegalArgumentException("The provided API key is wrong.");
		}

		LOGGER.info("API key is correct.");
	}

	private void saveAndExtractBuildAndStartImport(final InputPart inputPart) {
		File documentationDataDirectory = configurationRepository.getDocumentationDataDirectory();
		File targetDir = new File(documentationDataDirectory, "uploadedBuild_"
				+ Long.toString(new Date().getTime()));
		File targetFileForUploadedData = new File(targetDir, ".zip");
		
		try {
			InputStream inputStream = inputPart.getBody(InputStream.class, null);
			FileUtils.copyInputStreamToFile(inputStream, targetFileForUploadedData);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write file.", e);
		}

		extractFile(targetFileForUploadedData, targetDir);

		moveDateIntoCorrectDirectory(documentationDataDirectory, targetDir);

		ScenarioDocuBuildsManager.INSTANCE.updateAllBuildsAndSubmitNewBuildsForImport();
	}

	private void extractFile(final File file, final File targetDir) {
		// From http://stackoverflow.com/a/13912353
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File entryDestination = new File(targetDir, entry.getName());
				if (entry.isDirectory()) {
					entryDestination.mkdirs();
				} else {
					entryDestination.getParentFile().mkdirs();
					InputStream in = zipFile.getInputStream(entry);
					OutputStream out = new FileOutputStream(entryDestination);
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
					out.close();
				}
			}
		} catch (ZipException e) {
			throw new RuntimeException("Error while trying to read ZIP file.", e);
		} catch (IOException e) {
			throw new RuntimeException("Error while trying to read ZIP file.", e);
		} finally {
			try {
				zipFile.close();
			} catch (IOException e) {
				throw new RuntimeException("Error while closing ZIP file.", e);
			}
		}
	}

	private void moveDateIntoCorrectDirectory(final File documentationDataDirectory, final File unzippedDir) {
		File[] dirs = unzippedDir.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		if (dirs.length > 1) {
			throw new IllegalArgumentException(
					"The ZIP file must only contain one directory, which has to be the branch directory.");
		}

		File sourceBranchDir = dirs[0];
		File targetBranchDir = new File(documentationDataDirectory, sourceBranchDir.getName());

		if (targetBranchDir.exists()) {
			moveBuildToExistingBranchDir(sourceBranchDir, targetBranchDir);
		} else {
			moveEntireBranchDirectory(sourceBranchDir, targetBranchDir);
		}

		// TODO Cleanup files and folders if something goes wrong.

		try {
			FileUtils.deleteDirectory(unzippedDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void moveBuildToExistingBranchDir(final File sourceBranchDir, final File targetBranchDir) {
		LOGGER.info("Moving build to existing branch folder.");
		try {
			File[] filesToMove = sourceBranchDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					// we don't want to copy branch.xml, it should already exist.
					return !"branch.xml".equals(name);
				}
			});

			if (filesToMove.length != 1 || !filesToMove[0].isDirectory()) {
				throw new IllegalArgumentException("The branch directory must only contain one build directory.");
			}

			FileUtils.moveToDirectory(filesToMove[0], targetBranchDir, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void moveEntireBranchDirectory(final File sourceBranchDir, final File targetBranchDir) {
		LOGGER.info("Moving entire branch folder because it does not exist yet.");
		try {
			FileUtils.moveDirectory(sourceBranchDir, targetBranchDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO: Better exception and error handling

}
