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

angular.module('scenarioo.controllers').controller('NavigationCtrl', function ($scope, $location, $cookieStore, BranchesAndBuilds, BuildImportService, $rootScope, SelectedBranchAndBuild, $modal) {

    /**
     * is set to true while server is updating it's docu
     */
    $scope.aggregationInProgress = false;

    SelectedBranchAndBuild.callOnSelectionChange(loadBranchesAndBuilds);

    function loadBranchesAndBuilds() {
        BranchesAndBuilds.getBranchesAndBuilds(
            function onSuccess(branchesAndBuilds) {
                $scope.branchesAndBuilds = branchesAndBuilds;
            }
        );
    }

    $scope.setBranch = function (branch) {
        $scope.selectedBranch = branch;
        $cookieStore.remove(SelectedBranchAndBuild.BUILD_KEY);
        $location.search(SelectedBranchAndBuild.BRANCH_KEY, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.selectedBuild = build;
        $location.search(SelectedBranchAndBuild.BUILD_KEY, build.linkName);
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

        var result = BuildImportService.updateData({});
        result.then(function () {
            $scope.aggregationInProgress = false;
            $location.path('/').search('tab', 'builds');
        }, function () {
            $scope.aggregationInProgress = false;
        });
    };

    // TODO:  do not leak to rootScope
    $scope.isAFirstTimeUser = function() {
        var previouslyVisited = $cookieStore.get('scenariooFirstTimeUsageApplicationInfoPopupAlreadyVisitedYea');
        if (previouslyVisited) {
            return false;
        }
        $cookieStore.put('scenariooFirstTimeUsageApplicationInfoPopupAlreadyVisitedYea', true);
        return true;
    };

    $rootScope.openInfoModal = function (tabValue) {
        $modal.open({
            templateUrl: 'views/applicationInfoPopup.html',
            controller: ApplicationInfoPopupCtrl,
            windowClass: 'modal-small',
            backdropFade: true,
            resolve: {
                tabValue: function () { return tabValue; }
            }
        });
    };

    $scope.openOnFirstTimeUsage = function() {
        if ($scope.isAFirstTimeUser()===true) {
            $rootScope.openInfoModal();
        }
    };
    $scope.openOnFirstTimeUsage();

});


/** Sub-Controller for Application-Info-Popup **/
var ApplicationInfoPopupCtrl = function($scope, $modalInstance, Config, tabValue) {

    $scope.$watch(function() {
        return Config.applicationInformation();
    }, function(applicationInformation) {
        $scope.applicationInformation = applicationInformation;
    });

    $scope.tabValue = tabValue;

    $scope.closeInfoModal = function () {
        $modalInstance.dismiss('cancel');
    };

};
