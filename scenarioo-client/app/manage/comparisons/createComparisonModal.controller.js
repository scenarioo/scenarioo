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

angular.module('scenarioo.controllers').controller('CreateComparisonModalController', CreateComparisonModalController);

function CreateComparisonModalController($uibModalInstance, BranchesAndBuildsService) {

    var vm = this;

    vm.branchesAndBuilds = {};
    vm.comparisonName = '';
    vm.baseBranch = null;
    vm.baseBuild = null;
    vm.comparisonBranch = null;
    vm.comparisonBuild = null;

    vm.setBaseBranch = setBaseBranch;
    vm.setBaseBuild = setBaseBuild;
    vm.setComparisonBranch = setComparisonBranch;
    vm.setComparisonBuild = setComparisonBuild;

    vm.cancel = cancel;
    vm.createComparison = createComparison;

    activate();

    function activate() {
       BranchesAndBuildsService.getBranchesAndBuilds().then(function onSuccess(branchesAndBuilds) {
            vm.branchesAndBuilds = branchesAndBuilds;
            vm.baseBranch = branchesAndBuilds.selectedBranch;
            vm.baseBuild = branchesAndBuilds.selectedBuild;
       });
    }

    function setBaseBranch(branch) {
        vm.baseBranch = branch;
    }

    function setBaseBuild(build) {
        vm.baseBuild = build;
    }

    function setComparisonBranch(branch) {
        vm.comparisonBranch = branch;
    }

    function setComparisonBuild(build) {
        vm.comparisonBuild = build;
    }

    function createComparison() {
        $uibModalInstance.close();
    }

    function cancel() {
        $uibModalInstance.dismiss('cancel');
    }

}


