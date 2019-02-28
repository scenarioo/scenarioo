angular.module('scenarioo.services')
    .config(function ($httpProvider) {
        $httpProvider.defaults.headers.common.Accept = 'application/json';
        $httpProvider.defaults.stripTrailingSlashes = false;
    });
