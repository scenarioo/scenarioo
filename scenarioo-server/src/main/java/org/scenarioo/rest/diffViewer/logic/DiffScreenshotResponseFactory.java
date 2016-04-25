package org.scenarioo.rest.diffViewer.logic;

import java.io.File;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

public class DiffScreenshotResponseFactory {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final DiffReader diffReader = new DiffReader(
			configurationRepository.getDiffViewerDirectory());

	public Response createFoundImageResponse(final String baseBranchName, final String baseBuildName,
			final String comparisonName,
			final String useCaseName,
			final String scenarioName, final String imageName,
			final boolean showFallbackStamp) {
		File screenshot = diffReader.getScreenshotFile(baseBranchName, baseBuildName, comparisonName, useCaseName,
				scenarioName, imageName);

		return createFoundImageResponse(imageName, screenshot, showFallbackStamp);
	}

	private Response createFoundImageResponse(final String imgName, final File screenshot,
			final boolean showFallbackStamp) {
		if (screenshot == null || !screenshot.exists()) {
			return notFoundResponse();
		} else {
			return createOkResponse(imgName, screenshot);
		}
	}

	private Response createOkResponse(final String imgName, final File screenshot) {
		return Response.ok(screenshot).build();
	}

	private Response notFoundResponse() {
		return Response.status(Status.NOT_FOUND).build();
	}

}
