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

angular.module('scenarioo.filters').filter('scHumanReadable', function() {
    return function(input) {
        let text = input;
        if (text && text.length > 0) {
            // First Char
            text = text.charAt(0).toUpperCase() + text.substr(1);
            // Underline
            text = text.replace(/([_])/g, ' ');

            // Camel Case
            // example 1: ThisIsSomeText
            let regex = /([a-z])([A-Z])/g;
            let replaceFn: any = function(s, group0, group1) {
                return group0 + ' ' + group1;
            };
            // example 2: ABadExample
            text = text.replace(regex, replaceFn);
            regex = /([A-Z])([A-Z])([a-z])/g;
            replaceFn = function(s, group0, group1, group2) {
                return group0 + ' ' + group1 + group2;
            };
            text = text.replace(regex, replaceFn);
        }
        return text;
    };
});
