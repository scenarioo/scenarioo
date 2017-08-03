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


require('./components/bootstrap/dist/css/bootstrap.min.css');
require('./components/font-awesome/css/font-awesome.min.css');
// TODO danielsuter use less
require('./styles/scenarioo.css');

/** vendor */
require('jquery');
require('angular');
require('angular-resource');
require('angular-sanitize');

require('angular-route');


require('./components/angular-bootstrap/ui-bootstrap-tpls.js');
require('./components/angular-local-storage/dist/angular-local-storage.min.js');
require('./components/twigs/dist/twigs.js');
require('./components/angular-unsavedChanges/dist/unsavedChanges.js');

require('./environment_config.js');


angular.module('scenarioo.filters', []);
angular.module('scenarioo.screenAnnotations', ['scenarioo.filters', 'ngRoute']);
angular.module('scenarioo.directives', ['scenarioo.filters', 'ngRoute', 'twigs.globalHotkeys', 'unsavedChanges']);
angular.module('scenarioo.services', ['ngResource', 'ngRoute', 'scenarioo.config', 'LocalStorageModule']);
angular.module('scenarioo.controllers', ['scenarioo.services', 'scenarioo.directives']);

angular.module('scenarioo', ['scenarioo.controllers', 'ui.bootstrap', 'scenarioo.screenAnnotations'])

    .config(function ($routeProvider) {

        /**
         * breadcrumbId: id of the breadcrumb elements to use for this page as defined in breadcrumbs.service.js
         */
        $routeProvider
            .when('/', {
                template: require('./build/build.html'),
                controller: 'BuildController',
                controllerAs: 'main',
                breadcrumbId: 'main'
            })
            .when('/manage', {
                template: require('./manage/manage.html'),
                controller: 'ManageController',
                controllerAs: 'vm',
                breadcrumbId: 'manage'
            })
            .when('/usecase/:useCaseName', {
                template: require('./useCase/usecase.html'),
                controller: 'UseCaseController',
                controllerAs: 'useCase',
                useCaseName: '@useCaseName',
                breadcrumbId: 'usecase'
            })
            .when('/scenario/:useCaseName/:scenarioName', {
                template: require('./scenario/scenario.html'),
                controller: 'ScenarioController',
                controllerAs: 'vm',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName',
                breadcrumbId: 'scenario'
            })
            .when('/search/:searchTerm', {
                template: require('./search/search.html'),
                controller: 'SearchController',
                controllerAs: 'vm',
                breadcrumbId: 'search',
                searchTerm: '@searchTerm'
            })
            .when('/object/:objectType/:objectName', {
                template: require('./objectRepository/objectRepository.html'),
                controller: 'ObjectRepositoryController',
                controllerAs: 'vm',
                objectType: '@objectType',
                objectName: '@objectName',
                breadcrumbId: 'object'
            })
            .when('/step/:useCaseName/:scenarioName/:pageName/:pageOccurrence/:stepInPageOccurrence', {
                template: require('./step/step.html'),
                controller: 'StepController',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName',
                pageName: '@pageName',
                pageOccurrence: '@pageOccurrence',
                stepInPageOccurrence: '@stepInPageOccurrence',
                breadcrumbId: 'step'
            })
            .when('/stepsketch/:issueId/:scenarioSketchId/:stepSketchId', {
                template: require('./sketcher/stepSketch.html'),
                controller: 'StepSketchController',
                controllerAs: 'vm',
                breadcrumbId: 'stepsketch'
            })
            .when('/editor', {
                template: require('./sketcher/sketcherEditor.html'),
                controller: 'SketcherEditorController',
                controllerAs: 'vm'
            })
            .when('/editor/:issueId/:scenarioSketchId/:stepSketchId', {
                template: require('./sketcher/sketcherEditor.html'),
                controller: 'SketcherEditorController',
                controllerAs: 'vm',
                issueId: '@issueId',
                scenarioSketchId: '@scenarioSketchId',
                stepSketchId: '@stepSketchId'
            })
            .otherwise({
                redirectTo: '/'
            });

    }).run(function ($rootScope, ConfigService, GlobalHotkeysService, $location, $uibModalStack, ApplicationInfoPopupService, $templateCache) {

    // These templates are loaded dynamically, thus we preload it and put it into our template cache.
    $templateCache.put('shared/navigation/navigation.html', require('./shared/navigation/navigation.html'));
    $templateCache.put('build/useCasesTab.html', require('./build/useCasesTab.html'));
    $templateCache.put('build/useCasesTab.html', require('./build/useCasesTab.html'));
    $templateCache.put('build/customTab.html', require('./build/customTab.html'));
    $templateCache.put('manage/buildImport/buildsList.html', require('./manage/buildImport/buildsList.html'));
    $templateCache.put('manage/generalSettings/generalSettings.html', require('./manage/generalSettings/generalSettings.html'));
    $templateCache.put('manage/branchAliases/branchAliases.html', require('./manage/branchAliases/branchAliases.html'));
    $templateCache.put('manage/labelColors/labelColors.html', require('./manage/labelColors/labelColors.html'));


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

    require('./build/build.controller.js');
    require('./build/useCasesTab.controller.js');
    require('./build/sketchesTab.controller.js');
    require('./build/customTab.controller.js');
    require('./manage/config.service.js');
    require('./manage/manage.controller.js');
    require('./manage/buildImport/buildsList.controller.js');
    require('./manage/buildImport/buildImportDetails.controller.js');
    require('./manage/generalSettings/generalSettings.controller.js');
    require('./manage/branchAliases/branchAliases.controller.js');
    require('./manage/labelColors/labelColors.controller.js');
    require('./objectRepository/objectRepository.controller.js');
    require('./scenario/scenario.controller.js');
    require('./scenario/pagesAndSteps.service.js');
    require('./search/search.controller.js');
    require('./shared/navigation/branchesAndBuilds.service.js');
    require('./shared/restServices.js');
    require('./shared/utils/urlContextExtractor.service.js');
    require('./shared/localStorage/localStorage.service.js');
    require('./shared/navigation/selectedBranchAndBuild.service.js');
    require('./shared/navigation/applicationInfoPopup.service.js');
    require('./shared/filterableTableTree/treeNode.service.js');
    require('./shared/filterableTableTree/referenceTreeNavigation.service.js');
    require('./shared/navigation/sharePage/sharePage.service.js');
    require('./shared/navigation/breadcrumbs/breadcrumbs.service.js');
    require('./shared/navigation/breadcrumbs/breadcrumbs.directive.js');
    require('./shared/navigation/breadcrumbs/sketcherLink.service.js');
    require('./shared/sortableColumn.directive.js');
    require('./shared/applicationStatus.service.js');
    require('./shared/metadata/tree.directive.js');
    require('./shared/filterableTableTree/filterableTableTree.directive.js');
    require('./shared/keyboardNavigatableTable.directive.js');
    require('./shared/debounce.directive.js');
    require('./shared/metadata/metaDataButton.directive.js');
    require('./shared/metadata/metaDataPanel.directive.js');
    require('./shared/metadata/metaDataTree.directive.js');
    require('./shared/metadata/collapsablePanel.directive.js');
    require('./shared/navigation/navigation.controller.js');
    require('./shared/navigation/title.controller.js');
    require('./shared/utils/humanReadable.filter.js');
    require('./shared/utils/filterArray.filter.js');
    require('./shared/navigation/branchOrderBy.filter.js');
    require('./shared/metadata/treeDataCreator.filter.js');
    require('./shared/metadata/treeDataOptimizer.filter.js');
    require('./shared/metadata/metadataTreeCreator.filter.js');
    require('./shared/metadata/metadataTreeListCreator.filter.js');
    require('./shared/utils/dateTime.filter.js');
    require('./shared/utils/dateOnly.filter.js');
    require('./shared/navigation/applicationInfoPopup.service.js');
    require('./shared/navigation/sharePage/sharePagePopup.controller.js');
    require('./shared/navigation/sharePage/sharePagePopup.service.js');
/*
    require('./sketcher/sketcherContext.service.js');
    require('./sketcher/sketcherRestClient.service.js');
    require('./sketcher/stepSketch.controller.js');
    require('./sketcher/shapes/svg.element.extended.js');
    require('./sketcher/shapes/svg.compositeShape.js');
    require('./sketcher/shapes/svg.drawBorder.js');
    require('./sketcher/shapes/drawShape.service.js');
    require('./sketcher/shapes/svg.lineShape.js');
    require('./sketcher/drawingPad/drawingPad.service.js');
    require('./sketcher/drawingPad/zoomPan.service.js');
    require('./sketcher/tools/tool.service.js');
    require('./sketcher/tools/drawTool.service.js');
    require('./sketcher/tools/compositeDrawTool.service.js');
    require('./sketcher/tools/selectTool.service.js');
    require('./sketcher/tools/rectCompositeDrawTool.service.js');
    require('./sketcher/tools/borderCompositeDrawTool.service.js');
    require('./sketcher/tools/noteCompositeDrawTool.service.js');
    require('./sketcher/tools/textCompositeDrawTool.service.js');
    require('./sketcher/tools/buttonCompositeDrawTool.service.js');
    require('./sketcher/tools/highlightCompositeDrawTool.service.js');
    require('./sketcher/tools/lineDrawTool.service.js');
    require('./sketcher/drawingPad/toolBox.service.js');
    require('./sketcher/sketcherEditor.controller.js');
    require('./sketcher/storeSketch.service.js');
*/
    require('./step/screenAnnotations/annotatedScreenshot.directive.js');
    require('./step/screenAnnotations/screenAnnotationsButton.directive.js');
    require('./step/screenAnnotations/screenAnnotationInfoPopup.controller.js');
    require('./step/screenAnnotations/screenAnnotations.service.js');
    require('./step/step.controller.js');
    require('./useCase/usecase.controller.js');

    require('./diffViewer/diffInfo.service.js');
    require('./diffViewer/diffInfoIcon/diffInfoIcon.directive.js');
    require('./diffViewer/screenshotTitle/screenshotTitle.directive.js');
    require('./diffViewer/screenshotUrl.service.js');
    require('./diffViewer/diffViewerRestClient.service.js');
    require('./diffViewer/selectedComparison.service.js');
