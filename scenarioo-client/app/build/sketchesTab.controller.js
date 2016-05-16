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

angular.module('scenarioo.controllers').controller('SketchesTabController', SketchesTabController);

function SketchesTabController($scope, $location, SelectedBranchAndBuildService, IssuesResource, SketchIdsResource) {

    var vm = this;

    vm.table = { search: {searchTerm: ''} };
    vm.loading = true;
    vm.noIssuesExist = false;

    vm.resetSearchField = function () {
        vm.table.search = {searchTerm: ''};
    };

    vm.goToStepSketch = function (issue) {
        var selectedBranch = SelectedBranchAndBuildService.selected().branch;

        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    };

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadIssues);
    }

    function loadIssues() {
        var selectedBranch = SelectedBranchAndBuildService.selected().branch;

        IssuesResource.query(
            {'branchName': selectedBranch},
            function onSuccess(result) {
                $scope.issues = result;
                if (result.length === 0) {
                    vm.noIssuesExist = true;
                }
                vm.loading = false;
            },
            function onError() {
                vm.loading = false;
                // Error case not handled
            });
    }

}
