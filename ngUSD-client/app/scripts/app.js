'use strict';

var NgUsdClientApp = angular.module('ngUSDClientApp', ['ui.bootstrap.dropdownToggle', 'ngResource', 'ng']);

NgUsdClientApp.config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/usecase/:usecaseName', {
                templateUrl: 'views/usecase.html',
                controller: 'UseCaseCtrl',
                usecaseName: '@usecaseName'
            })
            .when('/scenario/:usecaseName/:scenarioName', {
                templateUrl: 'views/scenario.html',
                controller: 'ScenarioCtrl',
                usecaseName: '@usecaseName',
                scenarioName: '@scenarioName'

            })
            .when('/step/:usecaseName/:scenarioName/:pageName/:pageOccurenceInScenario/:stepIndex', {
                templateUrl: 'views/step.html',
                controller: 'StepCtrl',
                usecaseName: '@usecaseName',
                scenarioName: '@scenarioName',
                pageName: '@pageName',
                pageOccurenceInScenario: '@pageOccurenceInScenario',
                stepIndex: '@stepIndex'


            })
            .otherwise({
                redirectTo: '/'
            });
    });
