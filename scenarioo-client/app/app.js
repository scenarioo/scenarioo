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


angular.module('scenarioo.filters', []);
angular.module('scenarioo.screenAnnotations', ['scenarioo.filters', 'ngRoute', 'scenarioo.templates']);
angular.module('scenarioo.directives', ['scenarioo.filters', 'ngRoute', 'twigs.globalHotkeys', 'scenarioo.templates']);
angular.module('scenarioo.services', ['ngResource', 'ngRoute', 'scenarioo.config', 'LocalStorageModule', 'scenarioo.templates']);
angular.module('scenarioo.controllers', ['scenarioo.services', 'scenarioo.directives']);

angular.module('scenarioo', ['scenarioo.controllers', 'ui.bootstrap', 'scenarioo.screenAnnotations', 'ngMaterial'])

    .config(function ($routeProvider) {

        /**
         * breadcrumbId: id of the breadcrumb elements to use for this page as defined in breadcrumbs.service.js
         */
        $routeProvider
            .when('/', {
                redirectTo: '/feature'
            })
            .when('/manage', {
                templateUrl: 'manage/manage.html',
                controller: 'ManageController',
                controllerAs: 'vm',
                breadcrumbId: 'manage'
            })
            .when('/feature/:featureName', {
                templateUrl: 'feature/feature.html',
                controller: 'FeatureController',
                controllerAs: 'feature',
                featureName: '@featureName',
                breadcrumbId: 'feature'
            })
            .when('/scenario/:featureName/:scenarioName', {
                templateUrl: 'scenario/scenario.html',
                controller: 'ScenarioController',
                controllerAs: 'vm',
                featureName: '@featureName',
                scenarioName: '@scenarioName',
                breadcrumbId: 'scenario'
            })
            .when('/search/:searchTerm', {
                templateUrl: 'search/search.html',
                controller: 'SearchController',
                controllerAs: 'vm',
                breadcrumbId: 'search',
                searchTerm: '@searchTerm'
            })
            .when('/object/:objectType/:objectName', {
                templateUrl: 'objectRepository/objectRepository.html',
                controller: 'ObjectRepositoryController',
                controllerAs: 'vm',
                objectType: '@objectType',
                objectName: '@objectName',
                breadcrumbId: 'object'
            })
            .when('/step/:featureName/:scenarioName/:pageName/:pageOccurrence/:stepInPageOccurrence', {
                templateUrl: 'step/step.html',
                controller: 'StepController',
                featureName: '@featureName',
                scenarioName: '@scenarioName',
                pageName: '@pageName',
                pageOccurrence: '@pageOccurrence',
                stepInPageOccurrence: '@stepInPageOccurrence',
                breadcrumbId: 'step'
            })
            .when('/stepsketch/:issueId/:scenarioSketchId/:stepSketchId', {
                templateUrl: 'sketcher/stepSketch.html',
                controller: 'StepSketchController',
                controllerAs: 'vm',
                breadcrumbId: 'stepsketch'
            })
            .when('/editor', {
                templateUrl: 'sketcher/sketcherEditor.html',
                controller: 'SketcherEditorController',
                controllerAs: 'vm'
            })
            .when('/editor/:issueId/:scenarioSketchId/:stepSketchId', {
                templateUrl: 'sketcher/sketcherEditor.html',
                controller: 'SketcherEditorController',
                controllerAs: 'vm',
                issueId: '@issueId',
                scenarioSketchId: '@scenarioSketchId',
                stepSketchId: '@stepSketchId'
            })

            .when('/dashboard', {
                templateUrl: 'dashboard/dashboard.html',
                controller: 'DashboardController',
                controllerAs: 'dashboard',
                breadcrumbId: 'dashboard'
            })
            .when('/detailNav', {
                templateUrl: 'dashboard/documentationView.html',
                controller: 'DashboardController',
                controllerAs: 'dashboard',
                breadcrumbId: 'detail'
            })
            .when('/testScenarios', {
                templateUrl: 'dashboard/scenarioView.html',
                controller: 'DashboardController',
                controllerAs: 'dashboard',
                breadcrumbId: 'testScenarios'
            })
            .when('/feature', {
                templateUrl: 'dashboard/featureView.html',
                controller: 'DashboardController',
                controllerAs: 'dashboard',
                breadcrumbId: 'feature'
            })
            .when('/features', {
                templateUrl: 'build/featuresTab.html',
                controller: 'FeaturesTabController',
                controllerAs: 'featuresTab'
            })

            .otherwise({
                redirectTo: '/'
            });

    }).run(function ($rootScope, ConfigService, GlobalHotkeysService, $location, $uibModalStack, ApplicationInfoPopupService) {


    // Initialze modals to close when the location changes
    $rootScope.$on('$locationChangeSuccess', function () {
        $uibModalStack.dismissAll();
    });

    $rootScope.$on('$viewContentLoaded', function () {
        ApplicationInfoPopupService.showApplicationInfoPopupIfRequired();
    });

    // Register global hotkeys
    GlobalHotkeysService.registerGlobalHotkey('m', function () {
        $location.path('/manage').search('tab=builds');
    });
    GlobalHotkeysService.registerGlobalHotkey('c', function () {
        $location.path('/manage').search('tab=configuration');
    });
    GlobalHotkeysService.registerGlobalHotkey('h', function () {
        $location.path('/');
    });

    // Load config
    ConfigService.load();
});


