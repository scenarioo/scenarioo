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

angular.module('scenarioo.controllers').controller('BuildsListController', BuildsListController);

function BuildsListController($scope, $route, $uibModal, BuildImportStatesResource, BuildImportService, BuildReimportResource,
                              BuildImportLogResource) {

    var vm = this;

    vm.buildImportStates = [];
    vm.table = {search: {searchTerm: ''}, sort: {column: 'buildDescription.date', reverse: true}, filtering: false};
    $scope.table = vm.table; // expose "table" onto controller scope. is used at the moment by "sortableColumn" directive.

    vm.updatingBuildsInProgress = false;
    var styleClassesForBuildImportStatus = {
        'SUCCESS': 'label-success',
        'FAILED': 'label-danger',
        'UNPROCESSED': 'label-default',
        'QUEUED_FOR_PROCESSING': 'label-info',
        'PROCESSING': 'label-primary',
        'OUTDATED': 'label-warning'
    };
    vm.resetSearchField = resetSearchField;
    vm.goToBuild = goToBuild;
    vm.reimportBuild = reimportBuild;
    vm.getStyleClassForBuildImportStatus = getStyleClassForBuildImportStatus;
    vm.importAndUpdateBuilds = importAndUpdateBuilds;

    activate();

    function activate() {
        BuildImportStatesResource.query({}, buildImportStates => {
            vm.buildImportStates = buildImportStates;
        });
    }

    function resetSearchField() {
        vm.table.search = {searchTerm: ''};
    }

    function goToBuild(build) {
        BuildImportLogResource.get(build.identifier.branchName, build.identifier.buildName, function onSuccess(log) {
            $uibModal.open({
                template: require('./buildImportDetails.html'),
                controller: 'BuildImportDetailsController',
                controllerAs: 'vm',
                windowClass: 'modal-wide',
                resolve: {
                    build: function () {
                        return build;
                    },
                    log: function () {
                        return log;
                    },
                    getStyleClassForBuildImportStatus: function () {
                        return vm.getStyleClassForBuildImportStatus;
                    }
                }
            });
        }, error => {
            throw error;
        });
    }

    function reimportBuild(build) {
        vm.updatingBuildsInProgress = true;
        BuildReimportResource.get({branchName: build.identifier.branchName, buildName: build.identifier.buildName},
            buildImportFinished, buildImportFinished);
    }

    function getStyleClassForBuildImportStatus(status) {
        var styleClassFromMapping = styleClassesForBuildImportStatus[status];
        if (angular.isUndefined(styleClassFromMapping)) {
            return 'label-warning';
        } else {
            return styleClassFromMapping;
        }
    }

    function importAndUpdateBuilds() {
        vm.updatingBuildsInProgress = true;
        BuildImportService.updateData()
            .toPromise()
            .then(buildImportFinished, buildImportFinished);
    }

    function buildImportFinished() {
        vm.updatingBuildsInProgress = false;
        $route.reload();
    }
}


