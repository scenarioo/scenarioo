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

angular.module('scenarioo.controllers').controller('ManageCtrl', function ($scope, $location) {

    $scope.tabs = [
        {
            tabId: 'builds',
            title: 'Builds',
            contentViewUrl: 'views/manage/buildsList.html'
        },
        {
            tabId: 'configuration',
            title: 'Configuration',
            contentViewUrl: 'views/manage/config.html'
        },
        {
            tabId: 'branchAliases',
            title: 'Branch Aliases',
            contentViewUrl: 'views/manage/branchAliases.html'
        },
        {
            tabId: 'labelConfigurations',
            title: 'Label Configurations',
            contentViewUrl: 'views/manage/labelConfigurations.html'
        }
    ];

    $scope.getLazyTabContentViewUrl = function (tabId) {
        // Only return the tab src as soon as tab is active
        var url = null;
        angular.forEach($scope.tabs, function (tab) {
            if (tab.tabId === tabId && tab.active === true) {
                url =  tab.contentViewUrl;
            }
        });
        return url;
    };

    $scope.setSelectedTabInUrl = function (tabId) {
        angular.forEach($scope.tabs, function (tab) {
            if (tab.tabId === tabId && tab.active === true && $location.search().tab !== tab.tabId) {
                $location.search('tab', tab.tabId);
            }
        });
    };

    $scope.selectTabFromUrl = function () {
        var params = $location.search();
        var selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
            angular.forEach($scope.tabs, function (tab) {
                if (tab.tabId === selectedTabId) {
                    tab.active = true;
                }
            });
        }
    };
    $scope.selectTabFromUrl();
});
