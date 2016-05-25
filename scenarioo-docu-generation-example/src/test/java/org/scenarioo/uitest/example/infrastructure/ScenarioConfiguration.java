package org.scenarioo.uitest.example.infrastructure;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;

public class ScenarioConfiguration {

	private static final Map<BuildRun, Map<String, String>> BUILD_TO_SCENARIO_MAP = new HashMap<BuildRun, Map<String, String>>();

	static {
		Map<String, String> aprilTestMethodToName = new HashMap<String, String>();
		aprilTestMethodToName.put("find_donate_page", "find_donate_page_test");

		Map<String, String> mayTestMethodToName = new HashMap<String, String>();
		mayTestMethodToName.put("dummy_scenario_with_no_steps", "dummy_scenario_with_no_steps_test");
		mayTestMethodToName.put("dummy_scenario_with_no_page_names_set", "dummy_scenario_with_no_page_names_set_test");

		BUILD_TO_SCENARIO_MAP.put(BuildRun.APRIL, aprilTestMethodToName);
		BUILD_TO_SCENARIO_MAP.put(BuildRun.MAY, mayTestMethodToName);
	}

	public static String getScenarioName(Description testMethodDescription, final BuildRun build) {
		String configuredScenarioName = getConfiguredScenarioName(testMethodDescription.getMethodName(), build);
		if (StringUtils.isNotEmpty(configuredScenarioName)) {
			return configuredScenarioName;
		}

		DocuDescription description = testMethodDescription.getAnnotation(DocuDescription.class);
		if (description != null && !StringUtils.isBlank(description.name())) {
			return description.name();
		}
		
		// simply use the test name as scenario name if not set through
		// description annotation.
		return testMethodDescription.getMethodName();
	}

	private static String getConfiguredScenarioName(String methodName, BuildRun build) {
		Map<String, String> testMethodToName = BUILD_TO_SCENARIO_MAP.get(build);
		if (testMethodToName == null) {
			return null;
		}
		return testMethodToName.get(methodName);
	}
}
