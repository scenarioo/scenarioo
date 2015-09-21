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

angular.module('scenarioo.controllers').controller('SketchStepCtrl', function ($http, $scope, $routeParams, $location, $q, $window, localStorageService, Config, ScenarioSketchResource, SketchStepResource, HostnameAndPort, SelectedBranchAndBuild, $filter, ScApplicationInfoPopup, GlobalHotkeysService, LabelConfigurationsResource, SharePageService, ContextService, Issue, $rootScope) {

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    $scope.pageName = $routeParams.pageName;
    $scope.pageOccurrence = parseInt($routeParams.pageOccurrence, 10);
    $scope.stepInPageOccurrence = parseInt($routeParams.stepInPageOccurrence, 10);
    var issueId = $routeParams.issueId;
    var scenarioSketchId = $routeParams.scenarioSketchId;
    var sketchStepName = $routeParams.sketchStepName;
    $scope.isStepScope = true;

    Issue.load($routeParams.issueId);
    $rootScope.$on(Issue.ISSUE_LOADED_EVENT, function (event, result) {
        $scope.currentIssue = result.issue;
        $scope.issueName = $scope.currentIssue.name;
        $scope.issueDescription = $scope.currentIssue.description;
        $scope.issueAuthor = $scope.currentIssue.author;
    });

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.showApplicationInfoPopup = function (tab) {
        ScApplicationInfoPopup.showApplicationInfoPopup(tab);
    };

    SelectedBranchAndBuild.callOnSelectionChange(loadSketchStep);

    function loadSketchStep(selected) {
        loadSketchStepFromServer(selected);
    }

    function loadSketchStepFromServer(selected) {
        SketchStepResource.get(
            {
                'branchName': selected.branch,
                'issueId': issueId,
                'scenarioSketchId': scenarioSketchId,
                'sketchStepName': sketchStepName
            },
            function success(result) {
                $scope.sketchStep = result;
                $http.get($scope.getScreenShotUrl(), {headers: {
                    'Accept': 'image/svg+xml'
                }}).success(function(data) {
                    $scope.sketchStepSVG = data;
                });

                ContextService.sketchButton.innerHTML = ContextService.sketchButton.innerHTML.replace('Create', 'Edit');

                SharePageService.setPageUrl($scope.getCurrentUrlForSharing());

                $scope.hasContext = function () {
                    return $scope.sketchStep.usecaseContextName.length > 0 || $scope.sketchStep.usecaseContextLink.length > 0 || $scope.sketchStep.scenarioContextName.length > 0 || $scope.sketchStep.scenarioContextLink.length > 0;
                };
            },
            function error(result) {
                $scope.stepNotFound = true;
                $scope.httpResponse = {
                    status: result.status,
                    method: result.config.method,
                    url: result.config.url,
                    data: result.data
                };
            }
        );
    }

    // This URL is only used internally, not for sharing
    $scope.getScreenShotUrl = function () {
        if (angular.isUndefined($scope.sketchStep)) {
            return undefined;
        }

        var imageName = $scope.sketchStep.sketchFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();
        return HostnameAndPort.forLink() + 'rest/branch/' + selected.branch + '/issue/' + issueId + '/scenariosketch/' + scenarioSketchId + '/sketchstep/' + sketchStepName + '/image/' + imageName;
    };

    $scope.go = function (step) {
        $location.path('/step/' + (step.useCaseName || useCaseName) + '/' + (step.scenarioName || scenarioName) + '/' + step.pageName + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence);
    };

    $scope.getCurrentUrlForSharing = function () {
        return $location.absUrl();
    };

    $scope.getCurrentUrl = function () {
        return $location.absUrl();
    };

    $scope.getScreenshotUrlForSharing = function () {
        if (angular.isUndefined($scope.sketchStep)) {
            return undefined;
        }

        var imageName = $scope.sketchStep.sketchFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();

        return HostnameAndPort.forLinkAbsolute() + 'rest/branch/' + selected.branch + '/issue/' + issueId + '/scenariosketch/' + scenarioSketchId + '/sketchstep/' + sketchStepName + '/image/' + imageName;
    };

    $scope.$on('$destroy', function () {
        SharePageService.invalidateUrls();
    });

    $scope.sketchThis = function () {
        ContextService.initialize();
        ContextService.issueId = issueId;
        ContextService.scenarioSketchId = scenarioSketchId;
        ContextService.sketchStepName = sketchStepName;
        ContextService.screenshotURL = $scope.getScreenShotUrl();
        $location.path('/editor/').search('mode', 'edit');
    };

    $scope.goToUsecase = function(){
        $location.path('/usecase/' + $scope.sketchStep.usecaseContextLink);
    };

    $scope.goToScenario = function(){
        $location.path('/scenario/' + $scope.sketchStep.usecaseContextLink + '/' + $scope.sketchStep.scenarioContextLink);
    };

    $scope.goToStep = function(){
        $location.path($scope.sketchStep.stepContextLink);
    };

});

angular.module('scenarioo.directives').directive('ngHtml', ['$compile', function($compile) {
    return function(scope, elem, attrs) {
        if(attrs.ngHtml){
            elem.html(scope.$eval(attrs.ngHtml));
            $compile(elem.contents())(scope);
        }
        scope.$watch(attrs.ngHtml, function(newValue, oldValue) {
            if (newValue && newValue !== oldValue) {
                elem.html(newValue);
                $compile(elem.contents())(scope);
            }
        });
    };
}]);

