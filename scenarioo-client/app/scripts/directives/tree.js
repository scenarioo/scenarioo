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

angular.module('scenarioo.directives').directive('scTree', function (RecursionHelper) {
    return {
        restrict: 'E',
        scope: {data: '='},
        template:
            '<span ng-hide="!data.nodeLabel">' +
                '<span class="sc-node-label">{{data.nodeLabel}}</span><span ng-show="data.nodeValue || data.nodeValue == 0">: </span>' +
                '<span class="sc-node-value">{{data.nodeValue}}</span>' +
            '</span>' +
            '<ul class="sc-tree">' +
                '<li ng-repeat="child in data.childNodes" class="sc-tree">' +
                    '<sc-tree data="child"></sc-tree>' +
                '</li>' +
            '</ul>',
        compile: function(element) {
            return RecursionHelper.compile(element);
        }
    };
});