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

angular.module('scenarioo.controllers').controller('BuildsListCtrl', function ($scope, $location, $route, $uibModal, BuildImportStatesResource, BuildImportService, BuildReimportResource, BuildImportLogResource) {

    BuildImportStatesResource.query({}, function(buildImportStates) {
        $scope.buildImportStates = buildImportStates;
    });

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'buildDescription.date', reverse: true}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    $scope.goToBuild = function (build) {
        BuildImportLogResource.get(build.identifier.branchName, build.identifier.buildName, function onSuccess(log) {
            $uibModal.open({
                templateUrl: 'views/manage/buildImportDetails.html',
                controller: 'BuildImportDetailsCtrl',
                windowClass: 'modal-wide',
                resolve: {
                    build: function () { return build; },
                    log: function() { return log; },
                    getStyleClassForBuildImportStatus: function() { return $scope.getStyleClassForBuildImportStatus; }
                }
            });
        });
    };

    $scope.reimportBuild = function (build) {
        $scope.updatingBuildsInProgress = true;
        BuildReimportResource.get({branchName: build.identifier.branchName, buildName: build.identifier.buildName },
                function onSuccess() {
                    $scope.updatingBuildsInProgress = false;
                    $route.reload();
                },
                function onError() {
                    $scope.updatingBuildsInProgress = false;
                    $route.reload();
                }
            );
    };

    $scope.getStyleClassForBuildImportStatus = function(status) {
        var styleClassFromMapping = $scope.styleClassesForBuildImportStatus[status];
        if (angular.isUndefined(styleClassFromMapping)) {
            return 'label-warning';
        }
        else {
            return styleClassFromMapping;
        }
    };

    $scope.styleClassesForBuildImportStatus = {
        'SUCCESS': 'label-success',
        'FAILED': 'label-danger',
        'UNPROCESSED': 'label-default',
        'QUEUED_FOR_PROCESSING': 'label-info',
        'PROCESSING': 'label-primary',
        'OUTDATED': 'label-warning'
    };

    /**
     * Is set to true while call to server for updating available documentation builds is in progress
     */
    $scope.updatingBuildsInProgress = false;

    $scope.importAndUpdateBuilds = function () {
        $scope.updatingBuildsInProgress = true;

        var result = BuildImportService.updateData({});
        result.then(function onSuccess() {
            $scope.updatingBuildsInProgress = false;
            $route.reload();
        }, function onError() {
            $scope.updatingBuildsInProgress = false;
            $route.reload();
        });
    };


});


