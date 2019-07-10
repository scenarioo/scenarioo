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

angular.module('scenarioo.directives')
    .component('scSketchesTab', {
        template: require('./sketchesTab.html'),
        controller: SketchesTabController,
    });

function SketchesTabController($scope, $location, SelectedBranchAndBuildService, IssuesResource, SketchIdsResource) {

    const ctrl = this;

    ctrl.table = { search: {searchTerm: ''} };
    ctrl.loading = true;
    ctrl.noIssuesExist = false;

    ctrl.resetSearchField = () => {
        ctrl.table.search = {searchTerm: ''};
    };

    ctrl.goToStepSketch = (issue) => {
        const selectedBranch = SelectedBranchAndBuildService.selected().branch;

        SketchIdsResource.get(
            {
                branchName: selectedBranch,
                issueId: issue.id,
            },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    };

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadIssues);
    }

    function loadIssues() {
        const selectedBranch = SelectedBranchAndBuildService.selected().branch;

        IssuesResource.query(
            { branchName: selectedBranch },
            function onSuccess(result) {
                $scope.issues = result;
                if (result.length === 0) {
                    ctrl.noIssuesExist = true;
                }
                ctrl.loading = false;
            },
            function onError() {
                ctrl.loading = false;
                // Error case not handled
            });
    }

}
