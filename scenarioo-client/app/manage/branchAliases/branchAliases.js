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

angular.module('scenarioo.controllers').controller('BranchAliasesCtrl', function ($scope, $rootScope, $location, $route, $uibModal, BranchAliasesResource, Config, BranchesResource) {

    loadBranchAliases();

    BranchesResource.query({}, function (branches) {
        var branchesWithoutAliases = [];
        var index;
        for(index = 0; index < branches.length; index++) {
            var branch = branches[index];
            if(!branch.alias) {
                branchesWithoutAliases.push(branch);
            }
        }

        $scope.branches = branchesWithoutAliases;
    });

    $scope.deleteEntry = function (aliasName) {
        if (aliasName !== '') {
            var index;
            for (index = 0; index < $scope.branchAliases.length; index++) {
                var branchAlias = $scope.branchAliases[index];
                if (branchAlias.name === aliasName) {
                    $scope.branchAliases.splice(index, 1);
                    break;
                }
            }
        }
    };

    $scope.aliasNameChanged = function () {
        var aliasName = $scope.branchAliases[$scope.branchAliases.length - 1].name;
        if (aliasName !== '') {
            $scope.branchAliases.push(createEmptyAlias());
        }
    };

    function loadBranchAliases() {
        BranchAliasesResource.query({}, function (branchAliases) {
            branchAliases.push(createEmptyAlias());
            $scope.branchAliases = branchAliases;
        });
    }

    $scope.reset = function () {
        loadBranchAliases();
    };

    $scope.save = function () {
        $scope.uniqueError = false;
        $scope.successfullyUpdatedBranchAliases = false;

        var branchAliasesToSave = [];
        var index;
        for (index = 0; index < $scope.branchAliases.length; index++) {
            var branchAlias = $scope.branchAliases[index];
            if (branchAlias.name !== '') {
                branchAliasesToSave.push(branchAlias);
            }
        }

        if (areBuildAliasesUnique(branchAliasesToSave) === false) {
            $scope.uniqueError = true;
            return;
        }

        BranchAliasesResource.save(branchAliasesToSave, function() {
            $rootScope.$broadcast('branchesUpdated');
        });

        $scope.successfullyUpdatedBranchAliases = true;
    };

    var areBuildAliasesUnique = function (buildAliases) {
        var unique = true;
        var aliasesMap = {};
        angular.forEach(buildAliases, function (buildAlias) {
            if (aliasesMap[buildAlias.name] === undefined) {
                aliasesMap[buildAlias.name] = '';
            } else {
                unique = false;
            }
        });

        return unique;
    };

    function createEmptyAlias() {
        return {
            name: '',
            referencedBranch: '',
            description: ''
        };
    }
});


