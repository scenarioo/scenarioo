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

angular.module('scenarioo.controllers').controller('UseCasesTabController', UseCasesTabController);

function UseCasesTabController($scope, $location, $filter, BranchesAndBuildsService, SelectedBranchAndBuildService,
                               UseCasesResource, LabelConfigurationsResource) {

    var vm = this;
    vm.table = {
        search: {searchTerm: ''},
        sort: {column: 'name', reverse: false}
    };
    $scope.table = vm.table;
    vm.labelConfigurations = undefined;
    vm.branchesAndBuilds = [];
    vm.useCases = [];
    vm.branchInformationTree = {};
    vm.buildInformationTree = {};
    vm.metadataTreeBranches = {};
    vm.metadataTreeBuilds = {};

    vm.goToUseCase = goToUseCase;
    vm.onNavigatorTableHit = onNavigatorTableHit;
    vm.resetSearchField = resetSearchField;

    // FIXME this code is duplicated. How can we extract it into a service?
    vm.getLabelStyle = getLabelStyle;

    var transformMetadataToTree = $filter('scMetadataTreeCreator');
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var dateTimeFormatter = $filter('scDateTime');

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadUseCases);

        // FIXME this code is duplicated. How can we extract it into a service?
        LabelConfigurationsResource.query({}, function (labelConfiguratins) {
            vm.labelConfigurations = labelConfiguratins;
        });
    }
    
    function goToUseCase(useCaseName) {
        $location.path('/usecase/' + useCaseName);
    }

    function onNavigatorTableHit(useCase) {
        vm.goToUseCase(useCase.name);
    }

    function resetSearchField() {
        vm.table.search = {searchTerm: ''};
    }

    function getLabelStyle(labelName) {
        if (vm.labelConfigurations) {
            var labelConfig = vm.labelConfigurations[labelName];
            if (labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }
    }
    
    function loadUseCases(selected) {

        BranchesAndBuildsService.getBranchesAndBuildsService()
            .then(function onSuccess(branchesAndBuilds) {
                vm.branchesAndBuilds = branchesAndBuilds;
            }).then(function () {
            UseCasesResource.query(
                {'branchName': selected.branch, 'buildName': selected.build},
                function onSuccess(result) {
                    vm.useCases = result;

                    var branch = vm.branchesAndBuilds.selectedBranch.branch;
                    var build = vm.branchesAndBuilds.selectedBuild.build;
                    vm.branchInformationTree = createBranchInformationTree(branch);
                    vm.buildInformationTree = createBuildInformationTree(build);
                    vm.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                    vm.metadataTreeBuilds = transformMetadataToTreeArray(build.details);
                });
        });

    }

    function createBranchInformationTree(branch) {
        var branchInformationTree = {};
        branchInformationTree.Description = branch.description;
        return transformMetadataToTree(branchInformationTree);
    }

    function createBuildInformationTree(build) {
        var buildInformationTree = {};
        buildInformationTree.Date = dateTimeFormatter(build.date);
        buildInformationTree.Revision = build.revision;
        buildInformationTree.Status = build.status;
        return transformMetadataToTree(buildInformationTree);
    }

}
