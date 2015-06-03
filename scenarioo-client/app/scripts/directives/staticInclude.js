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

/**
 * Includes a template without creating a new scope like ng-include does.
 *
 * Code from Stackoverflow answer http://stackoverflow.com/a/17340138/581553
 */
angular.module('scenarioo.directives').directive('scStaticInclude', function ($http, $templateCache, $compile) {
    return function (scope, element, attributes) {
        var templatePath = attributes.scStaticInclude;

        $http.get(templatePath, {cache: $templateCache}).success(function (response) {
            element.html(response);
            $compile(element.contents())(scope);
        });
    };
});
