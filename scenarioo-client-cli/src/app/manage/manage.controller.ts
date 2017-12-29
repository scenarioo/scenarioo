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

angular.module('scenarioo.controllers').controller('ManageController', ManageController);

function ManageController($location) {
    var vm = this;
    vm.tabs = [
        {
            index: 0,
            tabId: 'builds',
            title: 'Builds',
            contentViewUrl: 'manage/buildImport/buildsList.html'
        },
        {
            index: 1,
            tabId: 'configuration',
            title: 'General Settings',
            contentViewUrl: 'manage/generalSettings/generalSettings.html'
        },
        {
            index: 2,
            tabId: 'branchAliases',
            title: 'Branch Aliases',
            contentViewUrl: 'manage/branchAliases/branchAliases.html'
        },
        {
            index: 3,
            tabId: 'labelConfigurations',
            title: 'Label Colors',
            contentViewUrl: 'manage/labelColors/labelColors.html'
        }
    ];
    vm.activeIndex = 0;
    vm.getLazyTabContentViewUrl = getLazyTabContentViewUrl;
    vm.setSelectedTabInUrl = setSelectedTabInUrl;

    activate();

    function activate() {
        selectTabFromUrl();
    }

    function getLazyTabContentViewUrl(tabIndex) {
        // Only return the tab src as soon as tab is active
        return vm.activeIndex === tabIndex ? vm.tabs[tabIndex].contentViewUrl : null;
    }

    function setSelectedTabInUrl(tabIndex) {
        var tabId = vm.tabs[tabIndex].tabId;
        if ($location.search().tab !== tabId) {
            $location.search('tab', tabId);
        }
    }

    function selectTabFromUrl() {
        var params = $location.search();
        var selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
            angular.forEach(vm.tabs, function (tab, index) {
                if (tab.tabId === selectedTabId) {
                    vm.activeIndex = index;
                }
            });
        }
    }
}
