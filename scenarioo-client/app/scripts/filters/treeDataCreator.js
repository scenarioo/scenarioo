'use strict';

/**
 * Transforms any nested object and array structure into a tree structure that can be used by our tree directive.
 */
angular.module('scenarioo.filters').filter('scTreeDataCreator', function() {

    function createTreeData(data) {
        if(angular.isUndefined(data)) {
            return undefined;
        }

        return transformNode(data, '');
    }

    function transformNode(node, nodeTitle) {

        var transformedNode = {
            nodeLabel : nodeTitle
        };

        if(angular.isArray(node)) {

        } else if (angular.isObject(node)) {
            transformedNode.childNodes = createObjectChildNodes(node);
        } else if (angular.isString(node)) {
            transformedNode = node;
        }

        return transformedNode;
    }

    function createObjectChildNodes(node) {
        var childNodes = [];

        angular.forEach(node, function(value, key) {
            if (angular.isArray(value)) {
                childNodes.push({
                    nodeLabel: key,
                    childNodes: createArrayChildNodes(value)
                });
            } else if (angular.isObject(value)) {
                childNodes.push({
                    nodeLabel: key,
                    childNodes: createObjectChildNodes(value)
                });
            } else {
                childNodes.push({
                    nodeLabel: key,
                    nodeValue: value
                });
            }
        });

        return childNodes;
    }

    function createArrayChildNodes(array) {
        var childNodes = [];
        angular.forEach(array, function(element) {
            childNodes.push({
                nodeLabel: '',
                childNodes: createObjectChildNodes(element)
            });
        });

        return childNodes;
    }

    return createTreeData;
});
