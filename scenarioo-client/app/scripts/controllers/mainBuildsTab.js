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

angular.module('scenarioo.controllers').controller('MainBuildsTabCtrl', function ($scope, BuildImportStatesResource, BuildImportLogResource, $window) {

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

    $scope.goToBuild = function (buildIdentifier) {
        BuildImportLogResource.get(buildIdentifier.branchName, buildIdentifier.buildName, function onSuccess(log) {
            // TODO #81: introduce a nicer popup for the logs to be displayed (using $modal).
            $window.alert('Log-Output for Import of Build ' + buildIdentifier.branchName + '/' + buildIdentifier.buildName + ':\n' + log);
        });
    };

});