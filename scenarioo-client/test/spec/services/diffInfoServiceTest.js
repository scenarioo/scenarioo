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

describe('Service :: DiffInfoService', function () {

    var ADDED_NAME = 'added';
    var REMOVED_NAME = 'removed';
    var CHANGED_NAME = 'changed';
    var UNCHANGED_NAME = 'unchanged';

    beforeEach(angular.mock.module('scenarioo.services'));


    it('should return elements with diff infos enriched', inject(function (DiffInfoService) {
        var elements = getDummyElements([ADDED_NAME, CHANGED_NAME, UNCHANGED_NAME]);
        var removedElements = getDummyElements([REMOVED_NAME]);
        var diffInfos = getDummyDiffInfos([CHANGED_NAME, UNCHANGED_NAME]);

        var elementsWithDiffInfo = DiffInfoService.getElementsWithDiffInfos(elements, removedElements, diffInfos);

        expect(elementsWithDiffInfo.length).toBe(4);
        assertElement(elementsWithDiffInfo[0], ADDED_NAME, true, false);
        assertElement(elementsWithDiffInfo[1], CHANGED_NAME, false, false);
        assertElement(elementsWithDiffInfo[2], UNCHANGED_NAME, false, false);
        assertElement(elementsWithDiffInfo[3], REMOVED_NAME, false, true);
    }));

    function assertElement(element, expectedName, expectedIsAdded, expectedIsRemoved){
        expect(element.name).toBe(expectedName);
        expect(element.diffInfo.isAdded).toBe(expectedIsAdded);
        expect(element.diffInfo.isRemoved).toBe(expectedIsRemoved);
    }

    function getDummyDiffInfos(elemNames) {
        var diffInfos = {};

        angular.forEach(elemNames, function(elemName) {
            diffInfos[elemName] = getDummyDiffInfo(elemName);
        });

        return diffInfos;
    }

    function getDummyDiffInfo(name){
        return {
            name: name,
            changeRate: 10,
            added: 10,
            changed: 10,
            removed: 10
        }
    }

    function getDummyElements(elemNames){
        var elements = [];

        angular.forEach(elemNames, function(elemName) {
            elements.push(getDummyElement(elemName));
        });

        return elements;
    }

    function getDummyElement(name){
        return {
            name: name
        }
    }
});
