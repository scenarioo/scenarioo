'use strict';

angular.module('ngUSDClientApp.controllers').controller('NavigationCtrl', function (CONFIG_LOADED_EVENT, $scope, $location, $cookieStore, BranchesAndBuilds, BranchService, AdminService, Config, $rootScope) {

    /**
     * is set to true while server is updating it's docu
     */
    $scope.aggregationInProgress = false;

    $scope.setBranch = function (branch) {
        $scope.selectedBranch = branch;
        // TODO:  maybe set build to default
        $cookieStore.remove(Config.BUILD_URL_PARAMETER);
        $location.search(Config.BRANCH_URL_PARAMETER, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.selectedBuild = build;
        $location.search(Config.BUILD_URL_PARAMETER, build.build.name);
    };

    $scope.$watch(function() {
        return Config.selectedBuildAndBranch();
    }, function() {
        loadBranchesAndBuilds();
    }, true);

    function loadBranchesAndBuilds () {
        $scope.branchesAndBuilds = BranchesAndBuilds.getBranchesAndBuilds();
    };

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