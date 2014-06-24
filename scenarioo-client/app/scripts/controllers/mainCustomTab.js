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

angular.module('scenarioo.controllers').controller('MainCustomTabCtrl', function ($scope, $location, $filter, GlobalHotkeysService, CustomTabContentResource, SelectedBranchAndBuild) {

    function getSelectedTabFromUrl() {
        var params = $location.search();
        var selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
        }
        return selectedTabId;
    }

    SelectedBranchAndBuild.callOnSelectionChange(function(selected) {
        // Initialization on registration of this listener and on all changes to the build selection:
        $scope.selectedBranchAndBuild = selected;
        $scope.selectedTab = getSelectedTabFromUrl();
        loadContent();
    });

    function loadContent() {
         CustomTabContentResource.get(
            {'branchName': $scope.selectedBranchAndBuild.branch, 'buildName': $scope.selectedBranchAndBuild.build, 'tabId': $scope.selectedTab},
            function onSuccess(result) {
                $scope.tabContentTree = result.tree;
            }
        );
    }

    $scope.searchField;

    $scope.treeHtml = {search: {$: ''}, sort: {column: 'Name', reverse: false}};

    $scope.resetSearchField = function () {
        $scope.treeHtml.search = {searchTerm: ''};
    };


});
