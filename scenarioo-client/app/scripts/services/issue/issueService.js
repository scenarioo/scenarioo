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


angular.module('scenarioo.services').factory('Issue', function ($rootScope, $routeParams, SelectedBranchAndBuild, IssueResource) {

    var ISSUE_LOADED_EVENT = 'issueLoaded';

    var issueData = {};

    function doLoad() {

        IssueResource.query(
            {'branchName': $routeParams.branch},
            function onSuccess(result) {
                issueData = result;
                $rootScope.$broadcast(ISSUE_LOADED_EVENT);
            },
            function onError() {

            });
    }


    return {
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

        updateIssue: function (issue, successCallback) {
            issue.$save(function (updatedIssue) {
                if (successCallback) {
                    successCallback(updatedIssue);
                }
            });
        }
    };
});
