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

angular.module('scenarioo.controllers').controller('ComparisonDetailsController', ComparisonDetailsController);

function ComparisonDetailsController($uibModalInstance, ComparisonStatusMapperService, ComparisonLogResource, comparison) {

    var vm = this;
    vm.comparison = comparison;
    vm.getStyleClassForComparisonStatus = ComparisonStatusMapperService.getStyleClassForComparisonStatus;
    vm.cancel = cancel;

    activate();

    function activate() {
        ComparisonLogResource.get({
            branchName: comparison.baseBuild.branchName,
            buildName: comparison.baseBuild.buildName,
            comparisonName: comparison.name
        }, function(response) {
            vm.log = response.content;
        });
    }

    function cancel() {
        $uibModalInstance.dismiss('cancel');
    }

}


