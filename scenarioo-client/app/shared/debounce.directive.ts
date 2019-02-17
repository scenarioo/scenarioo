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

/*
 Waits for given milliseconds before entered filter criteria will be applied
 */

angular.module('scenarioo.directives').directive('scDebounce', function($timeout) {
    return {
        restrict: 'A',
        require: 'ngModel',
        priority: 99,
        link(scope, elm, attr, ngModelController: any) {
            if (attr.type === 'radio' || attr.type === 'checkbox') {
                return;
            }

            elm.unbind('input');

            let debounce;
            elm.bind('input', function() {
                $timeout.cancel(debounce);
                debounce = $timeout(function() {
                    scope.$apply(function() {
                        ngModelController.$setViewValue(elm.val());
                    });
                }, attr.ngDebounce || 400);
            });
            elm.bind('blur', function() {
                scope.$apply(function() {
                    ngModelController.$setViewValue(elm.val());
                });
            });
        },
    };
});
