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

angular.module('scenarioo.controllers').controller('MainIssuesTabCtrl', function ($scope, $location, $filter, GlobalHotkeysService,BranchesAndBuilds, SelectedBranchAndBuild, IssuesResource, LabelConfigurationsResource) {

    //var transformMetadataToTree = $filter('scMetadataTreeCreator');
    //var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    //var dateTimeFormatter = $filter('scDateTime');
    SelectedBranchAndBuild.callOnSelectionChange(loadIssues);

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function(labelConfiguratins) {
        $scope.labelConfigurations = labelConfiguratins;
    });

    function loadIssues(selected) {

        IssuesResource.query(
            {'branchName': selected.branch},
            function onSuccess(result) {
                $scope.issues = result;

                //var branch = $scope.branchesAndBuilds.selectedBranch.branch;
                /*$scope.branchInformationTree = createBranchInformationTree(branch);
                $scope.buildInformationTree = createBuildInformationTree(build);
                $scope.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                $scope.metadataTreeBuilds = transformMetadataToTreeArray(build.details);*/
            },
            function onError(){
                $scope.issues = [
                    {
                        author:'mzem',
                        name:'first issue',
                        description:'Wie werden die Doku und Design Domäne im Front-end auseinandergehalten?',
                        id:51,
                        dateModiefied:'2015-04-21T09:26:48+00:00',
                        proposalCount:3,
                        status:'open'
                    },
                    {
                        author:'aher',
                        name:'second issue',
                        description:'Lorem ipsum dolor.',
                        id:44,
                        dateModiefied:'2015-04-21T09:26:48+00:00',
                        proposalCount:2,
                        status:'resolved'
                    },
                    {
                        author:'rbru',
                        name:'third issue',
                        description:'Lorem ipsum dolor.',
                        id:45,
                        dateModiefied:'2015-04-21T09:26:48+00:00',
                        proposalCount:1,
                        status:'open'
                    }
                ];

            });
    }

    $scope.goToIssue = function (issueName) {
        $location.path('/issue/' + issueName);
    };

    $scope.onNavigatorTableHit = function(issue) {
        $scope.goToIssue(issue.name);
    };

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'name', reverse: false}};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    /*function createBranchInformationTree(branch) {
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

    // FIXME this code is duplicated. How can we extract it into a service?
    $scope.getLabelStyle = function(labelName) {
        if($scope.labelConfigurations) {
            var labelConfig = $scope.labelConfigurations[labelName];
            if(labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }
    };*/
});