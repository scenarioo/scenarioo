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
angular.module('scenarioo.filters').filter('scBranchOrderBy', ['ConfigService', function(ConfigService) { return scBranchOrderByFilter(ConfigService);}]);

function scBranchOrderByFilter(ConfigService) {


    /**
     * comparator function that will order given branch resource objects as follows:
     * 1. branches that are marked as "alias" before others
     * 2. then by the configuration value "branchSelectionListOrder"
     *
     * @param {object} branchA
     * @param {object} branchB
     * @returns {number}
     */
    function branchComparator(branchA, branchB) {

        if (branchA.alias === true && branchB.alias === false) {
            return -1;
        }

        if (branchA.alias === false && branchB.alias === true) {
            return 1;
        }

        var order = ConfigService.branchSelectionListOrder();
        switch(order) {
            case 'name-descending':
                return orderByNameDescending(branchA, branchB);
            case 'last-build-date-descending':
                return orderByLastBuildDateDescending(branchA, branchB);
            case 'name-ascending':  //also the default behavior
            default:
                return orderByNameAscending(branchA, branchB);
        }
    }

    function orderByNameAscending(branchA, branchB) {

        // both are an alias or none is an alias -> use alphabetical ordering
        var branchAName = branchA.branch.name.toLowerCase();
        var branchBName = branchB.branch.name.toLowerCase();

        return compare(branchAName, branchBName);
    }

    function orderByNameDescending(branchA, branchB) {
        return orderByNameAscending(branchA, branchB) * -1;
    }

    function orderByLastBuildDateDescending(branchA, branchB) {

        if (branchA.alias === true) {
            return orderByNameAscending(branchA, branchB);
        }

        // none is an alias -> order by build date DESC
        var dateA = (branchA.builds.length != 0 ? branchA.builds[0].build.date : 0);
        var dateB = (branchB.builds.length != 0 ? branchB.builds[0].build.date : 0);

        return compare(dateA, dateB) * -1;
    }

    function compare(o1, o2) {
        if (o1 > o2) {
            return 1;
        } else if (o1 < o2) {
            return -1;
        } else {
            return 0;
        }
    }

    return function (input) {

        if (!angular.isArray(input)) {
            return input;
        }

        input.sort(branchComparator);

        return input;
    };

}
