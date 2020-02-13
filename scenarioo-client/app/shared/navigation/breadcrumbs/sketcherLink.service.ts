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

declare var angular: angular.IAngularStatic;

/**
 * Contains the information about whether to show the sketcher link in the navigation. The link can either be
 * displayed as "Create sketch" or "Edit sketch".
 */
angular.module('scenarioo.services').factory('SketcherLinkService', SketcherLinkServiceImpl);

function SketcherLinkServiceImpl() {

    const service = this;
    service.showCreateOrEditSketchLink = false;

    return {

        showCreateOrEditSketchLinkInBreadcrumbs(createOrEditSketchLinkTitle, linkClickedAction) {
            service.showCreateOrEditSketchLink = true;
            service.createOrEditSketchLinkTitle = createOrEditSketchLinkTitle;
            service.linkClickedAction = linkClickedAction;
        },

        hideCreateOrEditSketchLinkInBreadcrumbs() {
            service.showCreateOrEditSketchLink = false;
            service.createOrEditSketchLinkTitle = undefined;
            service.linkClickedAction = undefined;
        },

        isShowCreateOrEditSketchLink() {
            return service.showCreateOrEditSketchLink;
        },

        getCreateOrEditSketchLinkTitle() {
            return service.createOrEditSketchLinkTitle;
        },

        executeLinkClickedAction() {
            service.linkClickedAction();
        },
    };
}

export class SketcherLinkService {
    showCreateOrEditSketchLinkInBreadcrumbs() {}

    hideCreateOrEditSketchLinkInBreadcrumbs() {}

    isShowCreateOrEditSketchLink() {}

    getCreateOrEditSketchLinkTitle() {}

    executeLinkClickedAction() {}
}
