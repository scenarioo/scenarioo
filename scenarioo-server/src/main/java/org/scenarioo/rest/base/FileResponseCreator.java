package org.scenarioo.rest.base;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileResponseCreator {

	public static ResponseEntity<InputStreamResource> createLogFileResponse(File logFile) {
		return ResponseEntity
			.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + logFile + "\"")
			.body(getFileInputStream(logFile));
	}

	public static ResponseEntity<InputStreamResource> createImageFileResponse(File imageFile) {
		return ResponseEntity
			.ok()
			.cacheControl(CacheControl.noStore())
			.body(getFileInputStream(imageFile));
	}

	private static InputStreamResource getFileInputStream(File file) {
		try {
			return new InputStreamResource(Files.newInputStream(file.toPath()));
		} catch (IOException e) {
			throw new RuntimeException("Unable to open file: ", e);
		}
	}
}
