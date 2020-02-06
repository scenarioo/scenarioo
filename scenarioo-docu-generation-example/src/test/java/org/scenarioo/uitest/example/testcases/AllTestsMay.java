package org.scenarioo.uitest.example.testcases;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.scenarioo.uitest.example.builds.SaveBranchAndBuildDescriptionExampleTest;
import org.scenarioo.uitest.example.infrastructure.BuildRun;
import org.scenarioo.uitest.example.infrastructure.MultipleBuildsConfiguration;

@RunWith(Suite.class)
@SuiteClasses({FindPageUITest.class, SwitchLanguageUITest.class, TechnicalCornerCasesUITest.class, DonateTest.class, SaveBranchAndBuildDescriptionExampleTest.class})
public class AllTestsMay {

	@BeforeClass
	public static void initBuildRun() {
		MultipleBuildsConfiguration.initBuildRun(BuildRun.MAY);
	}
}
