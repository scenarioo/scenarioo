'use strict';

describe('Service :: MetadataTransformer', function () {

    var MetadataTransformer;

    var DATA_1 = {};
    var DATA_1_TRANSFOREMD = { nodeLabel : 'root', childNodes : [  ] };

    var DATA_2 = {
        myKey : "myValue",
        keyTwo : "valueTwo"
    };
    var DATA_2_TRANSFOREMD = {
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
        nodeLabel: 'root',
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
                                nodeLabel: 'Item',
                                childNodes: [
                                    {
                                        nodeLabel: 'val',
                                        nodeValue: '23123'
                                    }
                                ]
                            },
                            {
                                nodeLabel: 'Item',
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

    beforeEach(module('scenarioo.services'));

    beforeEach(inject(function(_MetadataTransformer_) {
        MetadataTransformer = _MetadataTransformer_;
    }));

    it('creates empty tree from undefined input', function() {
        var tree = MetadataTransformer.transformToTree(undefined);
        expect(tree).toBeUndefined();
    });

    it('transforms DATA_1', function() {
        var tree = MetadataTransformer.transformToTree(DATA_1);
        expect(tree).toEqual(DATA_1_TRANSFOREMD);
    });

    it('transforms DATA_2', function() {
        var tree = MetadataTransformer.transformToTree(DATA_2);
        expect(tree).toEqual(DATA_2_TRANSFOREMD);
    });

    it('transforms DATA_3', function() {
        var tree = MetadataTransformer.transformToTree(DATA_3);
        expect(tree).toEqual(DATA_3_TRANSFORMED);
    });

});