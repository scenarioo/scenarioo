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
angular.module('scenarioo.controllers').controller('FeatureController', FeatureController);

function FeatureController($scope, $filter, $routeParams, $location, ScenarioResource, ConfigService,
                           SelectedBranchAndBuildService, SelectedComparison, DiffInfoService, LabelConfigurationsResource, RelatedIssueResource,
                           SketchIdsResource, FeatureDiffInfoResource, ScenarioDiffInfosResource) {

    var vm = this;

    vm.table = {
        search: {$: ''},
        sort: {
            column: 'scenario.name',
            reverse: false
        }
    };
    $scope.table = vm.table; // expose "table" onto controller scope. is used at the moment by "sortableColumn" directive.
    vm.propertiesToShow = [];
    vm.labelConfigurations = {};
    vm.feature = {};
    vm.scenarios = [];
    vm.featureInformationTree = {};
    vm.metadataTree = {};
    vm.relatedIssues = {};
    vm.hasAnyLabels = false;

    vm.resetSearchField = resetSearchField;
    vm.clickScenario = handleClick;
    vm.goToFirstStep = goToFirstStep;
    vm.goToScenario = goToScenario;
    vm.onNavigatorTableHit = onNavigatorTableHit;
    vm.getLabelStyle = getLabelStyle;
    vm.goToIssue = goToIssue;

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadScenariosAndFeature);

        LabelConfigurationsResource.query({}, function (labelConfigurations) {
            vm.labelConfigurations = labelConfigurations;
        });
    }

    $scope.comparisonInfo = SelectedComparison.info;


    function resetSearchField() {
        vm.table.search = {searchTerm: ''};
    }

    function handleClick(featureName, scenarioSummary) {
        if(!scenarioSummary.diffInfo || !scenarioSummary.diffInfo.isRemoved){
            goToScenario(featureName, scenarioSummary.scenario.name);
        }
    }

    function goToScenario(featureName, scenarioName) {
        $location.path('/scenario/' + featureName + '/' + scenarioName);
    }

    function onNavigatorTableHit(scenario) {
        goToScenario($routeParams.featureName, scenario.scenario.name);
    }

    // FIXME this code is duplicated. How can we extract it into a service?
    function getLabelStyle(labelName) {
        var labelConfig = vm.labelConfigurations[labelName];
        if (labelConfig) {
            return {
                'background-color': labelConfig.backgroundColor,
                'color': labelConfig.foregroundColor
            };
        }
    }

    function goToFirstStep(featureName, scenarioName) {
        var selected = SelectedBranchAndBuildService.selected();

        // FIXME This could be improved, if the scenario service
        // for finding all scenarios would also retrieve the name of the first page
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                featureName: featureName,
                scenarioName: scenarioName
            },
            function onSuccess(scenarioResult) {
                $location.path('/step/' + featureName + '/' + scenarioName + '/' + scenarioResult.pagesAndSteps[0].page.name + '/0/0');
            }
        );
    }

    function loadScenariosAndFeature(selected) {
        var featureName = $routeParams.featureName;

        ScenarioResource.get({
            branchName: selected.branch,
            buildName: selected.build,
            featureName: featureName
        }, onFeatureLoaded);
        vm.propertiesToShow = ConfigService.scenarioPropertiesInOverview();

    }

    function onFeatureLoaded(result) {
        vm.feature = result.feature;
        vm.featureInformationTree = createFeatureInformationTree(vm.feature);
        vm.metadataTree = $filter('scMetadataTreeListCreator')(vm.feature.details);
        vm.hasAnyLabels = vm.feature.labels && vm.feature.labels.labels.length !== 0;

        if(SelectedComparison.isDefined()) {
            var selected = SelectedBranchAndBuildService.selected();
            loadDiffInfoData(result.scenarios, selected.branch, selected.build, SelectedComparison.selected(), result.feature.name);
        } else {
            vm.scenarios = result.scenarios;
        }

        loadRelatedIssues();
    }

    function loadDiffInfoData(scenarios, baseBranchName, baseBuildName, comparisonName, featureName) {
        if (scenarios && baseBranchName && baseBuildName && featureName){
            FeatureDiffInfoResource.get(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName, 'featureName': featureName},
                function onSuccess(featureDiffInfo) {
                    ScenarioDiffInfosResource.get(
                        {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName, 'featureName': featureName},
                        function onSuccess(scenarioDiffInfos) {
                            vm.scenarios = DiffInfoService.getElementsWithDiffInfos(scenarios, featureDiffInfo.removedElements, scenarioDiffInfos, 'scenario.name');
                        }
                    );
                }, function onFailure() {
                    vm.scenarios = DiffInfoService.getElementsWithDiffInfos(scenarios, [], [], 'scenario.name');
                }
            );
        }
    }

    function loadRelatedIssues(){
        RelatedIssueResource.query({
            branchName: SelectedBranchAndBuildService.selected().branch,
            buildName: SelectedBranchAndBuildService.selected().build,
            featureName: $routeParams.featureName
        }, function(result){
            vm.relatedIssues = result;
            vm.hasAnyRelatedIssues = vm.relatedIssues.length > 0;
        });
    }

    function goToIssue(issue) {
        var selectedBranch = SelectedBranchAndBuildService.selected().branch;
        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    }

    function createFeatureInformationTree(feature) {
        var featureInformation = {};
        featureInformation['Feature'] = feature.name;
        if(feature.description) {
            featureInformation.Description = feature.description;
        }
        featureInformation.Status = feature.status;
        return $filter('scMetadataTreeCreator')(featureInformation);
    }

}
