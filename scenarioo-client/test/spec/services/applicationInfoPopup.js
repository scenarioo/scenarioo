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

describe('Service: ScApplicationInfoPopup', function () {

    beforeEach(module('scenarioo.services'));

    beforeEach(module(function ($provide) {
        $provide.value('$modal', {
                open: function () {
                }
            }
        );
        $provide.value('ApplicationInfoCtrl', {});
    }));

    var ScApplicationInfoPopup, localStorageService, $modal;
    beforeEach(inject(function (_ScApplicationInfoPopup_, _localStorageService_, _$modal_) {
        ScApplicationInfoPopup = _ScApplicationInfoPopup_;
        localStorageService = _localStorageService_;
        $modal = _$modal_;
    }));

    it('shows the application info popup on first visit of the app', function () {
        localStorageService.clearAll();
        spyOn($modal, 'open');

        ScApplicationInfoPopup.showApplicationInfoPopupIfRequired();

        expect($modal.open).toHaveBeenCalled();
    });

    it('does not show the application info popup when the user returns to the app', function () {
        localStorageService.add(ScApplicationInfoPopup.PREVIOUSLY_VISITED_COOKIE_NAME, true);

        spyOn($modal, 'open');

        ScApplicationInfoPopup.showApplicationInfoPopupIfRequired();

        expect($modal.open).not.toHaveBeenCalled();
    });

    it('shows opens a modal dialog', function () {
        spyOn($modal, 'open');

        ScApplicationInfoPopup.showApplicationInfoPopup();

        expect($modal.open).toHaveBeenCalled();
    });

});

describe('Controller: ApplicationInfoCtrl', function () {

    beforeEach(module('scenarioo.controllers'));

    var ApplicationInfoCtrl,
        $scope,
        Config;

    beforeEach(inject(function ($controller, $rootScope, ConfigMock) {
        Config = ConfigMock;
        $scope = $rootScope.$new();
        ApplicationInfoCtrl = $controller('ApplicationInfoCtrl', {
            $scope: $scope,
            Config: ConfigMock,
            tabValue: null,
            $modalInstance: null
        });
    }));

    it('should update applicationInformation if it changes in Config', function () {

        expect($scope.applicationInformation).toBeUndefined();

        Config.setApplicationInformation('abc');

        $scope.$digest();

        /**
         * $scope.applicationInfo is $sce wrapped!
         */
        expect($scope.applicationInformation.$$unwrapTrustedValue()).toBe('abc');
    });

});
