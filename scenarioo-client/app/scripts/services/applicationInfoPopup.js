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

angular.module('scenarioo.services').factory('ScApplicationInfoPopup', function (scLocalStorage, $uibModal) {

    var PREVIOUSLY_VISITED_COOKIE_NAME = 'scenariooPreviouslyVisited';

    // This is required to avoid multiple popups (they could be opened using keyboard shortcuts)
    var modalIsCurrentlyOpen = false;

    function showApplicationInfoPopupIfRequired() {
        if (userVisitsAppForTheFirstTime() === true) {
            showApplicationInfoPopup();
        }

        function userVisitsAppForTheFirstTime() {
            if (scLocalStorage.get(PREVIOUSLY_VISITED_COOKIE_NAME)) {
                return false;
            }
            scLocalStorage.set(PREVIOUSLY_VISITED_COOKIE_NAME, true);
            return true;
        }
    }

    function showApplicationInfoPopup() {
        if (modalIsCurrentlyOpen === true) {
            return;
        }

        modalIsCurrentlyOpen = true;
        var modalInstance = $uibModal.open({
            templateUrl: 'views/applicationInfoPopup.html',
            controller: 'ApplicationInfoCtrl',
            windowClass: 'modal-small about-popup',
            backdropFade: true
        });

        modalInstance.result.finally(function () {
            modalIsCurrentlyOpen = false;
        });
    }

    return {
        PREVIOUSLY_VISITED_COOKIE_NAME: PREVIOUSLY_VISITED_COOKIE_NAME,

        showApplicationInfoPopupIfRequired: showApplicationInfoPopupIfRequired,

        showApplicationInfoPopup: showApplicationInfoPopup
    };

}).controller('ApplicationInfoCtrl', function ($scope, $uibModalInstance, Config, $sce, VersionResource) {
    $scope.$watch(function () {
        return Config.applicationInformation();
    }, function (applicationInformation) {
        $scope.applicationInformation = $sce.trustAsHtml(applicationInformation);
    });

    VersionResource.get(
        function onSuccess(result) {
            $scope.version = result;
        }
    );

    $scope.closeInfoModal = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
