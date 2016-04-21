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

angular.module('scenarioo.controllers').controller('UseCaseCtrl', UseCaseCtrl);

function UseCaseCtrl($scope, $filter, $routeParams, $location, ScenarioResource, Config, SelectedBranchAndBuild,
                     LabelConfigurationsResource, RelatedIssueResource, SketchIdsResource) {

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
    vm.useCase = {};
    vm.scenarios = [];
    vm.usecaseInformationTree = {};
    vm.metadataTree = {};
    vm.relatedIssues = {};
    vm.hasAnyLabels = false;

    vm.resetSearchField = resetSearchField;
    vm.goToFirstStep = goToFirstStep;
    vm.goToScenario = goToScenario;
    vm.onNavigatorTableHit = onNavigatorTableHit;
    vm.getLabelStyle = getLabelStyle;


    activate();


    function resetSearchField() {
        vm.table.search = {searchTerm: ''};
    }

    function goToScenario(useCaseName, scenarioName) {
        $location.path('/scenario/' + useCaseName + '/' + scenarioName);
    }

    function onNavigatorTableHit(scenario) {
        goToScenario($routeParams.useCaseName, scenario.scenario.name);
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

    function goToFirstStep(useCaseName, scenarioName) {
        var selected = SelectedBranchAndBuild.selected();

        //FIXME This could be improved, if the scenario service
        // for finding all scenarios would also retrieve the name of the first page
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName,
                scenarioName: scenarioName
            },
            function onSuccess(scenarioResult) {
                $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + scenarioResult.pagesAndSteps[0].page.name + '/0/0');
            }
        );
    }

    function activate() {

        SelectedBranchAndBuild.callOnSelectionChange(loadScenariosAndUseCase);

        LabelConfigurationsResource.query({}, function (labelConfigurations) {
            vm.labelConfigurations = labelConfigurations;
        });
    }

    function loadScenariosAndUseCase(selected) {
        var useCaseName = $routeParams.useCaseName;

        ScenarioResource.get({
            branchName: selected.branch,
            buildName: selected.build,
            usecaseName: useCaseName
        }, onUseCaseLoaded);
        vm.propertiesToShow = Config.scenarioPropertiesInOverview();

    }

    function onUseCaseLoaded(result) {
        vm.useCase = result.useCase;
        vm.scenarios = result.scenarios;
        vm.usecaseInformationTree = createUseCaseInformationTree(vm.useCase);
        vm.metadataTree = $filter('scMetadataTreeListCreator')(vm.useCase.details);
        vm.hasAnyLabels = vm.useCase.labels && vm.useCase.labels.labels.length !== 0;
        loadRelatedIssues();
    }

    function loadRelatedIssues(){
        RelatedIssueResource.query({
            branchName: SelectedBranchAndBuild.selected().branch,
            buildName: SelectedBranchAndBuild.selected().build,
            useCaseName: $routeParams.useCaseName
        }, function(result){
            $scope.relatedIssues = result;
            $scope.hasAnyRelatedIssues = function(){
                return $scope.relatedIssues.length > 0;
            };
            $scope.goToIssue = goToIssue;
        });
    }

    function goToIssue(issue) {
        var selectedBranch = SelectedBranchAndBuild.selected().branch;
        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    }

    function createUseCaseInformationTree(usecase) {
        var usecaseInformation = {};
        usecaseInformation.Status = usecase.status;
        return $filter('scMetadataTreeCreator')(usecaseInformation);
    }

}
