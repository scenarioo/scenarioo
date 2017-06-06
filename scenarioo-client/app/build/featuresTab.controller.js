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

angular.module('scenarioo.controllers').controller('FeaturesTabController', FeaturesTabController);

function FeaturesTabController($scope, $location, $filter, BranchesAndBuildsService, SelectedBranchAndBuildService,
                               SelectedComparison, DiffInfoService, FeaturesResource, LabelConfigurationsResource, BuildDiffInfoResource, FeatureDiffInfosResource) {

    var vm = this;
    vm.table = {
        search: {searchTerm: ''},
        sort: {column: 'name', reverse: false}
    };
    $scope.table = vm.table;
    vm.labelConfigurations = undefined;
    vm.branchesAndBuilds = [];
    vm.features = [];
    vm.branchInformationTree = {};
    vm.buildInformationTree = {};
    vm.metadataTreeBranches = {};
    vm.metadataTreeBuilds = {};
    vm.comparisonInfo = SelectedComparison.info;

    vm.gotoFeature = gotoFeature;
    vm.onNavigatorTableHit = onNavigatorTableHit;
    vm.resetSearchField = resetSearchField;

    vm.getLabelStyle = getLabelStyle;

    var transformMetadataToTree = $filter('scMetadataTreeCreator');
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var dateTimeFormatter = $filter('scDateTime');

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadFeatures);

        LabelConfigurationsResource.query({}, function (labelConfiguratins) {
            vm.labelConfigurations = labelConfiguratins;
        });
    }

    function gotoFeature(feature){
        if(!isRemovedFeature(feature)){
            $location.path('/feature/' + feature.name);
        }
    }

    function isRemovedFeature(feature) {
        return feature.diffInfo && feature.diffInfo.isRemoved;
    }

    function onNavigatorTableHit(feature) {
        vm.gotoFeature(feature);
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

    function loadFeatures(selected) {
        BranchesAndBuildsService.getBranchesAndBuilds()
            .then(function onSuccess(branchesAndBuilds) {
                vm.branchesAndBuilds = branchesAndBuilds;

                FeaturesResource.query(
                    {'branchName': selected.branch, 'buildName': selected.build},
                    function onSuccess(features) {
                        if(SelectedComparison.isDefined()) {
                            loadDiffInfoData(features, selected.branch, selected.build, SelectedComparison.selected());
                        } else {
                            vm.features = features;
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

    function loadDiffInfoData(features, baseBranchName, baseBuildName, comparisonName) {
        if(features && baseBranchName && baseBuildName) {
            BuildDiffInfoResource.get(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                function onSuccess(buildDiffInfo) {
                    FeatureDiffInfosResource.get(
                        {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                        function onSuccess(featureDiffInfos) {
                            vm.features = DiffInfoService.getElementsWithDiffInfos(features, buildDiffInfo.removedElements, featureDiffInfos, 'name');
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
