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
angular.module('scenarioo.directives', ['scenarioo.filters', 'ngRoute', 'twigs.globalHotkeys', 'ui.bootstrap.tpls']);
angular.module('scenarioo.services', [ 'ngResource', 'ngRoute', 'scenarioo.config', 'LocalStorageModule']);
angular.module('scenarioo.controllers', ['scenarioo.services', 'scenarioo.directives']);

angular.module('scenarioo', ['scenarioo.controllers','ui.bootstrap'])

.config(function ($routeProvider) {

    $routeProvider
        .when('/', {
            templateUrl: 'views/main.html',
            controller: 'MainCtrl',
            breadcrumbId: 'main'
        })
        .when('/manage', {
            templateUrl: 'views/manage/manage.html',
            controller: 'ManageCtrl',
            breadcrumbId: 'manage'
        })
        .when('/usecase/:useCaseName', {
            templateUrl: 'views/usecase.html',
            controller: 'UseCaseCtrl',
            useCaseName: '@useCaseName',
            breadcrumbId: 'usecase'
        })
        .when('/scenario/:useCaseName/:scenarioName', {
            templateUrl: 'views/scenario.html',
            controller: 'ScenarioCtrl',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
            breadcrumbId: 'scenario'
        })
        .when('/object/:objectType/:objectName', {
            templateUrl: 'views/referenceTree.html',
            controller: 'ReferenceTreeCtrl',
            objectType: '@objectType',
            objectName: '@objectName',
            breadcrumbId: 'object'
        })
        .when('/step/:useCaseName/:scenarioName/:pageName/:pageIndex/:stepIndex', {
            templateUrl: 'views/step.html',
            controller: 'StepCtrl',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
            pageName: '@pageName',
            pageIndex: '@pageIndex',
            stepIndex: '@stepIndex',
            breadcrumbId: 'step'
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



