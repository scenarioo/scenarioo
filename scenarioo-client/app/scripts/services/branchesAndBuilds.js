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