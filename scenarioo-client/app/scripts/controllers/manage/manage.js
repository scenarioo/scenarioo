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

angular.module('scenarioo.controllers').controller('ManageCtrl', function ($scope, $location) {

    $scope.tabs = [
        {
            index: 0,
            tabId: 'builds',
            title: 'Builds',
            contentViewUrl: 'views/manage/buildsList.html'
        },
        {
            index: 1,
            tabId: 'configuration',
            title: 'General Settings',
            contentViewUrl: 'views/manage/config.html'
        },
        {
            index: 2,
            tabId: 'branchAliases',
            title: 'Branch Aliases',
            contentViewUrl: 'views/manage/branchAliases.html'
        },
        {
            index: 3,
            tabId: 'labelConfigurations',
            title: 'Label Colors',
            contentViewUrl: 'views/manage/labelConfigurations.html'
        }
    ];
    $scope.activeIndex = 0;
    selectTabFromUrl();

    $scope.getLazyTabContentViewUrl = function (tabIndex) {
        // Only return the tab src as soon as tab is active
        return $scope.activeIndex === tabIndex ? $scope.tabs[tabIndex].contentViewUrl : null;
    };

    $scope.setSelectedTabInUrl = function (tabIndex) {
        var tabId = $scope.tabs[tabIndex].tabId;
        if ($location.search().tab !== tabId) {
           $location.search('tab', tabId);
        }
    };

    function selectTabFromUrl() {
        var params = $location.search();
        var selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
            angular.forEach($scope.tabs, function (tab, index) {
                if (tab.tabId === selectedTabId) {
                    $scope.activeIndex = index;
                }
           });
        }
    }

});
