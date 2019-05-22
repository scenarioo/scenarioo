package org.scenarioo.validator;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class ScenariooValidatorTest {

	@Rule
	public TemporaryFolder testDirectory = new TemporaryFolder();

    /**
     * Our validator must successfully validate our own example docu.
     */
    @Test
    public void validation_successful_with_own_example_docu() throws InterruptedException, IOException {

    	// Copy example data to import in temporary folder.
		// This is needed to not modify files that are commited,
		// because build.xml files are modified by the importer
		File docuDirectory = new File("../scenarioo-docu-generation-example/src/test/resources/example/documentation/scenarioDocuExample");
		FileUtils.copyDirectory(docuDirectory, testDirectory.getRoot());

		// Let the validator import and validate the data
		boolean result = new ScenariooValidator(testDirectory.getRoot(), true).validate();
        Assert.assertTrue(result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_failed_for_invalid_path() throws InterruptedException {
        File inexistentDirectory = new File("./this/path/does/not/exist");
        new ScenariooValidator(inexistentDirectory, true).validate();
    }

}
