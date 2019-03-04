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

describe('ApplicationInfoPopupService', () => {

    beforeEach(angular.mock.module('scenarioo.services'));

    beforeEach(angular.mock.module($provide => {
        $provide.value('$uibModal', {
                open: () => {
                }
            }
        );
        $provide.value('ApplicationInfoController', {});
    }));

    let ApplicationInfoPopupService, LocalStorageService, $uibModal;
    const dummyPromise = {
        result: {
            finally: () => {
            }
        }
    };
    beforeEach(inject((_ApplicationInfoPopupService_, _LocalStorageService_, _$uibModal_) => {
        ApplicationInfoPopupService = _ApplicationInfoPopupService_;
        LocalStorageService = _LocalStorageService_;
        $uibModal = _$uibModal_;
    }));

    it('shows the application info popup on first visit of the app', () => {
        LocalStorageService.clearAll();

        spyOn($uibModal, 'open').and.returnValue(dummyPromise);

        ApplicationInfoPopupService.showApplicationInfoPopupIfRequired();

        expect($uibModal.open).toHaveBeenCalled();
    });

    it('does not show the application info popup when the user returns to the app', () => {
        LocalStorageService.clearAll();
        LocalStorageService.set(ApplicationInfoPopupService.PREVIOUSLY_VISITED_COOKIE_NAME, true);

        spyOn($uibModal, 'open').and.returnValue(dummyPromise);

        ApplicationInfoPopupService.showApplicationInfoPopupIfRequired();

        expect($uibModal.open).not.toHaveBeenCalled();
    });

    it('shows opens a modal dialog', () => {
        spyOn($uibModal, 'open').and.returnValue(dummyPromise);

        ApplicationInfoPopupService.showApplicationInfoPopup();

        expect($uibModal.open).toHaveBeenCalled();
    });

});
