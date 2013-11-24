'use strict';

angular.module('scenarioo.filters').filter('scTreeDataOptimizer', function () {

    function removeEmptyChildNodes(childNode, modifiedChildNodes) {
        if(hasChildNodes(childNode) || hasNodeValue(childNode)) {
            modifiedChildNodes.push(childNode);
        }
    }

    function hasChildNodes(node) {
        var childNodes = node.childNodes;
        var childNodesArrayNotEmpty = angular.isArray(childNodes) && childNodes.length > 0;
        var childNodesPropertyDefined = angular.isDefined(childNodes);

        return childNodesPropertyDefined && childNodesArrayNotEmpty;
    }

    function hasNodeValue(node) {
        var value = node.nodeValue;
        var hasStringValue = angular.isString(value) && value.trim() !== '';
        var hasOtherValue = angular.isDefined(value) && value !== null;
        return hasStringValue || hasOtherValue;
    }

    function pullUpChildrenOfDetailsNodes(childNode, modifiedChildNodes) {
        if(isDetailsNode(childNode)) {
            angular.forEach(childNode.childNodes, function(grandChildNode) {
                modifiedChildNodes.push(grandChildNode);
            });
        } else {
            modifiedChildNodes.push(childNode);
        }
    }

    function isDetailsNode(node) {
        return angular.isDefined(node.nodeLabel) && node.nodeLabel === 'details';
    }

    function optimizeTree(node, operation) {
        if(angular.isUndefined(node.childNodes)) {
            return;
        }

        var modifiedChildNodes = [];

        angular.forEach(node.childNodes, function(childNode) {
            operation(childNode, modifiedChildNodes);
        });

        node.childNodes = modifiedChildNodes;

        angular.forEach(node.childNodes, function(childNode) {
            optimizeTree(childNode, operation);
        });
    }

    return function (rootNode) {
        optimizeTree(rootNode, removeEmptyChildNodes);
        optimizeTree(rootNode, pullUpChildrenOfDetailsNodes);
        return rootNode;
    };
});
