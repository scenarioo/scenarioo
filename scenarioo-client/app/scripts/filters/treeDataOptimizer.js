'use strict';

angular.module('scenarioo.filters').filter('scTreeDataOptimizer', function () {

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

    function removeEmptyNodes(node) {
        if(angular.isUndefined(node.childNodes)) {
            return;
        }

        var modifiedChildNodes = [];

        angular.forEach(node.childNodes, function(childNode) {
            if(hasChildNodes(childNode) || hasNodeValue(childNode)) {
                removeEmptyNodes(childNode);
                modifiedChildNodes.push(childNode);
            }
        });

        node.childNodes = modifiedChildNodes;
    }

    return function (rootNode) {
        removeEmptyNodes(rootNode);
        return rootNode;
    };
});
