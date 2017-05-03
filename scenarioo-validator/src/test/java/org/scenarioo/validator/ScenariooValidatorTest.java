package org.scenarioo.validator;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class ScenariooValidatorTest {

    /**
     * Our validator must successfully validate our own example docu.
     */
    @Test@Ignore//TODO FIX
    public void validation_successful_with_own_example_docu() throws InterruptedException {
        File docuDirectory = new File("../scenarioo-docu-generation-example/src/test/resources/example/documentation/scenarioDocuExample");
        boolean result = new ScenariooValidator(docuDirectory, true).validate();
        Assert.assertTrue(result);

        // Make sure to remove derived files afterwards. we do not want to pollute "scenarioo-docu-generation-example", which is under version control
        // however it will still modify build.xml files...
        DerivedFileCleaner.cleanDerivedFiles(docuDirectory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_failed_for_invalid_path() throws InterruptedException {
        File docuDirectory = new File("./this/path/does/not/exist");
        new ScenariooValidator(docuDirectory, true).validate();
    }

}
