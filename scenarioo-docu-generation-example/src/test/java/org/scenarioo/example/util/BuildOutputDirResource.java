package org.scenarioo.example.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Just to share the same output dir for generated docu data and ensure it gets cleared before running any test.
 */
public class BuildOutputDirResource {

	public static final File DOCU_BUILD_DIRECTORY = new File("build/scenarioDocuExample");

	static {
		// Ensure that on start of test all old test results are removed.
		deleteDirectory(DOCU_BUILD_DIRECTORY);

		// Ensure that the build directory gets precreated, this is not handled by the Scenarioo Docu writer, if
		// directory does not exist docu generation will fail.
		DOCU_BUILD_DIRECTORY.mkdirs();
	}

	private static void deleteDirectory(final File docuBranchDir) {
		try {
			FileUtils.deleteDirectory(docuBranchDir);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

}
