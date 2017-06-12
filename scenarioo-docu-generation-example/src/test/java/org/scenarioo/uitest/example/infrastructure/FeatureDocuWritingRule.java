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

package org.scenarioo.uitest.example.infrastructure;

import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.model.docu.entities.Feature;

/**
 * A {@link TestRule} to setup as a static {@link ClassRule} on your UI test classes to generate documentation content
 * for each running test class as a Usecase inside the Scenarioo Documentation.
 */
public class FeatureDocuWritingRule implements TestRule {

	private static final Logger LOGGER = Logger.getLogger(FeatureDocuWritingRule.class);

	@Override
	public Statement apply(final Statement test, final Description testClassDescription) {

		// Statement to write feature description as soon as test class gets executed
		return new Statement() {

			private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY,
					MultipleBuildsRule.getCurrentBranchName(), MultipleBuildsRule.getCurrentBuildName());

			@Override
			public void evaluate() throws Throwable {
				try {
					// Save feature description
					Feature feature = createFeature(testClassDescription.getTestClass());
					LOGGER.info("Generating Scenarioo Docu for Feature " + feature.getId() + ": "
							+ feature.getDescription());
					docuWriter.saveFeature(feature);
					// Run tests
					test.evaluate();
				} finally {
					// Wait until asynch writing has finished.
					docuWriter.flush();
				}
			}
		};
	}

	public static Feature createFeature(final Class<?> testClass) {
		// Extract feature name and description from concrete test class.
		String description = "";
		String name = getNameFromClass(testClass);

		DocuDescription docuDescription = testClass.getAnnotation(DocuDescription.class);
		if (docuDescription != null) {
			description = docuDescription.description();
		}
		// Create feature
		Feature feature = new Feature();

		feature.setId(FilesUtil.encodeName(name));

		feature.setNameAndId(name);
		feature.setDescription(description);
		feature.addDetail("Webtest Class", testClass.getName());
		addLabelsIfPresentOnTestClass(testClass, feature);
		return feature;
	}

	private static String getNameFromClass(Class<?> testClass) {

		String name = null;
		DocuDescription description = testClass.getAnnotation(DocuDescription.class);
		if (description != null && !StringUtils.isBlank(description.name())) {
			name =  description.name();
		}
		else {
			// simply use the test class name as feature name if not set through description annotation.
			name = testClass.getSimpleName();
		}

		// just for dummy data for demo:
		// change some names slightly depending on the build run
		// for having useful demo data for diff viewer
		return MultipleBuildsDummyTestNameGenerator.getDifferentFeatureNameForBuild(name);

	}

	private static void addLabelsIfPresentOnTestClass(final Class<?> testClass,
													  Feature feature) {
		Labels labels = testClass.getAnnotation(Labels.class);
		if (labels != null) {
			feature.getLabels().setLabels(new HashSet<String>(Arrays.asList(labels.value())));
		}
	}

	public static String createFeatureName(final Class<?> testClass) {
		DocuDescription description = testClass.getAnnotation(DocuDescription.class);
		if (description != null && !StringUtils.isBlank(description.name())) {
			return description.name();
		} else {
			// simply use the test class name as feature name if not set through description annotation.
			return testClass.getSimpleName();
		}
	}

}
