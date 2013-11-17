package org.scenarioo.uitest.example.config;

import java.io.File;

/**
 * Just some constants for the example UI test documentation generation.
 */
public class ExampleUITestDocuGenerationConfig {
	
	public static final File DOCU_BUILD_DIRECTORY = new File("build/scenarioDocuExample");
	
	public static final String EXAMPLE_BRANCH_NAME = "example-branch";
	
	public static final String EXAMPLE_BUILD_NAME = "example-build";
	
	static {
		// Ensure that the build directory gets precreated, this is not handled by the Scenarioo Docu writer, if
		// directory does not exist docu generation will fail.
		DOCU_BUILD_DIRECTORY.mkdirs();
	}
	
}
