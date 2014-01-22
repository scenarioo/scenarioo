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

'use strict';

angular.module('scenarioo.controllers').controller('MainBuildsTabCtrl', function ($scope, BuildImportStatesResource, BuildImportLogResource, $modal) {

    BuildImportStatesResource.query({}, function(buildImportStates) {
        $scope.buildImportStates = buildImportStates;
    });

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'build.date', reverse: true}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    $scope.toggleFilter = function () {
        $scope.table.filtering = !$scope.table.filtering;
        if (!$scope.table.filtering) {
            // Removes filter values when filter is switched off
            var searchTerm = $scope.table.search.searchTerm;
            $scope.table.search = { searchTerm: searchTerm };
        }
    };

    $scope.goToBuild = function (build) {
        BuildImportLogResource.get(build.identifier.branchName, build.identifier.buildName, function onSuccess(log) {
            $modal.open({
                templateUrl: 'buildImportStatusDialog.html',
                controller: BuildImportStatusDialogCtrl,
                windowClass: 'modal-wide',
                resolve: {
                    build: function () { return build; },
                    log: function() { return log; },
                    styleClassesForBuildImportStatus: function() { return $scope.styleClassesForBuildImportStatus; }
                }
            });
        });
    };

    $scope.styleClassesForBuildImportStatus = {
        'SUCCESS': 'label-success',
        'FAILED': 'label-danger',
        'UNPROCESSED': 'label-warning',
        'QUEUED_FOR_PROCESSING': 'label-warning',
        'PROCESSING': 'label-warning',
        'OUTDATED': 'label-warning'
    };

});


/** Sub-Controller for BuildImportStatus-Dialog **/
var BuildImportStatusDialogCtrl = function ($scope, $modalInstance, build, log, styleClassesForBuildImportStatus) {

    $scope.build = build;
    $scope.log = log;
    $scope.styleClassesForBuildImportStatus = styleClassesForBuildImportStatus;

    $scope.hasImportMessage = function() {
        if (angular.isUndefined($scope.build.statusMessage)) {
            return false;
        }
        else {
            return true;
        }
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

};