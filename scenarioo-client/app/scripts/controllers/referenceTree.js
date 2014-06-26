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

angular.module('scenarioo.controllers').controller('ReferenceTreeCtrl', function ($scope, $routeParams, ObjectIndexListResource, SelectedBranchAndBuild) {

    var objectType = $routeParams.objectType;
    var objectName = $routeParams.objectName;
    var selectedBranchAndBuild;
    $scope.referenceTree = [];

    SelectedBranchAndBuild.callOnSelectionChange(loadReferenceTree);

    function loadReferenceTree(selected) {
    selectedBranchAndBuild = selected;
    ObjectIndexListResource.get(
        {
            branchName: selected.branch,
            buildName: selected.build,
            objectType: 'page', // objectType,
            objectName: 'searchResults.jsp' // objectName
        },
        function(result) {
            $scope.referenceTree = result;
        });
	}

});	