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

angular.module('scenarioo.controllers').controller('MainUseCasesTabCtrl', function ($scope, $location, $filter, GlobalHotkeysService,BranchesAndBuilds, SelectedBranchAndBuild, UseCasesResource, localStorageService) {

    var transformMetadataToTree = $filter('scMetadataTreeCreator')
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    SelectedBranchAndBuild.callOnSelectionChange(loadUseCases);


    function loadUseCases(selected) {

        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(result) {
                $scope.useCases = result;

                var branch = $scope.branchesAndBuilds.selectedBranch.branch;
                var build = $scope.branchesAndBuilds.selectedBuild.build;
                $scope.branchInformationTree = createBranchInformationTree(branch);
                $scope.buildInformationTree = createBuildInformationTree(build);
                $scope.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                $scope.metadataTreeBuilds = transformMetadataToTreeArray(build.details);
            });
    }


    $scope.goToUseCase = function (useCaseName) {
        $location.path('/usecase/' + useCaseName);
    };

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'name', reverse: false}};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    var MAIN_METADATA_SECTION_EXPANDED = 'scenarioo-mainMetadataSectionExpanded-';

    $scope.isMetadataExpanded = function (type) {
        var metadataExpanded = localStorageService.get(MAIN_METADATA_SECTION_EXPANDED + type);
        if (metadataExpanded === 'true') {
            return true;
        } else {
            return false;
        }
    };

    $scope.toggleMetadataExpanded = function (type) {
        var metadataExpanded = !$scope.isMetadataExpanded(type);
        localStorageService.set(MAIN_METADATA_SECTION_EXPANDED + type, '' + metadataExpanded);
    };

    $scope.isMetadataCollapsed = function (type) {
        return !$scope.isMetadataExpanded(type);
    };



    function createBranchInformationTree(branch) {
        var branchInformationTree = {};
        branchInformationTree.Description = branch.description;
        return transformMetadataToTree(branchInformationTree);
    }

    function createBuildInformationTree(build) {
        var buildInformationTree = {};
        buildInformationTree.Date = build.date;
        buildInformationTree.Revision = build.revision;
        buildInformationTree.Status = build.status;
        return transformMetadataToTree(buildInformationTree);

    }

});
