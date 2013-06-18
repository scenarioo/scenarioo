'use strict';

var NgUsdClientApp = angular.module('ngUSDClientApp', ['ui.bootstrap.dropdownToggle', 'ngResource']);

NgUsdClientApp.config(function ($routeProvider) {
        $routeProvider
            .when('/build/:branchId', {
                redirectTo: '/build/:branchId/current'
            })
            .when('/build/:branchId/:buildId', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                branchId: '@branchId',
                buildId: '@buildId'
            })
            .when('/usecase/:useCaseId', {
                templateUrl: 'views/usecase.html',
                controller: 'UseCaseCtrl',
                useCaseId: '@useCaseId'
            })
            .otherwise({
                redirectTo: '/build/current/current'
            });
    });
