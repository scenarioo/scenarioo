'use strict';

describe('Filter: scenarioFilter,', function () {

    beforeEach(module('scenarioo.filters'));

    var MODEL = [
        {
            test: 'test',
            something: 'else',
            odd: {
                weird: 'things'
            }
        },
        {
            test: 'hi',
            something: 'there',
            odd: {
                weird: 'things'
            }
        }
    ];

    var MODEL_FILTERED = [
        {
            test: 'test',
            something: 'else',
            odd: {
                weird: 'things'
            }
        }
    ];

    var MODEL_EMPTY = [];

    var scFilter;
    beforeEach(inject(function ($filter) {
        scFilter = $filter('scFilterArray');
    }));

    describe('when there is search text is empty,', function () {
        it('should return the original model', function () {
            expect(scFilter(MODEL, '')).toEqual(MODEL);
        });
    });

    describe('when search text is a simple string,', function () {
        it('should filter the model', function () {
            expect(scFilter(MODEL, 'test')).toEqual(MODEL_FILTERED);
        });
    });

    describe('when search text consists of multiple words,', function () {
        it('keeps all objects in the model, that contain both words', function () {
            expect(scFilter(MODEL, 'test else')).toEqual(MODEL_FILTERED);
        });

        it('filters out all objects that miss one or more words,', function () {
            expect(scFilter(MODEL, 'test weirdthing')).toEqual(MODEL_EMPTY);
        });

        // Fix
        // it('filters out all objects that miss one or more words,', function () {
        //     expect(scFilter(MODEL, 'test things')).toEqual(MODEL_FILTERED);
        // });
    });

});
