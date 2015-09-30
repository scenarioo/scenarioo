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

angular.module('scenarioo.controllers').controller('ScenarioSketchCtrl', function ($scope, $q, $filter, $routeParams,
                                                                          $location, ScenarioSketchResource, Config, SelectedBranchAndBuild) {

    // TODO #187 What should be done if the branch is changed in the UI?
    SelectedBranchAndBuild.callOnSelectionChange(loadScenarioSketch);

    function loadScenarioSketch(selected) {
        var issueId = $routeParams.issueId;
        var scenarioSketchId = $routeParams.scenarioSketchId;
        ScenarioSketchResource.get(
            {
                branchName: selected.branch,
                issueId: issueId,
                scenarioSketchId: scenarioSketchId
            },
            function onSuccess(result) {
                $scope.issue = result.issue;
                $scope.scenarioSketch = result.scenarioSketch;
                $scope.issueName = result.issue.name;
                $scope.issueId = result.issue.issueId;
                $scope.scenarioSketchName = result.scenarioSketch.scenarioSketchName;
                $scope.scenarioSketchId = result.scenarioSketch.scenarioSketchId;
                $scope.sketchSteps = result.sketchSteps;
            },
            function onError(){
                // Error not handled
            }
        );
    }

    // todo
    $scope.goToSketchStep = function (sketchStepIndex) {
        $location.path('/sketchstep/' + $routeParams.issueId + '/' + $scope.scenarioSketchId + '/' + sketchStepIndex);
    };

    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

});
