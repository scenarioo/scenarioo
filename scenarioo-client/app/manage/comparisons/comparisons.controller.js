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

angular.module('scenarioo.controllers').controller('ComparisonsController', ComparisonsController);

function ComparisonsController($scope, ComparisonsResource) {

    var vm = this;

    vm.comparisons = [];
    vm.table = {search: {searchTerm: ''}, sort: {column: 'date', reverse: true}, filtering: false};
    $scope.table = vm.table; // expose "table" onto controller scope. is used at the moment by "sortableColumn" directive.

    var styleClassesForComparisonStatus = {
        'SUCCESS': 'label-success',
        'FAILED': 'label-danger',
        'UNPROCESSED': 'label-default',
        'QUEUED_FOR_PROCESSING': 'label-info',
        'PROCESSING': 'label-primary'
    };
    vm.resetSearchField = resetSearchField;
    vm.getStyleClassForComparisonStatus = getStyleClassForComparisonStatus;

    activate();

    function activate() {
        ComparisonsResource.query({}, function(comparisons) {
            vm.comparisons = comparisons;
        });
    }

    function resetSearchField() {
        vm.table.search = {searchTerm: ''};
    }

    function getStyleClassForComparisonStatus(status) {
        var styleClassFromMapping = styleClassesForComparisonStatus[status];
        if (angular.isUndefined(styleClassFromMapping)) {
            return 'label-warning';
        } else {
            return styleClassFromMapping;
        }
    }
}


