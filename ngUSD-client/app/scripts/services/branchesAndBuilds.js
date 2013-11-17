'use strict';

angular.module('ngUSDClientApp.services').service('BranchesAndBuilds', function ($rootScope, Config, BranchService, $q, SelectedBranchAndBuild) {

    var branchesAndBuildsData = getPromise($q, function(parameters, successFn, errorFn) {
        var loadedData = {};
        loadedData.applicationInformation = Config.applicationInformation();
        loadedData.branches = BranchService.findAllBranches();
        loadedData.branches.then(function (branches) {
            if(!SelectedBranchAndBuild.isDefined()) {
                return;
            }

            var selected = SelectedBranchAndBuild.selected();

            if(branches.length === 0) {
                console.log("Branch list empty!");
                return;
            }

            for (var index = 0; index < branches.length; index++) {
                if (branches[index].branch.name === selected.branch) {
                    loadedData.selectedBranch = branches[index];
                }
            }

            if(angular.isUndefined(loadedData.selectedBranch)) {
                console.log('Branch ' + selected.branch + ' not found in branch list!');
                return;
            }

            var allBuildsOnSelectedBranch = loadedData.selectedBranch.builds;
            for (var index = 0; index < loadedData.selectedBranch.builds.length; index++) {
                if (allBuildsOnSelectedBranch[index].build.name === selected.build || allBuildsOnSelectedBranch[index].linkName === selected.build) {
                    loadedData.selectedBuild = allBuildsOnSelectedBranch[index];
                }
            }
            successFn(loadedData);
        }, errorFn);
    });


    function getPromise($q, fn) {
        return function (parameters) {
            var deferred = $q.defer();
            fn(parameters, function (result) {
                deferred.resolve(result);
            }, function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
    }

    var serviceInstance = {
        getBranchesAndBuilds : branchesAndBuildsData
    };

    return serviceInstance;
});