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
angular
    .module('scenarioo.directives')
    .directive('scStepNotFoundDiv', stepNotFoundDiv);

function stepNotFoundDiv() {

    return {
        restrict: 'E',
        scope: {
        },
        template: require('./stepNotFound.html'),
        controller: StepNotFoundController,
        controllerAs: 'stepNotFoundDiv'
    };

    function StepNotFoundController($scope, $location,$element) {
        let expressionStepNotFound = $element.attr("stepNotFound");
        $scope.$parent.$watch(expressionStepNotFound, newVal => $scope.stepNotFound = newVal);
        let expressionHttpResponse = $element.attr("httpResponse");
        $scope.$parent.$watch(expressionHttpResponse, newVal => $scope.httpResponse = newVal);

        $scope.getCurrentUrl = () => $location.absUrl();

    }

}
