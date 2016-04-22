/**
 * Shared setup code for all protractor e2e tets configurations
 */
function prepareProtractorForE2ETests() {

    setupScenariooFluentDsl();
    configureBrowserWindowSize();

    /**
     * Setup Scenarioo Fluent DSL for our protractor tests
     */
    function setupScenariooFluentDsl() {
        // this is also needed if scenarioo documenztation is not enabled
        var scenarioo = require('scenarioo-js');
        scenarioo.setupFluentDsl();
        require('./test/protractorE2E/labelDefinitions');
    }

    /**
     * Ensure to run tests allways in same resolution
     */
    function configureBrowserWindowSize() {
        var width = 1280;
        var height = 800;
        browser.driver.manage().window().setSize(width, height);
    }

}

module.exports = prepareProtractorForE2ETests;
