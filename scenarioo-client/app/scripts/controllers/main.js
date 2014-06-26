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

/**
 * The main controller is responsible to control the main tabs (some are static deifned, most are dynamically defined through custom tabs in configuration).
 *
 * The content of the tab is managed in different views and controller that are lazyly loaded through this controller and view (using include URL resolution lazyly).
 */
angular.module('scenarioo.controllers').controller('MainCtrl', function ($scope, $location, SelectedBranchAndBuild, Config, BranchesAndBuilds) {

    function defineInitialStaticTabs() {
        $scope.tabs = [
            {
                tabId: 'usecases',
                title: 'Use Cases',
                contentViewUrl: 'views/mainUseCasesTab.html',
                active: true
            }
        ];
    }

    function defineCustomTabsFromConfig(config) {
         angular.forEach(config.customObjectTabs, function (customTab) {
             $scope.tabs[$scope.tabs.length] = {
                tabId: customTab.id,
                title: customTab.tabTitle,
                column: customTab.customObjectDetailColumns,
                contentViewUrl: 'views/mainCustomTab.html',
                active: false
            }
        });
    }

    // Load configuration and trigger definition of tabs from config.
    $scope.$on(Config.CONFIG_LOADED_EVENT, function () {
        var config = Config.getRawConfigDataCopy();
        defineInitialStaticTabs();
        defineCustomTabsFromConfig(config);
        $scope.selectTabFromUrl();
    });
    Config.load();

    function loadBuilds() {
        BranchesAndBuilds.getBranchesAndBuilds().then(function onSuccess(branchesAndBuilds) {
            $scope.branchesAndBuilds = branchesAndBuilds;
        }, function onFailure(error) {
            console.log(error);
        });
    }
    SelectedBranchAndBuild.callOnSelectionChange(loadBuilds);

    /**
     * Only return the URL for the tab content view as soon as the is is active, such that the content only gets lazyly loaded.
     */
    $scope.getLazyTabContentViewUrl = function (tab) {
        if (tab.active) {
            return tab.contentViewUrl;
        }
        else {
            return null;
        }
    }

    $scope.setSelectedTabInUrl = function (tabId) {
        $location.search('tab', tabId);
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


});