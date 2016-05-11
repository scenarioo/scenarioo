/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty ofre
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('scenarioo.controllers').controller('CustomTabController', CustomTabController);

CustomTabController.$inject = ['BranchesAndBuildsService', '$location', 'CustomTabContentResource', 'SelectedBranchAndBuildService', 'TreeNodeService'];
function CustomTabController(BranchesAndBuildsService, $location, CustomTabContentResource, SelectedBranchAndBuildService, TreeNodeService) {
    var vm = this;
    vm.searchField = '';
    vm.treemodel = [];

    // Determines if the tree has expanded / collapsed rootnodes initially
    vm.rootIsCollapsed = true;
    vm.toggleLabel = 'expand';
    vm.tabContentTree = [];
    vm.branchesAndBuilds = [];
    vm.selectedBranchAndBuild = {};
    vm.selectedTab = undefined;

    vm.goToReferenceTree = goToReferenceTree;
    vm.expandAndCollapseTree = expandAndCollapseTree;
    vm.resetSearchField = resetSearchField;

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(function (selected) {
        // Initialization on registration of this listener and on all changes to the build selection:
            vm.selectedBranchAndBuild = selected;
            vm.selectedTab = getSelectedTabFromUrl();
            loadContent();
        });
    }
    
    function goToReferenceTree(nodeElement) {
        $location.path('/object/' + nodeElement.type + '/' + nodeElement.name);
    }

    function expandAndCollapseTree(treemodel) {
        TreeNodeService.expandAndCollapseTree(treemodel, this);
    }

    function resetSearchField() {
        vm.searchField = '';
    }


    function getSelectedTabFromUrl() {
        var params = $location.search();
        var selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
        }

        return selectedTabId;
    }

    function loadContent() {

        BranchesAndBuildsService.getBranchesAndBuildsService()
            .then(function onSuccess(branchesAndBuilds) {
                vm.branchesAndBuilds = branchesAndBuilds;
            })
            .then(function () {
                CustomTabContentResource.get({
                    'branchName': vm.selectedBranchAndBuild.branch,
                    'buildName': vm.selectedBranchAndBuild.build,
                    'tabId': vm.selectedTab
                }, function onSuccess(result) {
                    vm.tabContentTree = result.tree;
                });
            });

    }

}
