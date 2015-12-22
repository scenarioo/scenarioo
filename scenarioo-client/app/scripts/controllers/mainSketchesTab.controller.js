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

angular.module('scenarioo.controllers').controller('MainSketchesTabCtrl', function ($scope, $location, SelectedBranchAndBuild, IssuesResource, SketchIdsResource, ContextService) {

    var vm = this;

    vm.table = { search: {searchTerm: ''} };
    vm.loading = true;
    vm.noIssuesExist = false;

    vm.resetSearchField = function () {
        vm.table.search = {searchTerm: ''};
    };

    vm.goToSketchStep = function (issue, stepSketchName) {
        var selectedBranch = SelectedBranchAndBuild.selected().branch;

        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/sketchstep/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            }, function onError(reulst) {
                // not handled
            });

        // TODO can this be removed?
        // ContextService.issueName = issue.name;
        // ContextService.issueDescription = issue.description;
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

});
