'use strict';

describe('Filter :: scTreeDataCreator', function () {

    var DATA_1 = {};
    var DATA_1_TRANSFOREMD = { nodeLabel : '', childNodes : [  ] };

    var DATA_2 = {
        myKey : "myValue",
        keyTwo : "valueTwo"
    };
    var DATA_2_TRANSFOREMD = {
        nodeLabel : '',
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

    var DATA_3 = {
        "details": {
            "start": "12312",
            "end": [
                { "val" : "23123" },
                { "val2" : "111" }
            ]
        },
        "name" : "page_load",
        "type" : "statistics"
    };
    var DATA_3_TRANSFORMED = {
        nodeLabel: '',
        childNodes: [
            {
                nodeLabel: 'details',
                childNodes: [
                    {
                        nodeLabel: 'start',
                        nodeValue: '12312'
                    },
                    {
                        nodeLabel: 'end',
                        childNodes: [
                            {
                                nodeLabel: '',
                                childNodes: [
                                    {
                                        nodeLabel: 'val',
                                        nodeValue: '23123'
                                    }
                                ]
                            },
                            {
                                nodeLabel: '',
                                childNodes: [
                                    {
                                        nodeLabel: 'val2',
                                        nodeValue: '111'
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                nodeLabel: 'name',
                nodeValue: 'page_load'
            },
            {
                nodeLabel: 'type',
                nodeValue: 'statistics'
            }
        ]
    };

    beforeEach(module('scenarioo.filters'));

    var scTreeDataCreator;
    beforeEach(inject(function ($filter) {
        scTreeDataCreator = $filter('scTreeDataCreator');
    }));

    it('creates empty tree from undefined input', function() {
        var tree = scTreeDataCreator(undefined);
        expect(tree).toBeUndefined();
    });

    it('transforms DATA_1', function() {
        var tree = scTreeDataCreator(DATA_1);
        expect(tree).toEqual(DATA_1_TRANSFOREMD);
    });

    it('transforms DATA_2', function() {
        var tree = scTreeDataCreator(DATA_2);
        expect(tree).toEqual(DATA_2_TRANSFOREMD);
    });

    it('transforms DATA_3', function() {
        var tree = scTreeDataCreator(DATA_3);
        expect(tree).toEqual(DATA_3_TRANSFORMED);
    });

});