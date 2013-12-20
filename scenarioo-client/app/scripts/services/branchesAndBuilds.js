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

angular.module('scenarioo.services').service('BranchesAndBuilds', function ($rootScope, Config, BranchesResource, $q, SelectedBranchAndBuild) {

    var branchesAndBuildsData = function(onSuccess) {

        BranchesResource.query({}, findSelectedBranchAndBuild);

        function findSelectedBranchAndBuild(branches) {
            var loadedData = {
                branches: branches
            };

            if(!SelectedBranchAndBuild.isDefined()) {
                return;
            }

            var selected = SelectedBranchAndBuild.selected();

            if(loadedData.branches.length === 0) {
                console.log('Branch list empty!');
                return;
            }

            var index;
            for (index = 0; index < loadedData.branches.length; index++) {
                if (loadedData.branches[index].branch.name === selected.branch) {
                    loadedData.selectedBranch = loadedData.branches[index];
                }
            }

            if(angular.isUndefined(loadedData.selectedBranch)) {
                console.log('Branch ' + selected.branch + ' not found in branch list!');
                return;
            }

            var allBuildsOnSelectedBranch = loadedData.selectedBranch.builds;
            for (index = 0; index < loadedData.selectedBranch.builds.length; index++) {
                if (allBuildsOnSelectedBranch[index].build.name === selected.build || allBuildsOnSelectedBranch[index].linkName === selected.build) {
                    loadedData.selectedBuild = allBuildsOnSelectedBranch[index];
                }
            }

            onSuccess(loadedData);
        }
    };

    return {
        getBranchesAndBuilds : branchesAndBuildsData
    };
});