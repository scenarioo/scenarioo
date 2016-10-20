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

angular.module('scenarioo.controllers').controller('GeneralSettingsController', GeneralSettingsController);

function GeneralSettingsController($scope, BranchesResource, ConfigService, SearchEngineStatusService) {

    var vm = this;
    vm.branches = [];
    vm.configuration = {};
    vm.configuredBranch = {};
    vm.successfullyUpdatedConfiguration = false;
    vm.searchEngineStatus = null;
    vm.resetConfiguration = resetConfiguration;
    vm.updateConfiguration = updateConfiguration;

    activate();

    function activate() {
        BranchesResource.query({}, function (branches) {
            vm.branches = branches;
            calculateConfiguredBranch();
        });

        ConfigService.load();
        loadSearchEngineStatus();
    }

    $scope.$on(ConfigService.CONFIG_LOADED_EVENT, function () {
        vm.configuration = ConfigService.getRawConfigDataCopy();
        calculateConfiguredBranch();
    });

    function calculateConfiguredBranch() {
        if (angular.isUndefined(vm.branches) || angular.isUndefined(vm.configuration)) {
            return;
        }

        for (var index = 0; index < vm.branches.length; index++) {
            if (vm.branches[index].branch.name === vm.configuration.defaultBranchName) {
                vm.configuredBranch = vm.branches[index];
            }
        }
    }

    function loadSearchEngineStatus () {
        SearchEngineStatusService.isSearchEngineRunning().then(function(result) {
            vm.searchEngineStatus = result;
        });
    }

    function resetConfiguration() {
        vm.configuration = ConfigService.getRawConfigDataCopy();
        calculateConfiguredBranch();
    }

    function updateConfiguration() {
        vm.successfullyUpdatedConfiguration = false;

        ConfigService.updateConfiguration(vm.configuration, function () {
            vm.successfullyUpdatedConfiguration = true;
        });
    }

}
