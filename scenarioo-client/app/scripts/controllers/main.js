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
 * The main controller is responsible to control the main tabs (some are static deifned, most are dynamically defined through custom tabs in configuration).
 *
 * The content of the tab is managed in different views and controller that are lazyly loaded through this controller and view (using include URL resolution lazyly).
 */
angular.module('scenarioo.controllers').controller('MainCtrl', MainCtrl);

function MainCtrl($scope, $location, Config) {

    var vm = this;
    vm.tabs = undefined;
    vm.getLazyTabContentViewUrl = getLazyTabContentViewUrl;
    vm.setSelectedTabInUrl = setSelectedTabInUrl;
    vm.tabIndices = {}; // store tabId to index hashMap. our tab-ng-repeat will not work correctly with a object and order it, thus vm.tabs must remain an array
    vm.activeIndex = 0;

    activate();

    function defineInitialStaticTabs() {
        vm.tabs = [
            {
                index: 0,
                tabId: 'usecases',
                title: 'Use Cases',
                contentViewUrl: 'views/mainUseCasesTab.html'
            }
        ];
        vm.tabIndices.usecases = 0;
    }


    function activate() {
        // Load configuration and trigger definition of tabs from config.
        $scope.$on(Config.CONFIG_LOADED_EVENT, function () {
            var config = Config.getRawConfigDataCopy();
            defineInitialStaticTabs();
            defineCustomTabsFromConfig(config);
            defineLastStaticTabs();
            selectTabFromUrl();
        });
        Config.load();

        selectTabFromUrl();
    }

    function defineCustomTabsFromConfig(config) {
        angular.forEach(config.customObjectTabs, function (customTab, index) {
            vm.tabs.push({
                index: index + 1,
                tabId: customTab.id,
                title: customTab.tabTitle,
                column: customTab.customObjectDetailColumns,
                contentViewUrl: 'views/mainCustomTab.html'
            });
            vm.tabIndices[customTab.id] = index + 1;
        });
    }

    function defineLastStaticTabs() {
        var i = vm.tabs.length;
        vm.tabs.push({
            index: i,
            tabId: 'sketches',
            title: 'Sketches',
            contentViewUrl: 'views/mainSketchesTab.html'
        });
        vm.tabIndices.sketches = i;
    }

    /**
     * Only return the URL for the tab content view as soon as the is is active, such that the content only gets lazyly loaded.
     */
    function getLazyTabContentViewUrl(tabIndex) {
        // Only return the tab src when tab is active
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
        if (params && angular.isDefined(params.tab)) {
            var tab = getTabById(params.tab);
            if (tab) {
                vm.activeIndex = tab.index;
            }
        }
    }

    function getTabById(tabId) {
        var index = vm.tabIndices[tabId];
        if (angular.isDefined(index)) {
            return vm.tabs[index];
        } else {
            return undefined;
        }
    }
}
