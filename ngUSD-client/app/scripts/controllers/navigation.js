'use strict';

angular.module('ngUSDClientApp.controllers').controller('NavigationCtrl', function (CONFIG_LOADED_EVENT, $scope, $location, $cookieStore, BranchService, AdminService, Config, $rootScope) {

    /**
     * is set to true while server is updating it's docu
     */
    $scope.aggregationInProgress = false;

    $scope.setBranch = function (branch) {
        $scope.selectedBranch = branch;
        // TODO:  maybe set build to default
        $location.search(Config.BRANCH_URL_PARAMETER, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.selectedBuild = build;
        $location.search(Config.BUILD_URL_PARAMETER, build.build.name);
    };

    $scope.$on(CONFIG_LOADED_EVENT, function () {
        $scope.applicationInformation = Config.applicationInformation();
        $scope.branches = BranchService.findAllBranches();
        $scope.branches.then(function (branches) {
            for (var index = 0; index < branches.length; index++) {
                if (branches[index].branch.name === Config.selectedBranch()) {
                    $scope.selectedBranch = branches[index];
                }
            }

            var configBuildName = Config.selectedBuild();
            var allBuildsOnSelectedBranch = $scope.selectedBranch.builds;
            for (var index = 0; index < $scope.selectedBranch.builds.length; index++) {
                if (allBuildsOnSelectedBranch[index].build.name === configBuildName || allBuildsOnSelectedBranch[index].linkName === configBuildName) {
                    $scope.selectedBuild = allBuildsOnSelectedBranch[index];
                }
            }
        });
    });

    $scope.modalInfoOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-small'
    };

    $scope.updating = false;

    $scope.getDisplayName = function (build) {
        if (angular.isUndefined(build)) {
            return '';
        }

        if (build.build.name !== build.linkName) {
            return build.linkName;
        } else {
            return 'Revision: ' + build.build.revision;
        }
    };

    $scope.aggregate = function () {
        $scope.aggregationInProgress = true;

        var result = AdminService.updateData({});
        result.then(function () {
            $scope.aggregationInProgress = false;
        }, function () {
            $scope.aggregationInProgress = false;
        });
    };

    // TODO:  do not leak to rootScope
    function isAFirstTimeUser() {
        var previouslyVisited = $cookieStore.get('previouslyVisited');
        if (previouslyVisited) {
            return false;
        }
        $cookieStore.put('previouslyVisited', true);
        return true;
    }

    $rootScope.infoModal = {showing: isAFirstTimeUser(), tab: null};

    $rootScope.openInfoModal = function (tabValue) {
        $rootScope.infoModal = {showing: true, tab: tabValue};
    };

    $rootScope.closeInfoModal = function () {
        $rootScope.infoModal = {showing: false};
    };

});