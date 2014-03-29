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

angular.module('scenarioo.services').factory('ScApplicationInfoPopup', function (localStorageService, $modal) {

    var PREVIOUSLY_VISITED_COOKIE_NAME = 'scenariooPreviouslyVisited';
    var modalIsCurrentlyOpen = false;

    function showApplicationInfoPopupIfRequired() {
        if (userVisitsAppForTheFirstTime() === true) {
            showApplicationInfoPopup();
        }

        function userVisitsAppForTheFirstTime() {
            if (localStorageService.get(PREVIOUSLY_VISITED_COOKIE_NAME)) {
                return false;
            }
            localStorageService.set(PREVIOUSLY_VISITED_COOKIE_NAME, true);
            return true;
        }
    }

    function showApplicationInfoPopup() {
        if (modalIsCurrentlyOpen === true) {
            return;
        }

        modalIsCurrentlyOpen = true;
        var modalInstance = $modal.open({
            templateUrl: 'views/applicationInfoPopup.html',
            controller: 'ApplicationInfoCtrl',
            windowClass: 'modal-small about-popup',
            backdropFade: true
        });

        modalInstance.result['finally'](function () {
            modalIsCurrentlyOpen = false;
        });
    }

    return {
        PREVIOUSLY_VISITED_COOKIE_NAME: PREVIOUSLY_VISITED_COOKIE_NAME,

        showApplicationInfoPopupIfRequired: showApplicationInfoPopupIfRequired,

        showApplicationInfoPopup: showApplicationInfoPopup
    };

}).controller('ApplicationInfoCtrl', function ($scope, $modalInstance, Config, $sce) {
    $scope.$watch(function () {
        return Config.applicationInformation();
    }, function (applicationInformation) {
        $scope.applicationInformation = $sce.trustAsHtml(applicationInformation);
    });

    $scope.closeInfoModal = function () {
        $modalInstance.dismiss('cancel');
    };
});
