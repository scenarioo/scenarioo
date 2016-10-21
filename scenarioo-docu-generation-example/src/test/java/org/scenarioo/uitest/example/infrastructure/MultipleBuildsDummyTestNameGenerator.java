package org.scenarioo.uitest.example.infrastructure;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;

/**
 * Only used for testing. You would never use this in a production environment.
 *
 * This is only for getting some useful demo data with some use cases and scenarios having different name depending on the build run.
 */
public class MultipleBuildsDummyTestNameGenerator {

	private static final Map<BuildRun, Map<String, String>> BUILD_TO_SCENARIO_MAP = new HashMap<BuildRun, Map<String, String>>();

	private static final Map<BuildRun, Map<String, String>> BUILD_TO_USECASE_MAP = new HashMap<BuildRun, Map<String, String>>();

	static {

		/**
		 * Configure different use case names for some builds
		 */

		Map<String, String> aprilTestClassToName = new HashMap<String, String>();
		aprilTestClassToName.put("Find Page", "FindPageUserInterfaceTest");

		Map<String, String> mayTestClassToName = new HashMap<String, String>();
		mayTestClassToName.put("Find Page", "FindPageUserInterfaceTest");
		mayTestClassToName.put("Switch Language", "SwitchLanguageUserInterfaceTest");

		BUILD_TO_USECASE_MAP.put(BuildRun.APRIL, aprilTestClassToName);
		BUILD_TO_USECASE_MAP.put(BuildRun.MAY, mayTestClassToName);


		/**
		 * Configure different scenario names for some builds
		 */

		Map<String, String> aprilTestMethodToName = new HashMap<String, String>();
		aprilTestMethodToName.put("find_donate_page", "find_donate_page_test");

		Map<String, String> mayTestMethodToName = new HashMap<String, String>();
		mayTestMethodToName.put("dummy_scenario_with_no_steps", "dummy_scenario_with_no_steps_test");
		mayTestMethodToName.put("dummy_scenario_with_no_page_names_set", "dummy_scenario_with_no_page_names_set_test");

		BUILD_TO_SCENARIO_MAP.put(BuildRun.APRIL, aprilTestMethodToName);
		BUILD_TO_SCENARIO_MAP.put(BuildRun.MAY, mayTestMethodToName);

	}

	public static String getDifferentUseCaseNameForBuild(String name) {
		String configuredUseCaseName = getConfiguredUseCaseName(name, MultipleBuildsRule.getCurrentBuildRun());
		if (StringUtils.isNotEmpty(configuredUseCaseName)) {
			return configuredUseCaseName;
		}
		else {
			return name;
		}
	}

	private static String getConfiguredUseCaseName(String className, BuildRun build) {
		Map<String, String> testClassToName = BUILD_TO_USECASE_MAP.get(build);
		if (testClassToName == null) {
			return null;
		}
		return testClassToName.get(className);
	}


	public static String getDifferentScenarioNameForBuild(String name) {
		String configuredScenarioName = getConfiguredScenarioName(name, MultipleBuildsRule.getCurrentBuildRun());
		if (StringUtils.isNotEmpty(configuredScenarioName)) {
			return configuredScenarioName;
		}
		else {
			return name;
		}
	}

	private static String getConfiguredScenarioName(String methodName, BuildRun build) {
		Map<String, String> testMethodToName = BUILD_TO_SCENARIO_MAP.get(build);
		if (testMethodToName == null) {
			return null;
		}
		return testMethodToName.get(methodName);
	}
}
