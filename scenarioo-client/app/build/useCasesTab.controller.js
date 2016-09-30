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
                               SelectedComparison, DiffInfoService, UseCasesResource, LabelConfigurationsResource, BuildDiffInfoResource, UseCaseDiffInfosResource) {

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
    vm.comparisonInfo = SelectedComparison.info;

    vm.gotoUseCase = gotoUseCase;
    vm.onNavigatorTableHit = onNavigatorTableHit;
    vm.resetSearchField = resetSearchField;

    vm.getLabelStyle = getLabelStyle;

    var transformMetadataToTree = $filter('scMetadataTreeCreator');
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var dateTimeFormatter = $filter('scDateTime');

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadUseCases);

        LabelConfigurationsResource.query({}, function (labelConfiguratins) {
            vm.labelConfigurations = labelConfiguratins;
        });
    }

    function gotoUseCase(useCase){
        if(!useCase.diffInfo || !useCase.diffInfo.isRemoved){
            $location.path('/usecase/' + useCase.name);
        }
    }

    function onNavigatorTableHit(useCase) {
        vm.gotoUseCase(useCase);
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
        BranchesAndBuildsService.getBranchesAndBuilds()
            .then(function onSuccess(branchesAndBuilds) {
                vm.branchesAndBuilds = branchesAndBuilds;

                UseCasesResource.query(
                    {'branchName': selected.branch, 'buildName': selected.build},
                    function onSuccess(useCases) {
                        if(SelectedComparison.isDefined()) {
                            loadDiffInfoData(useCases, selected.branch, selected.build, SelectedComparison.selected());
                        } else {
                            vm.useCases = useCases;
                        }

                        var branch = vm.branchesAndBuilds.selectedBranch.branch;
                        var build = vm.branchesAndBuilds.selectedBuild.build;
                        vm.branchInformationTree = createBranchInformationTree(branch);
                        vm.buildInformationTree = createBuildInformationTree(build);
                        vm.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                        vm.metadataTreeBuilds = transformMetadataToTreeArray(build.details);
                    });
        });
    }

    function loadDiffInfoData(useCases, baseBranchName, baseBuildName, comparisonName) {
        if(useCases && baseBranchName && baseBuildName) {
            BuildDiffInfoResource.get(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                function onSuccess(buildDiffInfo) {
                    UseCaseDiffInfosResource.get(
                        {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                        function onSuccess(useCaseDiffInfos) {
                            vm.useCases = DiffInfoService.getElementsWithDiffInfos(useCases, buildDiffInfo.removedElements, useCaseDiffInfos, 'name');
                        }
                    );
                }, function onFailure(error){
                    throw error;
                }
            );
        }
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
