package org.scenarioo.tools.validator;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ScenariooValidatorTest {

    /**
     * Our validator must successfully validate our own example docu.
     */
    @Test
    public void validation_successful_with_own_example_docu() throws InterruptedException {
        File docuDirectory = new File("../scenarioo-docu-generation-example/src/test/resources/example/documentation/scenarioDocuExample");
        boolean result = new ScenariooValidator(docuDirectory, true).validate();
        Assert.assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validation_failed_for_invalid_path() throws InterruptedException {
        File docuDirectory = new File("./this/path/does/not/exist");
        new ScenariooValidator(docuDirectory, true).validate();
    }

}
