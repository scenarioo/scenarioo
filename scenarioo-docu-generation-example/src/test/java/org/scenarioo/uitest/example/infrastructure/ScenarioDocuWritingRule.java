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
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.uitest.example.issues.IssuesTrackingAccessHelper;
import org.scenarioo.uitest.example.issues.UserStories;

/**
 * A {@link TestRule} to setup as a {@link Rule} on your UI test classes to generate documentation content for each
 * running test method as a {@link Scenario} inside the Scenarioo Documentation.
 */
public class ScenarioDocuWritingRule extends TestWatcher {

	private static final Logger LOGGER = Logger.getLogger(UseCaseDocuWritingRule.class);

	private UseCase useCase;
	private Scenario scenario;

	/**
	 * Get the usecase for current running test (as initialized by this rule)
	 */
	public UseCase getUseCase() {
		return useCase;
	}

	/**
	 * Get the scenario for current running test (as initialized by this rule)
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * Initialize current running usecase and scenario before the test gets executed.
	 */
	@Override
	protected void starting(final Description testMethodDescription) {
		LOGGER.info("Writing scenario " + testMethodDescription.getMethodName());
		useCase = UseCaseDocuWritingRule.createUseCase(testMethodDescription.getTestClass());
		scenario = createScenario(testMethodDescription);
	}

	private Scenario createScenario(final Description testMethodDescription) {
		Scenario scenario = new Scenario();
		String description = "";
		String name = MultipleBuildsDummyTestNameGenerator.getScenarioDumyName(testMethodDescription, MultipleBuildsRule.getCurrentBuildRun());
		if(name == null) {
			name = getNameFromMethod(testMethodDescription);
		}
		scenario.setName(name);

		// store description and user role from test method's annotation (if any)
		DocuDescription docuDescription = testMethodDescription.getAnnotation(DocuDescription.class);
		if (docuDescription != null) {
			description = docuDescription.description();
			scenario.addDetail("User Role", docuDescription.userRole());
		}
		scenario.setDescription(description);

		// store labels from test method's annotation (if any)
		Labels labels = testMethodDescription.getAnnotation(Labels.class);
		if (labels != null) {
			Set<String> labelsSet = new HashSet<String>();
			labelsSet.addAll(Arrays.asList(labels.value()));
			scenario.getLabels().setLabels(labelsSet);
		}

		// store requirements (features, epics, user stories) linked through UserStories annotation by story ids,
		// and loaded from Issue Tracking Management tool (here only a dummy simulation of such a tool is used, of
		// course)
		UserStories userStories = testMethodDescription.getAnnotation(UserStories.class);
		if (userStories != null && userStories.value().length > 0) {
			long[] storyIds = userStories.value();
			ObjectList<ObjectTreeNode<ObjectDescription>> featureEpicUserStoriesTrees = IssuesTrackingAccessHelper
					.loadFeatureTreesForWorkItemIds(storyIds);
			scenario.addDetail("Requirements", featureEpicUserStoriesTrees);
		}

		// store some dummy metadata to test some very long texts in metadata
		scenario.addDetail("Very Long Metadata Lines", createLongLine());

		return scenario;
	}

	private String getNameFromMethod(Description testMethodDescription) {
			DocuDescription description = testMethodDescription.getAnnotation(DocuDescription.class);
			if (description != null && !StringUtils.isBlank(description.name())) {
				return description.name();
			}

			// simply use the test name as scenario name if not set through
			// description annotation.
			return testMethodDescription.getMethodName();
	}

	private static Details createLongLine() {
		Details details = new Details();
		details.addDetail("Long value with spaces",
				"Whenever you find yourself on the side of the majority, it is time to pause and reflect. -- Mark Twain");
		details.addDetail("Long value without spaces",
				"A_man_who_carries_a_cat_by_the_tail_learns_something_he_can_learn_in_no_other_way._--_Mark_Twain");
		details.addDetail(
				"A_very_long_metadata_label_without_any_spaces_just_to_test_whether_this_is_displayed_nicely.",
				"Cheers!");
		return details;
	}

	/**
	 * In real life failing test would actually fail. Here this is not practical. That's why we simulate failing tests.
	 */
	@Override
	protected void succeeded(final Description description) {
		String methodName = description.getMethodName();
		String className = description.getTestClass().getSimpleName();

		Status status = Status.SUCCESS;
		if(BuildRunConfiguration.isScenarioFailing(className, methodName)) {
			status = Status.FAILED;
			LOGGER.info("Failing scenario " + className + "." + "methodName");
		}

		writeScenarioDescription(description, status);
	}

	private void writeScenarioDescription(final Description testMethodDescription, Status status) {

		ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, MultipleBuildsRule.getCurrentBranchName(),
				MultipleBuildsRule.getCurrentBuildName());

		// Write scenario
		LOGGER.info("Generating Scenarioo Docu for Scenario " + useCase.getName() + "." + scenario.getName() + " ("
				+ status.getKeyword() + ") : " + scenario.getDescription());
		scenario.setStatus(status);
		docuWriter.saveScenario(useCase, scenario);

		// Wait until asynch writing has finished.
		docuWriter.flush();
	}

}
