'use strict';

var gulp = require('gulp'),
    connect = require('gulp-connect'),
    less = require('gulp-less'),
    karma = require('karma').server,
    path = require('path'),
    protractor = require('gulp-protractor').protractor;

var files = {
    served: ['./app/index.html', './app/template/**/*.html', './app/views/**/*.html', './app/scripts.js', './app/*.css'],
    less: ['./app/styles/*.less']
};

gulp.task('serve', ['watch'], function () {
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