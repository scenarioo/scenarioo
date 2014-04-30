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

package org.scenarioo.uitest.example.builds;

import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Build;

/**
 * This test simply creates additional build directories for creating several builds in test data.
 */
public class CreateAdditionalBuildsTest {
	
	@BeforeClass
	public static void initTargetDir() {
		DOCU_BUILD_DIRECTORY.mkdirs(); // make sure the root directory is precreated
	}
	
	@Test
	public void write_multiple_additional_build_descriptions_for_test_data() {
		createOlderBuild(1, "success");
		createOlderBuild(2, "unknown");
		createOlderBuild(3, "failed");
		createOlderBuild(4, "success");
		createOlderBuild(5, "success");
	}
	
	private void createOlderBuild(final int daysBefore, final String status) {
		String buildName = EXAMPLE_BUILD_NAME + "-old-" + daysBefore;
		ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, EXAMPLE_BRANCH_NAME, buildName);
		Build build = new Build();
		build.setName(buildName);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -daysBefore);
		build.setDate(cal.getTime());
		build.setRevision("12" + (3456 - daysBefore));
		build.setStatus(status);
		docuWriter.saveBuildDescription(build);
		docuWriter.flush();
	}
}
