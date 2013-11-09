'use strict';

angular.module('ngUSDClientApp.filter', []);
angular.module('ngUSDClientApp.directives', ['ngUSDClientApp.filter']);
angular.module('ngUSDClientApp.services', [ 'ngCookies', 'ngResource', 'ngUSDClientApp.config']);
angular.module('ngUSDClientApp.controllers', ['ngUSDClientApp.services']);

var NgUsdClientApp = angular.module('ngUSDClientApp', [
    'ngUSDClientApp.controllers',
    'ngUSDClientApp.directives',
    'ngUSDClientApp.filter',
    'ui.bootstrap.modal',
    'ui.bootstrap.popover',
    'ui.bootstrap.dropdownToggle',
    'ui.bootstrap.tabs',
    'ui.bootstrap.tooltip',
    'ui.bootstrap.accordion'
]);

NgUsdClientApp.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'views/main.html',
            controller: 'MainCtrl',
            breadcrumb: '<i class="icon-home"></i> Home'
        })
        .when('/config', {
            templateUrl: 'views/config.html',
            controller: 'ConfigCtrl',
            breadcrumb: '<i class="icon-cogs"></i> Configuration'
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
        .when('/step/:useCaseName/:scenarioName/:pageName/:pageIndex/:stepIndex', {
            templateUrl: 'views/step.html',
            controller: 'StepCtrl',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
            pageName: '@pageName',
            pageIndex: '@pageIndex',
            stepIndex: '@stepIndex',
            breadcrumb: '<strong>Step:</strong> {{pageIndex+1}}.{{stepIndex}} - {{pageName}}'
        })
        .otherwise({
            redirectTo: '/'
        });
}).run(function($rootScope, Config) {
    Config.load();

});