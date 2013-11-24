'use strict';

angular.module('scenarioo.filters', []);
angular.module('scenarioo.directives', ['scenarioo.filters']);
angular.module('scenarioo.services', [ 'ngCookies', 'ngResource', 'scenarioo.config']);
angular.module('scenarioo.controllers', ['scenarioo.services', 'scenarioo.directives']);

angular.module('scenarioo', [
        'scenarioo.controllers',
        'ui.bootstrap'
    ]).config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                breadcrumb: '<i class="icon-home"></i> Home'
            })
            .when('/config', {
                templateUrl: 'views/config.html',
                controller: 'ConfigEditorCtrl',
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
    }).run(function ($rootScope, Config) {
        Config.load();
    });