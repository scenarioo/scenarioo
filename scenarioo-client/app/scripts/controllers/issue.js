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

angular.module('scenarioo.controllers').controller('IssueCtrl', function ($scope, $q, $filter, $routeParams,
                                                                            $location, IssueResource, Config, SelectedBranchAndBuild,
                                                                            LabelConfigurationsResource) {

    //var transformMetadataToTree = $filter('scMetadataTreeCreator');
    //var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    SelectedBranchAndBuild.callOnSelectionChange(loadIssue);
    console.log("test");

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function(labelConfigurations) {
        $scope.labelConfigurations = labelConfigurations;
    });

    function loadIssue(selected) {
        var issueId = $routeParams.issueName;
        IssueResource.get(
            {
                branchName: selected.branch,
                issueId: issueId
            },
            function onSuccess(result) {
                $scope.issue = result.issue;
                $scope.scenarioSketches = null;
                $scope.issueName = result.issue.name;
                //$scope.scenarios = result.scenarios;
                //$scope.issueInformationTree = createissueInformationTree($scope.issue);
                //$scope.metadataTree = transformMetadataToTreeArray($scope.issue.details);
                //$scope.hasAnyLabels =  $scope.issue.labels && $scope.issue.labels.labels.length !== 0;
            },
            function onError(){

                var scenarioSketches = [
                    {scenarioSketch: {
                        author:'mzem',
                        name:'first sketch',
                        description:'Die Domänen Design und Docu sind in diesem Sketch stark getrennt. Im Header werden Tabs eingesetzt.',
                        id:53,
                        dateModified:'2015-04-21T09:26:48+00:00',
                        stepCount:3,
                        status:'open'
                    }},
                    {scenarioSketch: {
                        author:'aher',
                        name:'second sketch',
                        description:'Die Design Domäne wird hier nahtlos in die Docu-Domäne integriert. Die Issues sind über ein Issue Tab aufrufbar.',
                        id:54,
                        dateModified:'2015-04-21T09:26:48+00:00',
                        stepCount:2,
                        status:'open'
                    }},
                    {scenarioSketch: {
                        author:'rbru',
                        name:'third sketch',
                        description:'Die Issues können hier als Button oben rechts aufgerufen werden.',
                        id:55,
                        dateModified:'2015-04-21T09:26:48+00:00',
                        stepCount:1,
                        status:'open'
                    }}
                ];

                $scope.issue = {
                    author: 'mwit',
                    description:'Weit hinten, hinter den Wortbergen, fern der Länder Vokalien und Konsonantien leben die Blindtexte.',
                    id:52,
                    status:'open',
                    finalProposal:'',
                    scenarioSketches:scenarioSketches
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
    $scope.goToScenarioSketch = function (issueId, scenarioSketchName) {
        $location.path('/' + issueId + '/scenariosketch/' + scenarioSketchName);
        //$location.path('#');
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
