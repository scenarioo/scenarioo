'use strict';

describe('Filter', function () {
    var filter;

    // load module
    beforeEach(module('scenarioo.filters'));
    beforeEach(inject(function($filter) {
        filter = $filter;
    }));

    it('Should create a human readable string', function() {
        var result = filter('scHumanReadable')("ThisIsSomeCamelCaseString");

        expect(result).toEqual("This Is Some Camel Case String");
    });

    it('Should start with a capital letter', function() {
        var result = filter('scHumanReadable')("someStringStartingSmall");

        expect(result).toEqual("Some String Starting Small");
    });

    it('Should accept special characters', function() {
        var result = filter('scHumanReadable')("thisIsSomeCamel-Case&/%String");

        expect(result).toContain(" Some Camel-Case");
        expect(result).toContain("&/%");
        expect(result).toContain("String");
    });

    it('Should replace underline with blanks', function() {
        var result = filter('scHumanReadable')("This_may_Also_be_Acceptable");

        expect(result).toEqual("This may Also be Acceptable");
    });
});
