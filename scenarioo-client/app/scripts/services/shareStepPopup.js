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

angular.module('scenarioo.services').factory('ScShareStepPopup', function (scLocalStorage, $uibModal) {

    // This is required to avoid multiple popups (they could be opened using keyboard shortcuts)
    var modalIsCurrentlyOpen = false;

    function showShareStepPopup() {
        if (modalIsCurrentlyOpen === true) {
            return;
        }

        modalIsCurrentlyOpen = true;

        var modalInstance = $uibModal.open({
            templateUrl: 'views/shareStepPopup.html',
            controller: 'ScShareStepPopupController',
            windowClass: 'modal-small',
            backdropFade: true
        });

        modalInstance.result.finally(function () {
            modalIsCurrentlyOpen = false;
        });
    }

    return {
        showShareStepPopup: showShareStepPopup
    };

}).controller('ScShareStepPopupController', function ($scope, $uibModalInstance, SharePageService, $location) {

    var currentBrowserLocation = $location.absUrl();

    $scope.pageUrl = (function () {
        if (angular.isDefined(SharePageService.getPageUrl())) {
            return SharePageService.getPageUrl();
        } else {
            return currentBrowserLocation;
        }
    }());
    $scope.imageUrl = SharePageService.getImageUrl();

    $scope.eMailSubject = encodeURIComponent('Link to Scenarioo');
    $scope.eMailUrl = encodeURIComponent($scope.pageUrl);

    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };

});
