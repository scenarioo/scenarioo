'use strict';

describe('Filter :: scTreeDataOptimizer', function () {

    var DATA_EMPTY_NODE = {
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
        childNodes : [
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    var DATA_DETAILS = {
        childNodes : [
            {
                nodeLabel: 'details',
                childNodes: [
                    {
                        nodeLabel: 'hero',
                        nodeValue: 'Donald Duck'
                    }
                ]
            }
        ]
    };
    var DATA_DETAILS_OPTIMIZED = {
        childNodes : [
            {
                nodeLabel: 'Hero',
                nodeValue: 'Donald Duck'
            }
        ]
    };

    var DATA_HUMAN_READABLE = {
        nodeLabel : 'veryImportantNode',
        childNodes : [
            {
                nodeLabel: 'firstName',
                nodeValue: 'donaldDuck'
            }
        ]
    };
    var DATA_HUMAN_READABLE_OPTIMIZED = {
        nodeLabel : 'Very Important Node',
        childNodes : [
            {
                nodeLabel: 'First Name',
                nodeValue: 'donaldDuck'
            }
        ]
    };

    var DATA_EMPTY_LABEL = {
        nodeLabel : '',
        childNodes : [
            {
                nodeLabel: '',
                nodeValue: 'Julia',
                childNodes : [
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Romeo\'s love'
                    }
                ]
            }
        ]
    };
    var DATA_EMPTY_LABEL_OPTIMIZED = {
        nodeLabel : '',
        childNodes : [
            {
                nodeLabel: 'Romeo\'s love',
                nodeValue: 'Julia',
                childNodes : []
            }
        ]
    };

    var DATA_EMPTY_VALUE = {
        nodeLabel : '',
        childNodes : [
            {
                nodeLabel: 'Julia',
                nodeValue: '',
                childNodes : [
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Romeo\'s love'
                    }
                ]
            }
        ]
    };
    var DATA_EMPTY_VALUE_OPTIMIZED = {
        nodeLabel : '',
        childNodes : [
            {
                nodeLabel: 'Julia',
                nodeValue: 'Romeo\'s love',
                childNodes : []
            }
        ]
    };

    var DATA_TYPE = {
        nodeLabel : 'something',
        nodeValue : 'special',
        childNodes : [
            {
                nodeLabel: 'type',
                nodeValue: 'configModule'
            }
        ]
    };
    var DATA_TYPE_OPTIMIZED = {
        nodeLabel : 'Config Module',
        nodeValue : 'special',
        childNodes : []
    };

    beforeEach(module('scenarioo.filters'));

    var scTreeDataOptimizer;
    beforeEach(inject(function (_$filter_) {
        scTreeDataOptimizer = _$filter_('scTreeDataOptimizer', {$filter: _$filter_});
    }));

    // TODO Check with Rolf whether we need to remove empty child nodes
    xit('removes empty nodes"', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_NODE)).toEqual(DATA_EMPTY_NODE_OPTIMIZED);
    });

    it('pulls children of details nodes one level up', function () {
        expect(scTreeDataOptimizer(DATA_DETAILS)).toEqual(DATA_DETAILS_OPTIMIZED);
    });

    it('makes node labels human readable but does not touch node values', function () {
        expect(scTreeDataOptimizer(DATA_HUMAN_READABLE)).toEqual(DATA_HUMAN_READABLE_OPTIMIZED);
    });

    it('uses the name child node to replace empty node label', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_LABEL)).toEqual(DATA_EMPTY_LABEL_OPTIMIZED);
    });

    it('uses the name child node to replace empty node values', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_VALUE)).toEqual(DATA_EMPTY_VALUE_OPTIMIZED);
    });

    it('always uses the type child node to replace the parent node label', function () {
        expect(scTreeDataOptimizer(DATA_TYPE)).toEqual(DATA_TYPE_OPTIMIZED);
    });

});
