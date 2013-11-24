'use strict';

describe('Filter :: ObjectToTree', function () {

    var DATA_EMPTY_NODE = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            },
            {
                nodeLabel: 'empty',
                childNodes: []
            }
        ]
    };
    var DATA_EMPTY_NODE_OPTIMIZED = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    var DATA_DETAILS = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'details',
                childNodes: [
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Donald Duck'
                    }
                ]
            }
        ]
    };
    var DATA_DETAILS_OPTIMIZED = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'name',
                nodeValue: 'Donald Duck'
            }
        ]
    };

    beforeEach(module('scenarioo.filters'));

    var scTreeDataOptimizer;
    beforeEach(inject(function ($filter) {
        scTreeDataOptimizer = $filter('scTreeDataOptimizer');
    }));

    it('removes empty nodes"', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_NODE)).toEqual(DATA_EMPTY_NODE_OPTIMIZED);
    });

    it('pulls children of details nodes one level up"', function () {
        expect(scTreeDataOptimizer(DATA_DETAILS)).toEqual(DATA_DETAILS_OPTIMIZED);
    });

});
