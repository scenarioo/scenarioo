package org.scenarioo.validator;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScenariooValidatorTest {

    /**
     * Our validator must successfully validate our own example docu.
     */
    @Test
    void validation_successful_with_own_example_docu(@TempDir File testDirectory) throws InterruptedException, IOException {

    	// Copy example data to import in temporary folder.
		// This is needed to not modify files that are committed,
		// because build.xml files are modified by the importer
		File docuDirectory = new File("../scenarioo-docu-generation-example/src/test/resources/example/documentation/scenarioDocuExample");
		FileUtils.copyDirectory(docuDirectory, testDirectory);

		// Let the validator import and validate the data
		boolean result = new ScenariooValidator(testDirectory, true).validate();
        assertTrue(result);

    }

    @Test
    void validation_failed_for_invalid_path() {
        File inexistentDirectory = new File("./this/path/does/not/exist");
        assertThrows(IllegalArgumentException.class, () -> new ScenariooValidator(inexistentDirectory, true).validate());
    }

}
