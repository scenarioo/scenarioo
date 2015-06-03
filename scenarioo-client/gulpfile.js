'use strict';

var gulp = require('gulp'),
    del = require('del'),
    fs = require('fs'),
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

var files = {
    templates: ['./app/template/**/*.html'],
    views: ['./app/views/**/*.html'],
    images: ['./app/images/**/*'],
    css: ['./app/styles/**/*.css'],
    sources: ['./app/scripts/**/*.js'],
    less: ['./app/styles/*.less']
};

/**
 *
 */
gulp.task('serve', ['environmentConstants', 'watch'], function () {
    connect.server({
        root: 'app',
        livereload: true,
        port: 9000
    });
});

/**
 * will serve the dist folder on port 9000.
 * Use this to test your built jetpack client.
 */
gulp.task('serveDist', ['build'], function () {
    connect.server({
        root: 'dist',
        livereload: true,
        port: 9000
    });
});

gulp.task('watch', function () {
    gulp.watch(files.css, files.templates, files.views, files.sources, ['file-reload']);
    gulp.watch(files.less, ['less', 'file-reload']);
});

gulp.task('file-reload', function () {
    gulp.src(files.css, files.templates, files.views, files.sources, files.less)
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

    var angularConfigFileContent = '';

    angularConfigFileContent += '// this file is written by a gulp task. configuration is done in environments.json\n';
    angularConfigFileContent += 'angular.module(\'scenarioo.config\', [])\n';


    _.forEach(environments[selectedEnvironment], function (value, key) {
        angularConfigFileContent += '.constant(\'' + key + '\', \'' + value + '\')\n';
    });
    angularConfigFileContent += '.constant(\'ENV\', \'' + selectedEnvironment + '\');\n';


    fs.writeFile('./app/scripts/environment_config.js', angularConfigFileContent, done);

});


gulp.task('cleanDist', function (done) {
    del('./dist', done);
});


/**
 *  Wraps given files (pipe) with IIFE and adds 'use strict';
 */
function wrapWithIIFE() {
    return wrap('(function(){\n\'use strict\';\n <%= contents %> }());\n');
}

gulp.task('usemin', ['cleanDist'], function () {
    return gulp.src('./app/index.html')
        .pipe(usemin({
            sources: [wrapWithIIFE(), 'concat', ngAnnotate(), uglify()],
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
gulp.task('copyAssets', ['environmentConstants', 'cleanDist', 'usemin', 'less'], function () {
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

gulp.task('build', ['test', 'cleanDist', 'copyAssets']);

gulp.task('default', ['build']);