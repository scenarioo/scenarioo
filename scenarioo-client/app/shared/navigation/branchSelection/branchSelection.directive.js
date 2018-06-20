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

angular.module('scenarioo.directives').directive('scBranchSelection', function ($routeParams, $location, $route, $compile,
                                                                            $filter, $sce, $uibModal, BranchesAndBuildsService) {
    return {
        restrict: 'E',
        replace: true,
        template: require('./branchSelection.html'),
        scope: {
            branchesAndBuilds: '=',
            selectedBranch: '=',
            onBranchChange: '&'
        },
        link: function (scope) {
            scope.getBranchDisplayName = getBranchDisplayName;
        }
    };

    function getBranchDisplayName(wrappedBranch) {
        return BranchesAndBuildsService.getBranchDisplayName(wrappedBranch);
    }

});
