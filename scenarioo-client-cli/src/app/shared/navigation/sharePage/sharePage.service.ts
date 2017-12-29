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

/**
 * Manages the information for sharing the currently displayed page.
 */
angular.module('scenarioo.services').factory('SharePageService', function () {

    var shareLinks;

    function initialize() {
        shareLinks = {
            pageUrl: undefined,
            imageUrl: undefined
        };
    }

    initialize();

    // Service interface
    return {
        /**
         * Make sure to call resetAll when the URL you set here becomes invalid.
         */
        setPageUrl: function (pageUrl) {
            shareLinks.pageUrl = pageUrl;
        },

        /**
         * Make sure to call resetAll when the URL you set here becomes invalid.
         */
        setImageUrl: function (imageUrl) {
            shareLinks.imageUrl = imageUrl;
        },

        getPageUrl: function () {
            return shareLinks.pageUrl;
        },

        getImageUrl: function () {
            return shareLinks.imageUrl;
        },

        /**
         * This method has to be called when the controller that changed some of the values is destroyed.
         */
        invalidateUrls: function () {
            initialize();
        }
    };

});
