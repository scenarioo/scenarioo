/**
 * Transforms the metadata structure into a tree structure that can be used by our tree directive.
 */
angular.module('scenarioo.services').service('MetadataTransformer', function() {

    function doTransformToTree(metadata) {
        if(angular.isUndefined(metadata)) {
            return undefined;
        }

        return transformNode(metadata, 'root');
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
                nodeLabel: 'Item',
                childNodes: createObjectChildNodes(element)
            });
        });

        return childNodes;
    }

    return {
        transformToTree: doTransformToTree
    }
});
