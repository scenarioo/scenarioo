'use strict';

angular.module('ngUSDClientApp.services').service('BranchesAndBuilds', function (CONFIG_LOADED_EVENT, $rootScope, Config, BranchService, $q) {

    var branchesAndBuildsData = getPromise($q, function(parameters, successFn, errorFn) {
        var loadedData = {};
        loadedData.applicationInformation = Config.applicationInformation();
        loadedData.branches = BranchService.findAllBranches();
        loadedData.branches.then(function (branches) {
            var configBranchName = Config.selectedBranch();
            for (var index = 0; index < branches.length; index++) {
                if (branches[index].branch.name === configBranchName) {
                    loadedData.selectedBranch = branches[index];
                }
            }

            var configBuildName = Config.selectedBuild();
            var allBuildsOnSelectedBranch = loadedData.selectedBranch.builds;
            for (var index = 0; index < loadedData.selectedBranch.builds.length; index++) {
                if (allBuildsOnSelectedBranch[index].build.name === configBuildName || allBuildsOnSelectedBranch[index].linkName === configBuildName) {
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