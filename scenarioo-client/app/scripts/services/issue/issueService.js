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

angular.module('scenarioo.services').factory('Issue', function ($rootScope, $routeParams, SelectedBranchAndBuild, IssueResource, $log) {

    var ISSUE_LOADED_EVENT = 'issueLoaded';

    var issueData = {};

    function doLoad(issueId) {

        IssueResource.query(
            {
                'branchName': $routeParams.branch,
                'issueId': issueId
            },
            function onSuccess(result) {
                $rootScope.$broadcast(ISSUE_LOADED_EVENT, result);
            });
    }


    return {
        ISSUE_LOADED_EVENT: ISSUE_LOADED_EVENT,

        getRawIssuesDataCopy: function () {
            return angular.copy(issueData);
        },

        load: function (issueId) {
            doLoad(issueId);
        },

        saveIssue: function (issue, successCallback, errorCallback) {
            issue.$save(function (updatedIssue) {
                if (successCallback) {
                    successCallback(updatedIssue);
                }
            },
            function (error) {
                $log.error(error);
                if (errorCallback) {
                    errorCallback('Issue could not be saved');
                }
            });
        },

        deleteSketcherData: function (issueId) {
            IssueResource.delete(
            {
                'issueId': issueId
            },
            function onSuccess() {
                $log.log('sketcher data rolled back');
            },
            function onError(error) {
                $log.warn('sketcher data NOT rolled back');
                $log.error(error);
            });
        }
    };
});
