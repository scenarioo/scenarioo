'use strict';

var gulp = require('gulp'),
    fs = require('fs'),
    _ = require('lodash'),
    gulpUtil = require('gulp-util'),
    connect = require('gulp-connect'),
    less = require('gulp-less'),
    karma = require('karma').server,
    path = require('path'),
    protractor = require('gulp-protractor').protractor;

var files = {
    served: ['./app/index.html', './app/template/**/*.html', './app/views/**/*.html', './app/scripts.js', './app/*.css'],
    less: ['./app/styles/*.less']
};

gulp.task('serve', ['environmentConstants', 'watch'], function () {
    connect.server({
        root: 'app',
        livereload: true,
        port: 9000
    });
});

gulp.task('watch', function () {
    gulp.watch(files.served, ['file-reload']);
    gulp.watch(files.less, ['less', 'file-reload']);
});

gulp.task('file-reload', function () {
    gulp.src(files.served, files.less)
        .pipe(connect.reload());
});

gulp.task('less', function () {
    return gulp.src(files.less)
        .pipe(less().on('error', function (e) {
            console.info('Error in your less-file', e.fileName, e.lineNumber);
        }))
        .pipe(gulp.dest('./app/styles/'));
});

gulp.task('test', function (done) {
    karma.start({
        configFile: path.join(__dirname, 'karma.conf.js'),
        singleRun: true
    }, done);
});

gulp.task('test-watch', function (done) {
    karma.start({
        configFile: path.join(__dirname, 'karma.conf.js'),
        singleRun: false
    }, done);
});

gulp.task('test-e2e', function () {
    gulp.src(['./test/protractorE2E/specs/**/*.js'])
        .pipe(protractor({
            configFile: './protractor-e2e.conf.js'
        }))
        .on('error', function (e) {
            throw e;
        });
});

/**
 * read constants from environments.json and write angular config file "environment_config.js"
 * specify environment like so:
 *
 * gulp serve --production
 * gulp build --production
 *
 * default is "development" and does not need to be specified
 */
gulp.task('environmentConstants', function (done) {

    var environments = require('./environments.json');
    var selectedEnvironment = gulpUtil.env.production ? 'production' : 'development';

    var angularConfigFileContent = '\'use strict\';\n';

    angularConfigFileContent += '// this file is written by a gulp task. configuration is done in environments.json\n';
    angularConfigFileContent += 'angular.module(\'scenarioo.config\', [])\n';


    _.forEach(environments[selectedEnvironment], function (value, key) {
        angularConfigFileContent += '.constant(\'' + key + '\', \'' + value + '\')\n';
    });
    angularConfigFileContent += '.constant(\'ENV\', \'' + selectedEnvironment + '\');\n';


    fs.writeFile('./app/scripts/environment_config.js', angularConfigFileContent, done);

});