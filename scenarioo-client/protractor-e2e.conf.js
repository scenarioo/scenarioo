'use strict';

/**
 * This protractor configuration can be configured using environment variables.
 *
 * If no environment variables are specified, default values are used. These default variables are meant
 * for running the tests locally on a developer machine.
 *
 * Parameters:
 *
 * PROTRACTOR_BASE_URL
 *   host of the application, i.e.: http://myhost:9345/myScenarioo
 *   default value: 'http://localhost:9000'
 */

var PROTRACTOR_BASE_URL = process.env.PROTRACTOR_BASE_URL || 'http://localhost:9000';


var exportsConfig = {

    // The location of the selenium standalone server .jar file.
    seleniumServerJar: './node_modules/gulp-protractor/node_modules/protractor/selenium/selenium-server-standalone-2.45.0.jar',
    // find its own unused port.
    seleniumPort: null,
    chromeDriver: './node_modules/gulp-protractor/node_modules/protractor/selenium/chromedriver',
    seleniumArgs: [],

    allScriptsTimeout: 11000,

    specs: [ /* See gulpfile.js for specified tests */ ],

    capabilities: {
        'browserName': 'chrome'
    },

    baseUrl: PROTRACTOR_BASE_URL,

    rootElement: 'body',

    onPrepare: function () {
    },

    params: {
        baseUrl: PROTRACTOR_BASE_URL
    },

    jasmineNodeOpts: {
        // onComplete will be called just before the driver quits.
        onComplete: null,
        // If true, display spec names.
        isVerbose: false,
        // If true, print colors to the terminal.
        showColors: true,
        // If true, include stack traces in failures.
        includeStackTrace: true,
        // Default time to wait in ms before a test fails.
        defaultTimeoutInterval: 30000
    }
};

exports.config = exportsConfig;
