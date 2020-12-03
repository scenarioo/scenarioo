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


import {Url} from '../shared/utils/url';

angular.module('scenarioo.controllers').controller('SketcherEditorController', SketcherEditorController);

function SketcherEditorController($rootScope, $scope, $location, $filter, $interval, $routeParams,
                                  SelectedBranchAndBuildService, ToolBoxService, DrawShapeService, DrawingPadService,
                                  IssueResource, SketcherContextService, LocalStorageService, ZoomPanService, $timeout,
                                  StoreSketchService) {

    var vm = this;
    vm.savingSketch = StoreSketchService.isSavingSketchInProgress;
    vm.alerts = [];
    vm.toolBox = [];
    vm.zoomFactor = ZoomPanService.getZoomFactor();
    vm.issueId;
    vm.scenarioSketchId;
    vm.stepSketchId;
    vm.mode;
    vm.sendSelectedShapeToBack = sendSelectedShapeToBack;
    vm.sendSelectedShapeToFront = sendSelectedShapeToFront;
    vm.sendSelectedShapeBackward = sendSelectedShapeBackward;
    vm.sendSelectedShapeForward = sendSelectedShapeForward;
    vm.contextBreadcrumbs = contextBreadcrumbs;
    vm.getExitSketcherPath = getExitSketcherPath;
    vm.saveSketch = saveSketch;
    vm.closeAlert = closeAlert;
    vm.activateTool = activateTool;

    var AUTHOR_LOCAL_STORAGE_KEY = 'issue_author',
        MODE_CREATE = 'create',
        MODE_EDIT = 'edit';

    activate();



    function activate() {
        vm.mode = $location.search().mode;
        if (vm.mode === MODE_EDIT) {
            initEditMode();
        } else {
            setAuthorFromLocalStorageIfAvailable();
            initializeDrawingPad();
        }
    }

    function initEditMode() {
        vm.issueId = $routeParams.issueId;
        vm.scenarioSketchId = $routeParams.scenarioSketchId;
        vm.stepSketchId = $routeParams.stepSketchId;

        IssueResource.get(
            {
                'branchName': $routeParams.branch,
                'issueId': vm.issueId
            },
            function onSuccess(result) {
                vm.currentIssue = result.issue;
                vm.issueName = vm.currentIssue.name;
                vm.issueDescription = vm.currentIssue.description;
                vm.issueAuthor = vm.currentIssue.author;
                DrawingPadService.setSvgUrl(getSVGUrl());
                initializeDrawingPad();
            });
    }

    function getSVGUrl () {
        if (angular.isUndefined(vm.currentIssue)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuildService.selected();
        return Url.encodeComponents `rest/branch/${selected.branch}/issue/${vm.currentIssue.issueId}/scenariosketch/${vm.scenarioSketchId}/stepsketch/${vm.stepSketchId}/svg/1`;
    }

    function setAuthorFromLocalStorageIfAvailable() {
        if (vm.mode !== MODE_CREATE) {
            return;
        }

        var author = LocalStorageService.get(AUTHOR_LOCAL_STORAGE_KEY);

        if (!angular.isString(author) || author.length === 0) {
            return;
        }

        vm.issueAuthor = author;
    }

    function initializeDrawingPad() {
        // The drawingPad is initialized here because we had issues
        // when initializing it in DrawingPadService.
        var drawingPad = SVG('drawingPad').spof();
        DrawingPadService.setDrawingPad(drawingPad);

        vm.currentTool = null;
        vm.toolBox = ToolBoxService;
        vm.activateTool(vm.toolBox[0]);

        $('body').addClass('sc-sketcher-bg-color-light');
    }

    function activateTool(tool) {
        if (vm.currentTool) {
            vm.currentTool.deactivate();
        }
        vm.currentTool = tool;
        tool.activate();

        DrawingPadService.unSelectAllShapes();

        $('.tooltip').hide().delay(100);
    }

    function sendSelectedShapeToBack() {
        DrawingPadService.sendSelectedShapeToBack();
    }

    function sendSelectedShapeToFront() {
        DrawingPadService.sendSelectedShapeToFront();
    }

    function sendSelectedShapeBackward() {
        DrawingPadService.sendSelectedShapeBackward();
    }

    function sendSelectedShapeForward() {
        DrawingPadService.sendSelectedShapeForward();
    }

    function contextBreadcrumbs() {
        var relatedStep;

        if (vm.currentIssue) {
            relatedStep = vm.currentIssue.relatedStep;
        } else if(SketcherContextService.stepIdentifier) {
            relatedStep = SketcherContextService.stepIdentifier;
        } else {
            return '';
        }

        var humanReadableFilter = $filter('scHumanReadable');

        return 'Use Case: ' + humanReadableFilter(relatedStep.usecaseName) + ' / Scenario: ' + humanReadableFilter(relatedStep.scenarioName)
            + ' / Step: ' + relatedStep.pageName + '/' + relatedStep.pageOccurrence + '/' + relatedStep.stepInPageOccurrence;
    }

    function getExitSketcherPath() {
        if(vm.issueId && vm.scenarioSketchId && vm.stepSketchId) {
            return '#/stepsketch/' + vm.issueId + '/' + vm.scenarioSketchId + '/' + vm.stepSketchId;
        } else {
            return undefined;
        }
    }

    function saveSketch() {
        DrawingPadService.unSelectAllShapes();
        StoreSketchService.saveIssueAndScenarioSketchAndStepSketch({
            branchName: $routeParams.branch,
            issueName: vm.issueName,
            issueDescription: vm.issueDescription,
            issueAuthor: vm.issueAuthor,
            issueId: vm.issueId,
            scenarioSketchId: vm.scenarioSketchId,
            stepSketchId: vm.stepSketchId,
            mode: vm.mode
        }, addAlert, function(result) {
            vm.issueId = result.issueId;
            vm.scenarioSketchId = result.scenarioSketchId;
            vm.stepSketchId = result.stepSketchId;
            vm.mode = result.mode;
        });
    }

    function closeAlert(alertEntry) {
        var index = vm.alerts.indexOf(alertEntry);
        vm.alerts.splice(index, 1);
    }

    // Alerts to give feedback whether saving the sketch was successful or not
    function addAlert(type, id, message) {
        var alertEntry = {type: type, id: id, message: message};
        vm.alerts.push(alertEntry);
        // We have to set our own timeout using the $interval service because using $timeout provokes issues
        // with protractor. See:
        // - https://github.com/angular-ui/bootstrap/pull/3982
        // - https://github.com/angular/protractor/issues/169
        $interval(function () {
            vm.closeAlert(alertEntry);
        }, 5000, 1);
    }

    $rootScope.$on('savedSketchSuccessfully', function(result) {

    });


    $rootScope.$on('drawingEnded', function (scope, shape) {
        // $scope.$apply is used to make sure that the button disabled directive updates in the view
        $scope.$apply(vm.activateTool(vm.toolBox[0]));

        shape.selectToggle();
        DrawingPadService.setSelectedShape(shape);
    });

    // Mark form as dirty if the drawing is changed
    $rootScope.$on('edit_drawing_event', function() {
        if(vm.unsavedDrawingChanges) {
            vm.unsavedDrawingChanges.$setViewValue(vm.unsavedDrawingChanges.$viewValue);
        }
    });

    $rootScope.$on(DrawShapeService.SHAPE_SELECTED_EVENT, function (scope, shape) {
        DrawingPadService.unSelectAllShapes();
        shape.selectToggle();
        DrawingPadService.setSelectedShape(shape);
    });

    $rootScope.$on(DrawingPadService.DRAWINGPAD_CLICKED_EVENT, function () {
        DrawingPadService.unSelectAllShapes();
    });

    $rootScope.$on(ZoomPanService.ZOOM_FACTOR_CHANGED, function() {
        vm.zoomFactor = ZoomPanService.getZoomFactor();
        $timeout(function() {
            $scope.$apply();
        });
    });

    $scope.$on('$destroy', function () {
        DrawingPadService.destroy();
        vm.issueId = null;
        vm.scenarioSketchId = null;
        vm.stepSketchId = null;
        $('body').removeClass('sc-sketcher-bg-color-light');
    });

}
