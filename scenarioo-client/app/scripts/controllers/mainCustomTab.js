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

angular.module('scenarioo.controllers').controller('MainCustomTabCtrl', function ($scope, $location, $filter,
    CustomTabContentResource, SelectedBranchAndBuild, TreeNode) {
    $scope.searchField = '';
    $scope.treemodel = [];

    // Determines if the tree has expanded / collapsed rootnodes initially
    $scope.rootIsCollapsed = true;
    $scope.toggleLabel = 'expand';
    $scope.collapsedIconName = 'collapsed.png';
    $scope.expandedIconName= 'expanded.png';

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
            {'branchName': $scope.selectedBranchAndBuild.branch,
                'buildName': $scope.selectedBranchAndBuild.build, 'tabId': $scope.selectedTab},
            function onSuccess(result) {
                $scope.tabContentTree = result.tree;
            }
        );
    }

    $scope.goToReferenceTree = function (nodeElement) {
        $location.path('/referenceTree/' + nodeElement.type + '/' + nodeElement.name);
    };

    $scope.expandAndCollapseTree = function(treemodel, toggleLabel) {
        TreeNode.expandAndCollapseTree(treemodel, $scope);
    };

    $scope.resetSearchField = function() {
        $scope.searchField = '';
    };
});
