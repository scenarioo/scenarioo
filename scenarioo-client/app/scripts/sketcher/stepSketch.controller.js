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

angular
    .module('scenarioo.controllers')
    .controller('StepSketchCtrl', function ($http, $scope, $routeParams, $location, $q, $window, scLocalStorage,
        Config, StepSketchResource, HostnameAndPort, SelectedBranchAndBuild, $filter, ScApplicationInfoPopup,
        GlobalHotkeysService, LabelConfigurationsResource, SharePageService, ContextService, IssueResource) {

    var issueId = $routeParams.issueId,
        scenarioSketchId = $routeParams.scenarioSketchId,
        stepSketchId = $routeParams.stepSketchId;

    $scope.showCreateOrEditSketchLink = true;
    $scope.loading = true;
    $scope.issueNotFound = false;

    SelectedBranchAndBuild.callOnSelectionChange(loadIssueAndSketch);

    function loadIssueAndSketch() {
        IssueResource.get(
            {
                'branchName': $routeParams.branch,
                'issueId': issueId
            },
            function onSuccess(result) {
                $scope.issue = result.issue;
                $scope.scenarioSketch = result.scenarioSketch;
                $scope.stepSketch = result.stepSketch;
                updateUrlsForSharing();
                $scope.loading = false;
            }, function onFailure() {
                $scope.issueNotFound = true;
                $scope.loading = false;
            });

        function updateUrlsForSharing() {
            SharePageService.setPageUrl($scope.getCurrentUrlForSharing());
            SharePageService.setImageUrl($scope.getScreenshotUrlForSharing());
        }
    }

    $scope.getScreenShotUrl = function () {
        if (angular.isUndefined($scope.stepSketch)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();
        return HostnameAndPort.forLink() + 'rest/branch/' + selected.branch + '/issue/' + issueId + '/scenariosketch/' + scenarioSketchId + '/stepsketch/' + stepSketchId + '/image/sketch.png';
    };

    $scope.getOriginalScreenshotUrl = function() {
        if (angular.isUndefined($scope.stepSketch)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();
        return HostnameAndPort.forLink() + 'rest/branch/' + selected.branch + '/issue/' + issueId + '/scenariosketch/' + scenarioSketchId + '/stepsketch/' + stepSketchId + '/image/original.png';
    };

    $scope.getCurrentUrlForSharing = function () {
        return $location.absUrl();
    };

    $scope.getCurrentUrl = function () {
        return $location.absUrl();
    };

    $scope.getScreenshotUrlForSharing = function () {
        if (angular.isUndefined($scope.stepSketch)) {
            return undefined;
        }

        var imageName = $scope.stepSketch.sketchFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();

        return HostnameAndPort.forLinkAbsolute() + 'rest/branch/' + selected.branch + '/issue/' + issueId + '/scenariosketch/' + scenarioSketchId + '/stepsketch/' + stepSketchId + '/image/sketch.png';
    };

    $scope.$on('$destroy', function () {
        SharePageService.invalidateUrls();
    });

    // Used in breadcrumbs.html
    $scope.showCreateOrEditSketchLink = true;

    // Called from breadcrumbs.html
    $scope.getSketchButtonTitle = function () {
        return 'Edit Sketch';
    };

    // Called from breadcrumbs.html
    $scope.createOrEditSketch = function () {
        $location.path('/editor/' + issueId + '/' + scenarioSketchId + '/' + stepSketchId).search('mode', 'edit');
    };

    $scope.getUseCaseUrl = function() {
        if($scope.stepSketch == null) {
            return undefined;
        }
        return '#/usecase/' + encodeURIComponent($scope.stepSketch.relatedStep.usecaseName);
    };

    $scope.getScenarioUrl = function() {
        if($scope.stepSketch == null) {
            return undefined;
        }
        var step = $scope.stepSketch.relatedStep;
        return '#/scenario/' + encodeURIComponent(step.usecaseName) + '/' + encodeURIComponent(step.scenarioName);
    };

    $scope.getStepUrl = function(){
        if($scope.stepSketch == null) {
            return undefined;
        }
        var step = $scope.stepSketch.relatedStep;
        return '#/step/' + encodeURIComponent(step.usecaseName) + '/' + encodeURIComponent(step.scenarioName) + '/' + encodeURIComponent(step.pageName) + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence;
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

