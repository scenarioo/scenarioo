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

import {SharePageService} from '../../../../../app/shared/navigation/sharePage/sharePage.service';

describe('SharePageService', () => {

    let sharePageService: SharePageService;
    const URL = 'http://www.scenarioo.org';

    beforeEach(() => {
        sharePageService = new SharePageService();
    });

    it('is initialized with undefined values by default', () => {
        expectBothUrlsAreUndefined();
    });

    it('stores the page Url', () => {
        sharePageService.setPageUrl(URL);
        expect(sharePageService.getPageUrl()).toBe(URL);
    });

    it('stores the image Url', () => {
        sharePageService.setImageUrl(URL);
        expect(sharePageService.getImageUrl()).toBe(URL);
    });

    it('sets both URLs to undefined when the invalidateUrl method is called', () => {
        sharePageService.setPageUrl(URL);
        sharePageService.setImageUrl(URL);

        sharePageService.invalidateUrls();

        expectBothUrlsAreUndefined();
    });

    function expectBothUrlsAreUndefined() {
        expect(sharePageService.getPageUrl()).toBeUndefined();
        expect(sharePageService.getImageUrl()).toBeUndefined();
    }

});
