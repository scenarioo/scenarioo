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

describe('SharePageService', function () {

    var SharePageService;
    var URL = 'http://www.scenarioo.org';

    beforeEach(angular.mock.module('scenarioo.services'));
    beforeEach(angular.mock.module('scenarioo.filters'));

    beforeEach(inject(function (_SharePageService_) {
        SharePageService = _SharePageService_;
    }));


    it('is initialized with undefined values by default', function () {
        expectBothUrlsAreUndefined();
    });

    it('stores the page Url', function() {
        SharePageService.setPageUrl(URL);
        expect(SharePageService.getPageUrl()).toBe(URL);
    });

    it('stores the image Url', function() {
        SharePageService.setImageUrl(URL);
        expect(SharePageService.getImageUrl()).toBe(URL);
    });

    it('sets both URLs to undefined when the invalidateUrl method is called', function() {
        SharePageService.setPageUrl(URL);
        SharePageService.setImageUrl(URL);

        SharePageService.invalidateUrls();

        expectBothUrlsAreUndefined();
    });

    function expectBothUrlsAreUndefined() {
        expect(SharePageService.getPageUrl()).toBeUndefined();
        expect(SharePageService.getImageUrl()).toBeUndefined();
    }

});
