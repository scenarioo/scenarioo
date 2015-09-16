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
            },
            function onError(){

            });
    }


    return {
        ISSUES_LOADED_EVENT: ISSUES_LOADED_EVENT,

        getRawIssuesDataCopy: function () {
            return angular.copy(issuesData);
        },

        /**
         * Will fire event 'issuesLoaded'
         */
        load: function () {
            doLoad();
        }

        /*saveIssue: function (newIssue, successCallback) {
            newIssue.$save(function (savedIssue) {
                if (successCallback) {
                    successCallback(savedIssue);
                }
            });
        }*/
    };
});
