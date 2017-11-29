var jasmineReporters = require('jasmine-reporters');

/**
 * Shared setup code for all protractor e2e tets configurations
 */
function prepareProtractorForE2ETests() {

    require("ts-node").register({
        project: "test/protractorE2E/tsconfig.json"
    });

    setupJasmineXmlReporters();
    setupScenariooFluentDsl();
    disableAnimations();

    /**
     * Jasmine XML Reporters are needed to see test failures in nice report on jenkins.
     */
    /* global jasmine:true */
    function setupJasmineXmlReporters() {
        jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
            consolidateAll: true,
            savePath: './test-reports',
            filePrefix: 'xmloutput'
        }));
    }

    /**
     * Setup Scenarioo Fluent DSL for our protractor tests
     */
    function setupScenariooFluentDsl() {
        // this is also needed if scenarioo documenztation is not enabled
        var scenarioo = require('scenarioo-js');
        scenarioo.setupFluentDsl();
        require('./test/protractorE2E/labelDefinitions');
    }

    function disableAnimations() {

        var disableNgAnimate = function () {
            angular
                .module('disableNgAnimate', [])
                .run(['$animate', function ($animate) {
                    $animate.enabled(false);
                }]);
        };

        var disableCssAnimate = function () {
            angular
                .module('disableCssAnimate', [])
                .run(function () {
                    var style = document.createElement('style');
                    style.type = 'text/css';
                    style.innerHTML = '* {' +
                        '-webkit-transition: none !important;' +
                        '-moz-transition: none !important' +
                        '-o-transition: none !important' +
                        '-ms-transition: none !important' +
                        'transition: none !important' +
                        '}';
                    document.getElementsByTagName('head')[0].appendChild(style);
                });
        };

        browser.addMockModule('disableNgAnimate', disableNgAnimate);
        browser.addMockModule('disableCssAnimate', disableCssAnimate);

    }

}

module.exports = prepareProtractorForE2ETests;
