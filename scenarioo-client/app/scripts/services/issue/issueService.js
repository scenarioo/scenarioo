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

angular.module('scenarioo.services').factory('Issue', function ($rootScope, $routeParams, SelectedBranchAndBuild, IssueResource) {

    var ISSUE_LOADED_EVENT = 'issueLoaded';

    var issueData = {};

    function doLoad() {

        IssueResource.query(
            // why does "selected" call not work?
            //{'branchName': selected.branch},
            {'branchName': $routeParams.branch},
            function onSuccess(result) {
                issueData = result;
                $rootScope.$broadcast(ISSUE_LOADED_EVENT);

                //var branch = $scope.branchesAndBuilds.selectedBranch.branch;
                /*$scope.branchInformationTree = createBranchInformationTree(branch);
                 $scope.buildInformationTree = createBuildInformationTree(build);
                 $scope.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                 $scope.metadataTreeBuilds = transformMetadataToTreeArray(build.details);*/
            },
            function onError() {

            });
    }


    var serviceInstance = {
        ISSUE_LOADED_EVENT: ISSUE_LOADED_EVENT,

        getRawIssuesDataCopy: function () {
            return angular.copy(issueData);
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

        updateIssue: function (issue, successCallback) {
            issue.$update(function (updatedIssue) {
                if (successCallback) {
                    successCallback(updatedIssue);
                }
            });
        },

        saveIssue: function (newIssue, successCallback) {
            newIssue.$save(function (savedIssue) {
                if (successCallback) {
                    //doLoad();
                    successCallback(savedIssue);
                }
            });
        }
    };

    return serviceInstance;

});