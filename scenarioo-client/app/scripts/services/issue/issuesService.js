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

angular.module('scenarioo.services').factory('Issues', function ($rootScope, $routeParams, SelectedBranchAndBuild, IssuesResource) {

    var ISSUES_LOADED_EVENT = 'issuesLoaded';

    var issuesData = {};

    function doLoad() {

        IssuesResource.query(
            // why does "selected" call not work?
            //{'branchName': selected.branch},
            {'branchName': $routeParams.branch},
            function onSuccess(result) {
                issuesData = result;
                $rootScope.$broadcast(ISSUES_LOADED_EVENT);

                //var branch = $scope.branchesAndBuilds.selectedBranch.branch;
                /*$scope.branchInformationTree = createBranchInformationTree(branch);
                 $scope.buildInformationTree = createBuildInformationTree(build);
                 $scope.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                 $scope.metadataTreeBuilds = transformMetadataToTreeArray(build.details);*/
            },
            function onError(){
                issuesData = [
                    {
                        author:'mzem',
                        name:'first issue (client side dummy data)',
                        description:'Wie werden die Doku und Design Domäne im Front-end auseinandergehalten?',
                        id:51,
                        dateModified:'2015-04-21T09:26:48+00:00',
                        proposalCount:3,
                        status:'open'
                    },
                    {
                        author:'aher',
                        name:'second issue',
                        description:'Lorem ipsum dolor.',
                        id:44,
                        dateModified:'2015-04-21T09:26:48+00:00',
                        proposalCount:2,
                        status:'resolved'
                    },
                    {
                        author:'rbru',
                        name:'third issue',
                        description:'Lorem ipsum dolor.',
                        id:45,
                        dateModified:'2015-04-21T09:26:48+00:00',
                        proposalCount:1,
                        status:'open'
                    }
                ];

            });
    }


    var serviceInstance = {
        ISSUES_LOADED_EVENT: ISSUES_LOADED_EVENT,

        getRawIssuesDataCopy: function () {
            return angular.copy(issuesData);
        },

        /**
         * Will fire event 'issuesLoaded'
         */
        load: function () {
            doLoad();
        },

        isLoaded: function () {
            //return angular.isDefined(configData.defaultBuildName);
        },

        saveIssue: function (newIssue, successCallback) {
            newIssue.$save(function () {
                if (successCallback) {
                    doLoad();
                    successCallback();
                }
            });
        }
    };

    return serviceInstance;

});