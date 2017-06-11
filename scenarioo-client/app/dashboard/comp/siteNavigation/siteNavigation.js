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
angular.module('scenarioo.services').service('SiteNavigationLinkService', function () {
    var service = this;
    service.links = [
        {
            name: 'feature',
            short: 'Feature',
            template: 'dashboard/featureView.html'
        },{
            name: 'dashboard',
            short: 'Map',
            template: 'dashboard/dashboard.html'
        },{
            name: 'detailNav',
            short: 'Docs',
            template: 'dashboard/documentationView.html'
        },{
            name: 'testScenarios',
            short: 'Scenarios',
            template: 'dashboard/scenarioView.html'
        },
    ];
});

angular.module('scenarioo').component('siteNavigation', {
    templateUrl: 'dashboard/comp/siteNavigation/siteNavigation.html',
    controllerAs: 'siteNaviagtion',
    bindings: {
        current: '@'
    },
    controller: function ($location, LocalStorageNameService, SiteNavigationLinkService) {
        var siteNaviagtion = this;

        siteNaviagtion.links = SiteNavigationLinkService.links;

        siteNaviagtion.setView = function (viewName) {
            localStorage.setItem(LocalStorageNameService.LATEST_VIEW_NAME, viewName);
            $location.path('/' + viewName + '/');
        };
    }
});
