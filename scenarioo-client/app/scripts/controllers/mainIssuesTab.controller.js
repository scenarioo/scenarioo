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


angular.module('scenarioo.controllers').controller('MainIssuesTabCtrl', function ($scope, $location, SelectedBranchAndBuild, IssuesResource, ContextService) {

    var vm = this;

    vm.table = {search: {searchTerm: ''}, sort: {column: 'name', reverse: false}};

    vm.resetSearchField = function () {
        vm.table.search = {searchTerm: ''};
    };

    vm.goToSketchStep = function (issue, sketchStepName) {
        sketchStepName = sketchStepName || 1;
        ContextService.issueName = issue.name;
        ContextService.issueDescription = issue.description;
        $location.path('/sketchstep/' + issue.id + '/' + issue.firstScenarioSketchId + '/' + sketchStepName);
    };

    activate();

    function activate() {
        SelectedBranchAndBuild.callOnSelectionChange(loadIssues);
    }

    function loadIssues() {
        var selectedBranch = SelectedBranchAndBuild.selected().branch;

        IssuesResource.query(
            {'branchName': selectedBranch},
            function onSuccess(result) {
                $scope.issues = result;
            },
            function onError() {
                // Error case not handled
            });
    }

});
