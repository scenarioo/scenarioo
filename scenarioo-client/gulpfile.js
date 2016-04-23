/* eslint-env node */

var gulp = require('gulp'),
    del = require('del'),
    fs = require('fs'),
    eslint = require('gulp-eslint'),
    usemin = require('gulp-usemin'),
    _ = require('lodash'),
    gulpUtil = require('gulp-util'),
    wrap = require('gulp-wrap'),
    connect = require('gulp-connect'),
    less = require('gulp-less'),
    karma = require('karma').server,
    path = require('path'),
    ngAnnotate = require('gulp-ng-annotate'),
    uglify = require('gulp-uglify'),
    protractor = require('gulp-protractor').protractor;
    webdriver_update = require('gulp-protractor').webdriver_update; // eslint-disable-line camelcase, no-undef

var files = {
    templates: ['./app/template/**/*.html'],
    views: ['./app/views/**/*.html'],
    images: ['./app/images/**/*'],
    css: ['./app/styles/**/*.css'],
    sources: ['./app/scripts/**/*.js'],
    tests: ['./test/**/*.js'],
    less: ['./app/styles/*.less']
};


/**
 * Serve the 'app' folder as a web application. The page is automatically reloaded whenever a file changes.
 * Use 'gulp serve' to view the app during development.
 */
gulp.task('serve', ['environmentConstants', 'watch', 'less'], function () {
    connect.server({
        root: 'app',
        livereload: true,
        port: 9000
    });
});

/**
 * Build the application and serve the 'dist' folder in a web server. Files are not watched, so you have
 * to build again and reload the page yourself after a change.
 * Use this to test your built Scenarioo client.
 */
gulp.task('serve-dist', ['build'], function () {
    connect.server({
        root: 'dist',
        livereload: true,
        port: 9000
    });
});

/**
 * Lint our sources with ESLint (http://eslint.org/).
 */
gulp.task('lint', function () {
    return gulp.src(_.flatten([files.sources, files.tests, 'gulpfile.js']))
        .pipe(eslint())
        .pipe(eslint.format())
        .pipe(eslint.failAfterError());
});

/**
 * Watches our source files and reloads them in the developers browser, when changes are made.
 * Also compiles the less files to css whenever changes are made.
 */
gulp.task('watch', function () {
    gulp.watch(_.flatten([files.css, files.templates, files.views, files.sources]), ['reload-files']);
    gulp.watch(files.less, ['less', 'reload-files']);
});

/**
 * Reloads the page in the 'gulp serve' browser. Called when source files are changed.
 */
gulp.task('reload-files', function () {
    gulp.src(_.flatten([files.css, files.templates, files.views, files.sources, files.less]))
        .pipe(connect.reload());
});

/**
 * Compiles our less files into css.
 */
gulp.task('less', function () {
    return gulp.src(files.less)
        .pipe(less().on('error', function (e) {
            console.info('Error in your less-file', e.fileName, e.lineNumber);
        }))
        .pipe(gulp.dest('./app/styles/'));
});

/**
 * Run unit tests once
 */
gulp.task('test', ['environmentConstants'], function (done) {
    karma.start({
        configFile: path.join(__dirname, 'karma.conf.js'),
        singleRun: true
    }, done);
});

/**
 * Run unit tests after each file change
 */
gulp.task('test-watch', function (done) {
    karma.start({
        configFile: path.join(__dirname, 'karma.conf.js'),
        singleRun: false,
        autoWatch: true
    }, done);
});

/**
 * Run protractor ui tests (without generating Scenarioo documentation)
 */
gulp.task('test-e2e', function () {
    gulp.src(['./test/protractorE2E/specs/**/*.js'])
        .pipe(protractor({
            configFile: './protractor-e2e.conf.js'
        }))
        .on('error', function (e) {
            throw e;
        });
});

gulp.task('webdriver_update', webdriver_update); // eslint-disable-line no-undef

/**
 * Run protractor ui tests and generate Scenarioo documentation from them.
 */
gulp.task('test-e2e-scenarioo', function () {
    gulp.src(['./test/protractorE2E/specs/**/*.js'])
        .pipe(protractor({
            configFile: './protractor-e2e-scenarioo.conf.js'
        }))
        .on('error', function (e) {
            throw e;
        });
});

/**
 * Read constants from environments.json and write angular config file "environment_config.js"
 * specify environment like so:
 *
 * gulp serve --production
 * gulp build --production
 *
 * Default is "development" and does not need to be specified.
 */
gulp.task('environmentConstants', function (done) {
    var environments = require('./environments.json');
    var selectedEnvironment = gulpUtil.env.production ? 'production' : 'development';

    var angularConfigFileContent = '// This file is written by a gulp task. Configuration is done in environments.json\n\n';
    angularConfigFileContent += 'angular.module(\'scenarioo.config\', [])\n';
    _.forEach(environments[selectedEnvironment], function (value, key) {
        angularConfigFileContent += '.constant(\'' + key + '\', \'' + value + '\')\n';
    });
    angularConfigFileContent += '.constant(\'ENV\', \'' + selectedEnvironment + '\');\n';

    fs.writeFile('./app/scripts/environment_config.js', angularConfigFileContent);
    done();
});

/**
 * Delete the 'dist' folder.
 */
gulp.task('clean-dist', function (done) {
    del('./dist');
    done();
});

/**
 *  Wraps given files (pipe) with IIFE and adds 'use strict';
 */
function wrapWithIIFE() {
    return wrap('(function(){\n\'use strict\';\n <%= contents %> }());\n');
}

/**
 * Concatenate and uglify sources.
 */
gulp.task('usemin', ['clean-dist'], function () {
    return gulp.src('./app/index.html')
        .pipe(usemin({
            sources: [wrapWithIIFE(), 'concat', ngAnnotate(), uglify({
                mangle: false
            })],
            vendor: [uglify({
                mangle: false
            }), 'concat'],
            vendorcss: []
        }))
        .pipe(gulp.dest('./dist/'));
});

/**
 * Copies all required files from the 'app' folder to the 'dist' folder.
 */
gulp.task('copy-to-dist', ['environmentConstants', 'clean-dist', 'usemin', 'less'], function () {
    /* copy own images, styles, and templates */
    gulp.src(files.images).pipe(gulp.dest('./dist/images'));
    gulp.src(files.css).pipe(gulp.dest('./dist/styles'));
    gulp.src(files.templates).pipe(gulp.dest('./dist/template'));
    gulp.src(files.views).pipe(gulp.dest('./dist/views'));
    gulp.src('./app/favicon.ico').pipe(gulp.dest('./dist/'));

    /* copy third party files */
    gulp.src(['./app/components/font-awesome/font/*']).pipe(gulp.dest('./dist/font'));
    gulp.src(['./app/components/bootstrap/dist/fonts/*']).pipe(gulp.dest('./dist/fonts'));
});

/**
 * Build the client.
 */
gulp.task('build', ['lint', 'test', 'clean-dist', 'copy-to-dist']);

/**
 * If you call gulp without a target, we assume you want to build the client.
 */
gulp.task('default', ['build']);
