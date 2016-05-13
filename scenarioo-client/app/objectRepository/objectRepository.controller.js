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

angular.module('scenarioo.controllers').controller('ObjectRepositoryController', ObjectRepositoryController);


function ObjectRepositoryController($routeParams, $location, ObjectIndexListResource, SelectedBranchAndBuildService, TreeNodeService, ReferenceTreeNavigationService, $filter) {
    var vm = this;

    vm.treemodel = [];
    vm.object = {};
    vm.searchField = '';
    vm.metadataTree = [];
    // Determines if the tree has expanded / collapsed root-nodes initially
    vm.rootIsCollapsed = false;
    vm.toggleLabel = 'collapse';
    vm.goToRelatedView = goToRelatedView;
    vm.expandAndCollapseTree = expandAndCollapseTree;
    vm.resetSearchField = resetSearchField;

    var objectType = $routeParams.objectType;
    var objectName = $routeParams.objectName;
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadReferenceTree);
    }

    function loadReferenceTree(selected) {

        // Get all references for given object
        ObjectIndexListResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                objectType: objectType,
                objectName: objectName
            },
            function (result) {
                vm.object = result;
                var transformedMetaDataTree = transformMetadataToTree(result.object.details);
                vm.metadataTree = transformedMetaDataTree.childNodes;
            });
    }

    // Entry point when a tree entry is clicked
    function goToRelatedView(nodeElement) {
        $location.path(ReferenceTreeNavigationService.goToRelatedView(nodeElement, vm.treemodel));
    }

    function expandAndCollapseTree(treemodel) {
        TreeNodeService.expandAndCollapseTree(treemodel, vm);
    }

    function resetSearchField() {
        vm.searchField = '';
    }
}
