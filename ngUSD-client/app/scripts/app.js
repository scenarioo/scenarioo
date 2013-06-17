'use strict';

angular.module('ngUSDClientApp', [])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/usecase', {
                templateUrl: 'views/usecase.html',
                controller: 'UseCaseCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
