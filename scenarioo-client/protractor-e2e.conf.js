'use strict';

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

    baseUrl: 'http://localhost:9000',

    rootElement: 'body',

    onPrepare: function () {
    },

    params: {
        baseUrl: 'http://localhost:9000'
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
        defaultTimeoutInterval: 600000
    }
};

exports.config = exportsConfig;
