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

var PROTRACTOR_BASE_URL = process.env.PROTRACTOR_BASE_URL || 'http://localhost:8500/scenarioo';
var BRANCH = process.env.BRANCH || 'HEAD';
var BUILD_NAME = 'build-' + (process.env.BUILD_NUMBER || 'latest');

console.log('PROTRACTOR_BASE_URL: ' + PROTRACTOR_BASE_URL);
console.log('BRANCH: ' + BRANCH);
console.log('BUILD_NAME: ' + BUILD_NAME);

var prepareProtractor = require('./prepareProtractor');

var exportsConfig = {
    framework: 'jasmine',

    // Do not use selenium server but instead connect directly to chrome
    directConnect: true,

    // Timeouts: https://angular.github.io/protractor/#/timeouts
    allScriptsTimeout: 20000,
    getPageTimeout: 20000,

    capabilities: {
        browserName: 'chrome',
        chromeOptions: {
            args: [
                'disable-infobars',
                'window-size=1280,800',
            ]
        },
    },

    specs: ['./test/protractorE2E/specs/**/*.ts'],

    baseUrl: PROTRACTOR_BASE_URL,

    // CSS Selector for the element housing the angular app - this defaults to
    // body, but is necessary if ng-app is on a descendant of <body>.
    rootElement: 'html',

    onPrepare: function () {

        // Setup and configure the ScenariooJS jasmine reporter
        // See https://github.com/scenarioo/scenarioo-js/blob/develop/README.md
        var git = require('git-rev-sync');
        var scenarioo = require('scenarioo-js');
        scenarioo.setupJasmineReporter(jasmine, {

            targetDirectory: './scenariooDocumentation',
            branchName: 'scenarioo-' + BRANCH,
            branchDescription: 'Scenarioo documenting itself.',
            buildName: BUILD_NAME,
            revision: git.short(),
            pageNameExtractor: function (url) {
                return url.pathname.substring(1);
            },
            reportStepOnExpectationFailed: true,
            recordLastStepForStatus: {
                failed: true,
                success: true
            }

        });

        prepareProtractor();

    },

    params: {
        // Used in our tests
        baseUrl: PROTRACTOR_BASE_URL
    },

    jasmineNodeOpts: {
        // If true, display spec names.
        isVerbose: false,
        // If true, print colors to the terminal.
        showColors: true,
        // If true, include stack traces in failures.
        includeStackTrace: true,
        // Default time to wait in ms before a test fails.
        defaultTimeoutInterval: 80000
    },

    SELENIUM_PROMISE_MANAGER: 0
};

exports.config = exportsConfig;
