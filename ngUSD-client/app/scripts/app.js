'use strict';

var NgUsdClientApp = angular.module('ngUSDClientApp', ['ui.bootstrap.modal', 'ui.bootstrap.popover', 'ui.bootstrap.dropdownToggle', 'ui.bootstrap.tabs', 'ui.bootstrap.tooltip', 'ui.bootstrap.accordion', 'ngResource', 'ng', 'ngCookies']);

NgUsdClientApp.config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                breadcrumb: "<i class='icon-home'></i> Home"
            })
            .when('/config', {
                templateUrl: 'views/config.html',
                controller: 'ConfigCtrl',
                breadcrumb: "<i class='icon-cogs'></i> Configuration"
            })
            .when('/usecase/:useCaseName', {
                templateUrl: 'views/usecase.html',
                controller: 'UseCaseCtrl',
                useCaseName: '@useCaseName',
                breadcrumb: '<strong>Use Case:</strong> $param'
            })
            .when('/scenario/:useCaseName/:scenarioName', {
                templateUrl: 'views/scenario.html',
                controller: 'ScenarioCtrl',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName',
                breadcrumb: '<strong>Scenario:</strong> $param'
            })
            .when('/step/:useCaseName/:scenarioName/:pageName/:pageOccurenceInScenario/:stepIndex', {
                templateUrl: 'views/step.html',
                controller: 'StepCtrl',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName',
                pageName: '@pageName',
                pageOccurenceInScenario: '@pageOccurenceInScenario',
                stepIndex: '@stepIndex',
                breadcrumb: '<strong>Step:</strong> {{pageIndex+1}}.{{stepIndex}} - {{pageName}}'
            })
            .otherwise({
                redirectTo: '/'
            });
    });