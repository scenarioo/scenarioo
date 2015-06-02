'use strict';

var gulp = require('gulp'),
    connect = require('gulp-connect'),
    less = require('gulp-less');

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