angular.module('scenarioo.services')
    .config(($httpProvider) => {
        $httpProvider.defaults.headers.common.Accept = 'application/json';
        $httpProvider.defaults.stripTrailingSlashes = false;
    });
