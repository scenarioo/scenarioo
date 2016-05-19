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
var BRANCH = process.env.BRANCH || 'local_dev';

console.log('PROTRACTOR_BASE_URL: ' + PROTRACTOR_BASE_URL);
console.log('BRANCH: ' + BRANCH);

var prepareProtractor = require('./prepareProtractor');

var exportsConfig = {
    framework: 'jasmine',

    // Do not use selenium server but instead connect directly to chrome
    directConnect: true,

    // Timeouts: https://angular.github.io/protractor/#/timeouts
    allScriptsTimeout: 20000,
    getPageTimeout: 20000,

    specs: [/* See gulpfile.js for specified tests */],

    baseUrl: PROTRACTOR_BASE_URL,

    // CSS Selector for the element housing the angular app - this defaults to
    // body, but is necessary if ng-app is on a descendant of <body>.
    rootElement: 'html',

    onPrepare: function () {
        // enable scenarioo userDocumentation (see more on http://www.scenarioo.org)
        // pass in the current branch of your VCS you are testing, an arbitrary build name and the current revision you are testing.
        var moment = require('moment');
        var timeStamp = moment().format('YYYY.MM.DD_HH.mm.ss');
        var git = require('git-rev-sync');

        var scenarioo = require('scenarioo-js');

        // Setup and configure the ScenariooJS jasmine reporter
        scenarioo.setupJasmineReporter(jasmine, {

            targetDirectory: './scenariooDocumentation',
            branchName: 'scenarioo-self-docu',
            branchDescription: 'Scenarioo documenting itself.',
            buildName: 'build_' + timeStamp,
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
        defaultTimeoutInterval: 40000
    }
};

exports.config = exportsConfig;
