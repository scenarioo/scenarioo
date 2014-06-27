/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

angular.module('scenarioo.filters', []);
angular.module('scenarioo.directives', ['scenarioo.filters', 'ngRoute', 'twigs.globalHotkeys']);
angular.module('scenarioo.services', [ 'ngResource', 'ngRoute', 'scenarioo.config', 'LocalStorageModule']);
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
        .when('/manage', {
            templateUrl: 'views/manage/manage.html',
            controller: 'ManageCtrl',
            breadcrumb: '<i class="icon-cogs"></i> Manage'
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
            breadcrumb: '<strong>Step:</strong> $title'
        })
        .otherwise({
            redirectTo: '/'
        });
}).run(function ($rootScope, Config, GlobalHotkeysService, $location) {

    GlobalHotkeysService.registerGlobalHotkey('m', function () {
        $location.path('/manage').search('tab=builds');
    });

    GlobalHotkeysService.registerGlobalHotkey('c', function () {
        $location.path('/manage').search('tab=configuration');
    });

    GlobalHotkeysService.registerGlobalHotkey('h', function () {
        $location.path('/');
    });

    Config.load();
});



