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
    .component('scCustomTab', {
        bindings: {
            tabId: '<',
            tabColumns: '<',
        },
        template: require('./customTab.html'),
        controller: CustomTabController,
    });

function CustomTabController(BranchesAndBuildsService, $location, CustomTabContentResource,
                             SelectedBranchAndBuildService, TreeNodeService) {

    const ctrl = this;
    ctrl.searchField = '';
    ctrl.treemodel = [];

    // Determines if the tree has expanded / collapsed rootnodes initially
    ctrl.rootIsCollapsed = true;
    ctrl.toggleLabel = 'expand';
    ctrl.tabContentTree = [];
    ctrl.branchesAndBuilds = [];
    ctrl.selectedBranchAndBuild = {};
    ctrl.selectedTab = undefined;

    ctrl.goToReferenceTree = goToReferenceTree;
    ctrl.expandAndCollapseTree = expandAndCollapseTree;
    ctrl.resetSearchField = resetSearchField;

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange((selected) => {
            // Initialization on registration of this listener and on all changes to the build selection:
            ctrl.selectedBranchAndBuild = selected;
            ctrl.selectedTab = getSelectedTabFromUrl();
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
        ctrl.searchField = '';
    }

    function getSelectedTabFromUrl() {
        const params = $location.search();
        let selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
        }

        return selectedTabId;
    }

    function loadContent() {

        BranchesAndBuildsService.getBranchesAndBuilds()
            .subscribe((branchesAndBuilds) => {
                ctrl.branchesAndBuilds = branchesAndBuilds;

                CustomTabContentResource
                    .get({
                        branchName: ctrl.selectedBranchAndBuild.branch,
                        buildName: ctrl.selectedBranchAndBuild.build,
                    }, ctrl.selectedTab)
                    .subscribe((result) => {
                        ctrl.tabContentTree = result.tree;
                    });
            });

    }

}
