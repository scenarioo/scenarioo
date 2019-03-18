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

angular.module('scenarioo.directives').directive('scBuildSelection', function ($rootScope, $routeParams, $location, $route, $compile,
                                                                               $filter, $sce, $uibModal, BranchesAndBuildsService, RootScopeService) {
    return {
        restrict: 'E',
        replace: true,
        template: require('./buildSelection.html'),
        scope: {
            branchesAndBuilds: '=',
            selectedBranch: '=',
            selectedBuild: '=',
            onBuildChange: '&'
        },
        link: function (scope) {
            scope.getDisplayNameForBuild = getDisplayNameForBuild;
            scope.isBuildAlias = isBuildAlias;
            scope.isLastSuccessfulScenariosBuild = isLastSuccessfulScenariosBuild;
            scope.getStatusStyleClass = getStatusStyleClass;
        }
    };

    function getDisplayNameForBuild(build, returnShortText) {
        return BranchesAndBuildsService.getDisplayNameForBuild(build, returnShortText);
    }

    function isBuildAlias(build) {
        return BranchesAndBuildsService.isBuildAlias(build);
    }

    function isLastSuccessfulScenariosBuild(build) {
        return BranchesAndBuildsService.isLastSuccessfulScenariosBuild(build);
    }

    function getStatusStyleClass(status) {
        return RootScopeService.getStatusStyleClass && RootScopeService.getStatusStyleClass(status);
    }

});
