/* Copyright (c) 2014, scenarioo.org Development Team
 * All rights reserved.
 *
 * See https://github.com/scenarioo?tab=members
 * for a complete list of contributors to this project.
 *
 * Redistribution and use of the Scenarioo Examples in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.scenarioo.uitest.example.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Just some constants for the example UI test documentation generation.
 */
public class ExampleUITestDocuGenerationConfig {
	
	public static final File DOCU_BUILD_DIRECTORY = new File("build/scenarioDocuExample");
	
	/**
	 * An example name for the branch in the example. Usually you would use the name of your real development branch
	 * (e.g. "trunk" or "bugfix-branch-2013-11")
	 */
	public static final String EXAMPLE_BRANCH_NAME = "wikipedia-docu-example";
	public static final String EXAMPLE_BRANCH_NAME_DEV = "wikipedia-docu-example-dev";
	
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
