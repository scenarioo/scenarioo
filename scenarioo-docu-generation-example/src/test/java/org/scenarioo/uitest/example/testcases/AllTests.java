package org.scenarioo.uitest.example.testcases;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.scenarioo.uitest.example.builds.SaveBranchAndBuildDescriptionExampleTest;
import org.scenarioo.uitest.example.infrastructure.MultipleBuildsRule;

@RunWith(Suite.class)
@SuiteClasses({FindPageUITest.class, SwitchLanguageUITest.class, TechnicalCornerCasesUITest.class, SaveBranchAndBuildDescriptionExampleTest.class})
public class AllTests {
	
	@ClassRule
	public static MultipleBuildsRule multipleBuildsRule = new MultipleBuildsRule();

}
