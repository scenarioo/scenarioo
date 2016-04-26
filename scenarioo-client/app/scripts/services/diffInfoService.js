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
 *  Service functionality to prepare diff info data to display.
 */
angular.module('scenarioo.services').factory('DiffInfoService', function () {

    function getElementsWithDiffInfos(elements, removedElements, diffInfos) {
        var elementsWithDiffInfo = [];

        angular.forEach(elements, function(element){
            var scenarioDiffInfo = diffInfos[element.name];
            if(scenarioDiffInfo) {
                element.diffInfo = scenarioDiffInfo;
                element.diffInfo.isAdded = false;
                element.diffInfo.isRemoved = false;
            } else {
                element.diffInfo = {};
                element.diffInfo.isAdded = true;
                element.diffInfo.isRemoved = false;
            }
            elementsWithDiffInfo.push(element);
        });

        angular.forEach(removedElements, function(removedElement){
            removedElement.diffInfo = {};
            removedElement.diffInfo.isAdded = false;
            removedElement.diffInfo.isRemoved = true;
            elementsWithDiffInfo.push(removedElement);
        });

        return elementsWithDiffInfo;
    }

    return {
        getElementsWithDiffInfos: getElementsWithDiffInfos
    };


});
