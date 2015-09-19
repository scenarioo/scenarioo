/* scenarioo-client
 * Copyright (C) 2015, scenarioo.org Development Team
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


angular.module('scenarioo.controllers').controller('IssueCtrl', function ($scope, $q, $filter, $routeParams,
                                                                            $location, IssueResource, Config, SelectedBranchAndBuild) {

    // TODO #187 What should be done if the branch is changed in the UI?
    SelectedBranchAndBuild.callOnSelectionChange(loadIssue);

    function loadIssue(selected) {
        var issueId = $routeParams.issueId;
        IssueResource.get(
            {
                branchName: selected.branch,
                issueId: issueId
            },
            function onSuccess(result) {
                $scope.issue = result.issue;
                $scope.scenarioSketches = result.scenarioSketches;
                $scope.issueName = result.issue.name;
                $scope.issueId = result.issue.id;
                $scope.author = result.issue.author;
                $scope.usecaseContext = result.issue.usecaseContext;
            },
            function onError(){
                // error not handled
            }
        );
    }

    $scope.goToScenarioSketch = function (scenarioSketchId) {
        $location.path('/scenariosketch/' + $routeParams.issueId + '/' + scenarioSketchId);
    };

    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

});
