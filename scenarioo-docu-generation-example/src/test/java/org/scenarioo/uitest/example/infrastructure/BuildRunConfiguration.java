package org.scenarioo.uitest.example.infrastructure;


public class BuildRunConfiguration {

	/**
	 * Here we configure which scenarios are failing. As only the February build has status "failed" all other builds do not have failing tests.
	 */
	public static boolean isScenarioFailing(String className, String methodName) {
		if(!MultipleBuildsRule.getCurrentBuildRun().equals(BuildRun.FEBRUARY)) {
			return false;
		}
		String classAndMethodName = className + "." + methodName;
		return "TechnicalCornerCasesUITest.dummy_scenario_with_no_steps".equals(classAndMethodName) || "FindPageUITest.find_page_no_result".equals(classAndMethodName);
	}
	
}
