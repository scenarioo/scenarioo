'use strict';

angular.module('scenarioo.filters').filter('scFilterArray', function () {

    function contains(haystack, needle) {
        return haystack.indexOf(needle) > -1;
    }

    function objectContainsAllSearchElements(object, filterString) {
        var searchElements = filterString.split(' ');

        for(var i in searchElements) {
            if(!objectContainsString(object, searchElements[i])) {
                return false;
            }
        }
        return true;
    }

    function objectContainsString(object, string) {
        var returnTrue = false;

        angular.forEach(object, function (property) {
            if(!returnTrue) {
                if (typeof property === 'string') {
                    if (contains(property, string)) {
                        returnTrue = true;
                    }
                } else if (typeof value === 'object') {
                    returnTrue = objectContainsString(property, string);
                }
            }
        });

        return returnTrue;
    }

    return function (array, filterString) {

        if (!angular.isArray(array)) {
            return array;
        }
        if(angular.isUndefined(filterString) || typeof filterString !== 'string') {
            return array;
        }

        var filteredModel = [];

        angular.forEach(array, function (arrayElement) {
            if (typeof arrayElement === 'object') {
                if(objectContainsAllSearchElements(arrayElement, filterString)) {
                    filteredModel.push(arrayElement);
                }
            }
        });

        return filteredModel;
    };

});
