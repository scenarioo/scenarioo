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

// From http://stackoverflow.com/a/18609594/581553

angular.module('scenarioo.directives').factory('RecursionHelper', ['$compile', function($compile){
    var RecursionHelper = {
        compile: function(element){
            var contents = element.contents().remove();
            var compiledContents;
            return function(scope, element){
                if(!compiledContents){
                    compiledContents = $compile(contents);
                }
                compiledContents(scope, function(clone){
                    element.append(clone);
                });
            };
        }
    };

    return RecursionHelper;
}]);