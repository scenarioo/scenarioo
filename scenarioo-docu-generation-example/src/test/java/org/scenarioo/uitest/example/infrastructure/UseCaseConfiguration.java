package org.scenarioo.uitest.example.infrastructure;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.scenarioo.uitest.example.testcases.FindPageUITest;
import org.scenarioo.uitest.example.testcases.SwitchLanguageUITest;

public class UseCaseConfiguration {

	private static final Map<BuildRun, Map<String, String>> BUILD_TO_USECASE_MAP = new HashMap<BuildRun, Map<String, String>>();

	static {
		Map<String, String> aprilTestClassToName = new HashMap<String, String>();
		aprilTestClassToName.put(FindPageUITest.class.getSimpleName(), "FindPageUserInterfaceTest");

		Map<String, String> mayTestClassToName = new HashMap<String, String>();
		mayTestClassToName.put(FindPageUITest.class.getSimpleName(), "FindPageUserInterfaceTest");
		mayTestClassToName.put(SwitchLanguageUITest.class.getSimpleName(), "SwitchLanguageUserInterfaceTest");

		BUILD_TO_USECASE_MAP.put(BuildRun.APRIL, aprilTestClassToName);
		BUILD_TO_USECASE_MAP.put(BuildRun.MAY, mayTestClassToName);
	}

	public static String getUseCaseName(final Class<?> testClass, final BuildRun build) {
		String configuredUseCaseName = getConfiguredUseCaseName(testClass.getSimpleName(), build);
		if (StringUtils.isNotEmpty(configuredUseCaseName)) {
			return configuredUseCaseName;
		}

		DocuDescription description = testClass.getAnnotation(DocuDescription.class);
		if (description != null && !StringUtils.isBlank(description.name())) {
			return description.name();
		}

		// simply use the test class name as use case name if not set through description annotation.
		return testClass.getSimpleName();
	}

	private static String getConfiguredUseCaseName(String className, BuildRun build) {
		Map<String, String> testClassToName = BUILD_TO_USECASE_MAP.get(build);
		if (testClassToName == null) {
			return null;
		}
		return testClassToName.get(className);
	}
}
