'use strict';

var NgUsdClientApp = angular.module('ngUSDClientApp', ['ui.bootstrap.dropdownToggle', 'ui.bootstrap.tabs', 'ui.bootstrap.tooltip', 'ui.bootstrap.accordion', 'ngResource', 'ng', 'ngCookies']);

NgUsdClientApp.config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                breadcrumb: "<i class='icon-home'></i>"
            })
            .when('/usecase/:useCaseName', {
                templateUrl: 'views/usecase.html',
                controller: 'UseCaseCtrl',
                useCaseName: '@useCaseName'
            })
            .when('/scenario/:useCaseName/:scenarioName', {
                templateUrl: 'views/scenario.html',
                controller: 'ScenarioCtrl',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName'

            })
            .when('/step/:useCaseName/:scenarioName/:pageName/:pageOccurenceInScenario/:stepIndex', {
                templateUrl: 'views/step.html',
                controller: 'StepCtrl',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName',
                pageName: '@pageName',
                pageOccurenceInScenario: '@pageOccurenceInScenario',
                stepIndex: '@stepIndex'

            })
            .otherwise({
                redirectTo: '/'
            });
    });