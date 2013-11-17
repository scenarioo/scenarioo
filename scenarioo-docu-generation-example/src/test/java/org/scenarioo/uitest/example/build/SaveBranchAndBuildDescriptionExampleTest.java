package org.scenarioo.uitest.example.build;

import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.api.ScenarioDocuWriter;

/**
 * A simple example test to show how to save example branch and build description files.
 * 
 * Usually you would create these files from inside your CI build scripts or somewhere inside your UI testing
 * framework/infrastructure that runs all your UI tests.
 * 
 * The branch description might even be created manually (as an xml file in your documentation directory), whenever you
 * create a new branch.
 */
public class SaveBranchAndBuildDescriptionExampleTest {
	
	private static ScenarioDocuWriter docuWriter;
	
	@BeforeClass
	public static void createDocuWriter() {
		DOCU_BUILD_DIRECTORY.mkdirs(); // make sure the root directory is precreated
		docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, EXAMPLE_BRANCH_NAME, EXAMPLE_BUILD_NAME);
	}
	
	@AfterClass
	public static void flushAllAsynchronouslyWrittenData() {
		docuWriter.flush();
	}
	
	@Test
	public void write_branch_description() {
		// TODO
		
	}
	
	@Test
	public void write_build_description() {
		// TODO
	}
	
}
