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
 * Contains the information about whether to show the sketcher link in the navigation. The link can either be
 * displayed as "Create sketch" or "Edit sketch".
 */
angular.module('scenarioo.services').factory('SketcherLinkService', SketcherLinkService);

function SketcherLinkService() {

    var service = this;
    service.showCreateOrEditSketchLink = false;
    service.createOrEditSketchLinkTitle;
    service.linkClickedAction;

    return {

        showCreateOrEditSketchLinkInBreadcrumbs: function(createOrEditSketchLinkTitle, linkClickedAction) {
            service.showCreateOrEditSketchLink = true;
            service.createOrEditSketchLinkTitle = createOrEditSketchLinkTitle;
            service.linkClickedAction = linkClickedAction;
        },

        hideCreateOrEditSketchLinkInBreadcrumbs: function() {
            service.showCreateOrEditSketchLink = false;
            service.createOrEditSketchLinkTitle = undefined;
            service.linkClickedAction = undefined;
        },

        isShowCreateOrEditSketchLink: function() {
            return service.showCreateOrEditSketchLink;
        },

        getCreateOrEditSketchLinkTitle: function() {
            return service.createOrEditSketchLinkTitle;
        },

        executeLinkClickedAction: function() {
            service.linkClickedAction();
        }

    };

};
