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

angular.module('scenarioo.controllers').controller('MainCtrl', function ($scope, $location, SelectedBranchAndBuild, UseCasesResource, BranchesAndBuilds, ObjectsForTypeResource) {

    SelectedBranchAndBuild.callOnSelectionChange(loadUseCases);

    function loadUseCases(selected) {

        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(result) {
                $scope.useCases = result;
            });

        BranchesAndBuilds.getBranchesAndBuilds(
            function onSuccess(branchesAndBuilds) {
                $scope.branchesAndBuilds = branchesAndBuilds;
            }
        );

        // TODO: rename method or split it.
        ObjectsForTypeResource.query({'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(result) {
                $scope.objectDescriptions = result;
            }
        );

    }

    $scope.goToUseCase = function (useCaseName) {
        $location.path('/usecase/' + useCaseName);
    };

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'useCase.name', reverse: false}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    $scope.toggleFilter = function () {
        $scope.table.filtering = !$scope.table.filtering;
        if (!$scope.table.filtering) {
            // Removes filter values when filter is switched off
            var searchTerm = $scope.table.search.searchTerm;
            $scope.table.search = { searchTerm: searchTerm };
        }
    };

    $scope.genericObjectsTypes = {selected: null};
    $scope.genericObjectsTypeFilter = function (genericObjectTabs) {
        if ($scope.genericObjectsTypes.selected) {
            if ($scope.genericObjectsTypes.selected.objectType === genericObjectTabs.type) {
                return true;
            }
        } else {
            return true;
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

});