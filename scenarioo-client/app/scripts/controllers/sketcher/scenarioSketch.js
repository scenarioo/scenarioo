/* scenarioo-client
 * Copyright (C) 2015, scenarioo.org Development Team
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


angular.module('scenarioo.controllers').controller('ScenarioSketchCtrl', function ($scope, $q, $filter, $routeParams,
                                                                          $location, ScenarioSketchResource, Config, SelectedBranchAndBuild,
                                                                          LabelConfigurationsResource) {

    //var transformMetadataToTree = $filter('scMetadataTreeCreator');
    //var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    SelectedBranchAndBuild.callOnSelectionChange(loadScenarioSketch);

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function(labelConfigurations) {
        $scope.labelConfigurations = labelConfigurations;
    });

    function loadScenarioSketch(selected) {
        var issueId = $routeParams.issueId;
        var scenarioSketchId = $routeParams.scenarioSketchId;
        ScenarioSketchResource.get(
            {
                branchName: selected.branch,
                issueId: issueId,
                scenarioSketchId: scenarioSketchId
            },
            function onSuccess(result) {
                //console.log(result);
                $scope.issue = result.issue;
                $scope.scenarioSketch = result.scenarioSketch;
                $scope.issueName = result.issue.name;
                $scope.issueId = result.issue.issueId;
                $scope.scenarioSketchName = result.scenarioSketch.scenarioSketchName;
                $scope.scenarioSketchId = result.scenarioSketch.scenarioSketchId;
                $scope.sketchSteps = result.sketchSteps;
                //$scope.scenarios = result.scenarios;
                //$scope.issueInformationTree = createissueInformationTree($scope.issue);
                //$scope.metadataTree = transformMetadataToTreeArray($scope.issue.details);
                //$scope.hasAnyLabels =  $scope.issue.labels && $scope.issue.labels.labels.length !== 0;
            },
            function onError(){

                var scenarioSketches = [
                    {scenarioSketch: {
                        author: 'mzem',
                        name: 'first sketch',
                        description: 'Die Dom�nen Design und Docu sind in diesem Sketch stark getrennt. Im Header werden Tabs eingesetzt.',
                        id: 53,
                        dateModified: '2015-04-21T09:26:48+00:00',
                        stepCount: 3,
                        status: 'open'
                    }},
                    {scenarioSketch: {
                        author: 'aher',
                        name: 'second sketch',
                        description: 'Die Design Dom�ne wird hier nahtlos in die Docu-Dom�ne integriert. Die Issues sind �ber ein Issue Tab aufrufbar.',
                        id: 54,
                        dateModified: '2015-04-21T09:26:48+00:00',
                        stepCount: 2,
                        status: 'open'
                    }},
                    {scenarioSketch: {
                        author: 'rbru',
                        name: 'third sketch',
                        description: 'Die Issues k�nnen hier als Button oben rechts aufgerufen werden.',
                        id: 55,
                        dateModified: '2015-04-21T09:26:48+00:00',
                        stepCount: 1,
                        status: 'open'
                    }}
                ];

                $scope.issue = {
                    author: 'mwit',
                    description: 'Weit hinten, hinter den Wortbergen, fern der L�nder Vokalien und Konsonantien leben die Blindtexte.',
                    id: 52,
                    status: 'open',
                    finalProposal: '',
                    scenarioSketches: scenarioSketches
                };
                $scope.scenarioSketches = $scope.issue.scenarioSketches;
            }
        );
        //$scope.propertiesToShow = Config.scenarioPropertiesInOverview();
    }

    /*function loadScenariosAndIssue(selected) {
     var issueName = $routeParams.issueName;
     ScenarioResource.get(
     {
     branchName: selected.branch,
     buildName: selected.build,
     issueName: issueName
     },
     function onSuccess(result) {
     $scope.issue = result.issue;
     $scope.scenarios = result.scenarios;
     $scope.issueInformationTree = createissueInformationTree($scope.issue);
     $scope.metadataTree = transformMetadataToTreeArray($scope.issue.details);
     $scope.hasAnyLabels =  $scope.issue.labels && $scope.issue.labels.labels.length !== 0;
     }
     );

     $scope.propertiesToShow = Config.scenarioPropertiesInOverview();
     }*/

    // todo
    $scope.goToSketchStep = function (sketchStepIndex) {
        $location.path('/sketchstep/' + $routeParams.issueId + '/' + $scope.scenarioSketchId + '/' + sketchStepIndex);
    };

    /*$scope.goToScenario = function (issueName, scenarioName) {
     $location.path('/scenario/' + issueName + '/' + scenarioName);
     };

     $scope.onNavigatorTableHit = function (scenario) {
     $scope.goToScenario($routeParams.issueName, scenario.scenario.name);
     };

     $scope.goToFirstStep = function (issueName, scenarioName) {
     var selected = SelectedBranchAndBuild.selected();

     //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
     ScenarioResource.get(
     {
     branchName: selected.branch,
     buildName: selected.build,
     issueName: issueName,
     scenarioName: scenarioName
     },
     function onSuccess(scenarioResult) {
     $location.path('/step/' + encodeURIComponent(issueName) + '/' + encodeURIComponent(scenarioName) + '/' + encodeURIComponent(scenarioResult.pagesAndSteps[0].page.name) + '/0/0');
     }
     );
     };*/
    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}};

    /*function createIssueInformationTree(issue) {
     var issueInformation = {};
     issueInformation.Status = issue.status;
     return transformMetadataToTree(issueInformation);
     }*/

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    // FIXME this code is duplicated. How can we extract it into a service?
    /*$scope.getLabelStyle = function(labelName) {
     var labelConfig = $scope.labelConfigurations[labelName];
     if(labelConfig) {
     return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
     }
     };*/
});
