'use strict';

angular.module('scenarioo.controllers').controller('NavigationCtrl', function ($scope, $location, $cookieStore, BranchesAndBuilds, AdminService, $rootScope, SelectedBranchAndBuild) {

    /**
     * is set to true while server is updating it's docu
     */
    $scope.aggregationInProgress = false;

    SelectedBranchAndBuild.callOnSelectionChange(loadBranchesAndBuilds);

    function loadBranchesAndBuilds () {
        $scope.branchesAndBuilds = BranchesAndBuilds.getBranchesAndBuilds();
    }

    $scope.setBranch = function (branch) {
        $scope.selectedBranch = branch;
        // TODO:  maybe set build to default
        $cookieStore.remove(SelectedBranchAndBuild.BUILD_KEY);
        $location.search(SelectedBranchAndBuild.BRANCH_KEY, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.selectedBuild = build;
        $location.search(SelectedBranchAndBuild.BUILD_KEY, build.build.name);
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

    // TODO: Use $modal from angular-bootstrap, as soon as it works with bootstrap 3
    $rootScope.infoModal = {showing: (isAFirstTimeUser() ? 'block' : 'none'), tab: null};

    $rootScope.openInfoModal = function (tabValue) {
        $rootScope.infoModal = {showing: true, tab: tabValue, style: {display: 'block'}};
    };

    $rootScope.closeInfoModal = function () {
        $rootScope.infoModal = {showing: false, style: {display: 'none'}};
    };

});