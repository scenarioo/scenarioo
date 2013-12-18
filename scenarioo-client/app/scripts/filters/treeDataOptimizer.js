/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

angular.module('scenarioo.filters').filter('scTreeDataOptimizer', function ($filter) {

    var scHumanReadable = $filter('scHumanReadable');

    function optimizeChildNodes(node, operation) {
        if(angular.isUndefined(node.childNodes)) {
            return;
        }

        var modifiedChildNodes = [];

        angular.forEach(node.childNodes, function(childNode) {
            operation(childNode, modifiedChildNodes);
        });

        node.childNodes = modifiedChildNodes;

        angular.forEach(node.childNodes, function(childNode) {
            optimizeChildNodes(childNode, operation);
        });
    }

// TODO Decide whether we want do remove empty child nodes or not. Probably not.
//    function removeEmptyChildNodes(childNode, modifiedChildNodes) {
//        if(hasChildNodes(childNode) || hasNodeValue(childNode)) {
//            modifiedChildNodes.push(childNode);
//        }
//    }
//
//    function hasChildNodes(node) {
//        var childNodes = node.childNodes;
//        var childNodesArrayNotEmpty = angular.isArray(childNodes) && childNodes.length > 0;
//        var childNodesPropertyDefined = angular.isDefined(childNodes);
//
//        return childNodesPropertyDefined && childNodesArrayNotEmpty;
//    }
//
//    function hasNodeValue(node) {
//        var value = node.nodeValue;
//        var hasStringValue = angular.isString(value) && value.trim() !== '';
//        var hasOtherValue = angular.isDefined(value) && value !== null;
//        return hasStringValue || hasOtherValue;
//    }

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

    function optimizeNodes(node, operation) {
        operation(node);

        if(angular.isUndefined(node.childNodes)) {
            return;
        }

        angular.forEach(node.childNodes, function(childNode) {
            optimizeNodes(childNode, operation);
        });
    }

    function makeLabelsHumanReadable(node) {
        if(angular.isString(node.nodeLabel)) {
            node.nodeLabel = scHumanReadable(node.nodeLabel);
        }
    }

    function pullUpTypeToReplaceNodeLabel(node) {
        var childNode = getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'type');

        if(angular.isUndefined(childNode)) {
            return;
        }

        node.nodeLabel = childNode.nodeValue;
    }

    function moveChildrenChildNodeBehindOthers(node) {
        var childrenNode = getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'children');

        if(angular.isDefined(childrenNode)) {
            addNodeToChildNodesAfterAllOthers(node, childrenNode);
        }
    }

    function pullUpNameToReplaceEmptyNodeLabel(node) {
        if(angular.isString(node.nodeLabel) && node.nodeLabel !== '') {
            return;
        }

        var childNode = getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'name');

        if(angular.isUndefined(childNode)) {
            return;
        }

        node.nodeLabel = childNode.nodeValue;
    }

    function pullUpNameToReplaceEmptyNodeValue(node) {
        if(angular.isString(node.nodeValue) && node.nodeValue !== '') {
            return;
        }

        var childNode = getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'name');

        if(angular.isUndefined(childNode)) {
            return;
        }

        node.nodeValue = childNode.nodeValue;
    }

    function getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, type) {
        if(!angular.isArray(node.childNodes)) {
            return;
        }

        var modifiedChildNodes = [];
        var nameChildNode;

        for(var i in node.childNodes) {
            var childNode = node.childNodes[i];
            if(childNode.nodeLabel.toLowerCase() === type) {
                nameChildNode = childNode;
            } else {
                modifiedChildNodes.push(childNode);
            }
        }

        node.childNodes = modifiedChildNodes;

        return nameChildNode;
    }

    function addNodeToChildNodesAfterAllOthers(node, childNodeToAdd) {
        if(!angular.isArray(node.childNodes)) {
            node.childNodes = [];
        }
        node.childNodes.push(childNodeToAdd);
    }

    return function (rootNode) {
        // TODO Check with Rolf whether we need to remove empty child nodes
        // optimizeTree(rootNode, removeEmptyChildNodes);
        optimizeChildNodes(rootNode, pullUpChildrenOfDetailsNodes);
        optimizeNodes(rootNode, pullUpTypeToReplaceNodeLabel);
        optimizeNodes(rootNode, moveChildrenChildNodeBehindOthers);

        optimizeNodes(rootNode, makeLabelsHumanReadable);

        // this happens after making the labels human readable,
        // because the name node value could be a technical expression
        optimizeNodes(rootNode, pullUpNameToReplaceEmptyNodeLabel);
        optimizeNodes(rootNode, pullUpNameToReplaceEmptyNodeValue);

        return rootNode;
    };
});
