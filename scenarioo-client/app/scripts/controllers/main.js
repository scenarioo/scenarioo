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

angular.module('scenarioo.controllers').controller('MainCtrl', function ($scope, $location, SelectedBranchAndBuild, BranchesAndBuilds) {

    function loadBuilds() {
        BranchesAndBuilds.getBranchesAndBuilds().then(function onSuccess(branchesAndBuilds) {
            $scope.branchesAndBuilds = branchesAndBuilds;
        }, function onFailure(error) {
            console.log(error);
        });
    }

    SelectedBranchAndBuild.callOnSelectionChange(loadBuilds);

    $scope.tabs = [
        {
            tabId: 'usecases',
            title: 'Use Cases',
            contentViewUrl: 'views/mainUseCasesTab.html',
            active: true
        },
        {
            tabId: 'builds',
            title: 'Builds',
            contentViewUrl: 'views/mainBuildsTab.html'
        }
    ];

    $scope.getLazyTabContentViewUrl = function (tab) {
        // Only return the tab src as soon as tab is active
        if (tab.active) {
            return tab.contentViewUrl;
        }
        else {
            return null;
        }
    };

    $scope.genericObjectTabs = [
        {index: '0', label: 'Object Descriptions', objectTypes: [
            {index: 0, label: 'Business Operations', objectType: 'businessOperation'},
            {index: 1, label: 'Services', objectType: 'service', columns: [
                {key: 'realName', label: 'Real Name'},
                {key: 'eaiName', label: 'Integration Name (EAI)'}
            ]},
            {index: 2, label: 'UI Actions', objectType: 'action'} ,
            {index: 3, label: 'HTTP Requests', objectType: 'httpAction'}
        ]}
        //,{index: '1', label:'Simulation Configs', objectType: 'httpAction'}
    ];

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

    $scope.selectTabFromUrl();


});