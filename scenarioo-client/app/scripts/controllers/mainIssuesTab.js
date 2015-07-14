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


angular.module('scenarioo.controllers').controller('MainIssuesTabCtrl', function ($scope, $location, $filter, GlobalHotkeysService, BranchesAndBuilds,
                                                                                  SelectedBranchAndBuild, IssuesResource, LabelConfigurationsResource, Issues) {

    //var dateTimeFormatter = $filter('scDateTime');
    SelectedBranchAndBuild.callOnSelectionChange(Issues.load);

    Issues.load();

    $scope.$on(Issues.ISSUES_LOADED_EVENT, function () {
        $scope.issues = Issues.getRawIssuesDataCopy();
    });

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function (labelConfigurations) {
        $scope.labelConfigurations = labelConfigurations;
    });

    $scope.goToIssue = function (issueId) {
        $location.path('/issue/' + issueId);
    };

    $scope.goToSketchStep = function (issue, sketchStepId) {
        sketchStepId = sketchStepId || 1;
        $location.path('/sketchstep/' + issue.id + '/' + issue.firstScenarioSketchId + '/' + sketchStepId);
    };

    /*
      This never seems to get used...
     */
    $scope.onNavigatorTableHit = function (issue) {
        $scope.goToSketchStep(issue);
    };

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'name', reverse: false}};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

});
