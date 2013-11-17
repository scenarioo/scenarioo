package org.scenarioo.uitest.example.config;

import java.io.File;

/**
 * Just some constants for the example UI test documentation generation.
 */
public class ExampleUITestDocuGenerationConfig {
	
	public static final File DOCU_BUILD_DIRECTORY = new File("build/scenarioDocuExample");
	
	/**
	 * An example name for the branch in the example. Usually you would use the name of your real development branch
	 * (e.g. "trunk" or "bugfix-branch-2013-11")
	 */
	public static final String EXAMPLE_BRANCH_NAME = "example-branch";
	
	/**
	 * An example name for the build in the example. Usually you would use something like current datw, time and
	 * revision of current build. It is important that this name is unique.
	 */
	public static final String EXAMPLE_BUILD_NAME = "example-build";
	
	static {
		// Ensure that the build directory gets precreated, this is not handled by the Scenarioo Docu writer, if
		// directory does not exist docu generation will fail.
		DOCU_BUILD_DIRECTORY.mkdirs();
	}
	
}
