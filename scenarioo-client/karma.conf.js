/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

module.exports = function (config) {

    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '',

        // list of files / patterns to load in the browser
        files: [
            'app/components/angular/angular.js',
            'app/components/angular-resource/angular-resource.js',
            'app/components/angular-route/angular-route.js',
            'app/components/angular-mocks/angular-*.js',
            'app/components/angular-local-storage/dist/angular-local-storage.min.js',
            'app/components/twigs/dist/twigs.js',
            'app/components/svg.js/dist/svg.js',
            'app/components/svg.draggable.js/dist/svg.draggable.js',
            'app/components/svg-pan-zoom/dist/svg-pan-zoom.js',
            'app/components/angular-unsavedChanges/dist/unsavedChanges.js',
            'app/scripts/*.js',
            'app/scripts/**/*.js',
            'test/mock/**/*.js',
            'test/spec/**/*.js'
        ],

        frameworks: ['jasmine'],

        // list of files to exclude
        exclude: [],

        // test results reporter to use
        // possible values: dots || progress || growl
        reporters: ['progress', 'junit'],
        //reporters = ['progress', 'junit', 'coverage'];

        //preprocessors = {
        //    'app/scripts/services/*.js' : 'coverage'
        //}

        // web server port
        port: 7070,

        // cli runner port
        runnerPort: 9100,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - Firefox
        // - PhantomJS
        browsers: ['PhantomJS'],

        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 20000,

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false
    });

};

