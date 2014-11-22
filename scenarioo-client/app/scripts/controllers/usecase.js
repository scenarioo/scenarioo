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

angular.module('scenarioo.controllers').controller('UseCaseCtrl', function ($scope, $q, $filter, $routeParams,
                                                                            $location, ScenarioResource, Config, SelectedBranchAndBuild,
                                                                            LabelConfigurationsResource) {

    var transformMetadataToTree = $filter('scMetadataTreeCreator');
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    SelectedBranchAndBuild.callOnSelectionChange(loadScenariosAndUseCase);

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function(labelConfiguratins) {
        $scope.labelConfigurations = labelConfiguratins;
    });

    function loadScenariosAndUseCase(selected) {
        var useCaseName = $routeParams.useCaseName;
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName
            },
            function onSuccess(result) {
                $scope.useCase = result.useCase;
                $scope.scenarios = result.scenarios;
                $scope.usecaseInformationTree = createUseCaseInformationTree($scope.useCase);
                $scope.metadataTree = transformMetadataToTreeArray($scope.useCase.details);
                $scope.hasAnyLabels =  $scope.useCase.labels && $scope.useCase.labels.labels.length !== 0;
            }
        );

        $scope.propertiesToShow = Config.scenarioPropertiesInOverview();
    }

    $scope.goToScenario = function (useCaseName, scenarioName) {
        $location.path('/scenario/' + useCaseName + '/' + scenarioName);
    };

    $scope.onNavigatorTableHit = function (scenario) {
        $scope.goToScenario($routeParams.useCaseName, scenario.scenario.name);
    };

    $scope.goToFirstStep = function (useCaseName, scenarioName) {
        var selected = SelectedBranchAndBuild.selected();

        //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName,
                scenarioName: scenarioName
            },
            function onSuccess(scenarioResult) {
                $location.path('/step/' + encodeURIComponent(useCaseName) + '/' + encodeURIComponent(scenarioName) + '/' + encodeURIComponent(scenarioResult.pagesAndSteps[0].page.name) + '/0/0');
            }
        );
    };
    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}};

    function createUseCaseInformationTree(usecase) {
        var usecaseInformation = {};
        usecaseInformation.Status = usecase.status;
        return transformMetadataToTree(usecaseInformation);
    }

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    // FIXME this code is duplicated. How can we extract it into a service?
    $scope.getLabelStyle = function(labelName) {
        var labelConfig = $scope.labelConfigurations[labelName];
        if(labelConfig) {
            return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
        }
    };
});
