'use strict';

describe('Filter :: ObjectToTree', function () {

    var DATA = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'myKey',
                nodeValue: 'myValue'
            },
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

    var DATA_TRANSFOREMD = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'myKey',
                nodeValue: 'myValue'
            },
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    beforeEach(module('scenarioo.filters'));

    var scTreeDataOptimizer;
    beforeEach(inject(function ($filter) {
        scTreeDataOptimizer = $filter('scTreeDataOptimizer');
    }));

    it('removes empty nodes"', function () {
        expect(scTreeDataOptimizer(DATA)).toEqual(DATA_TRANSFOREMD);
    });

});
