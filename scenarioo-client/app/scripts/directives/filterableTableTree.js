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

angular.module('scenarioo.directives').directive('scFilterableTableTree', function ($sce) {

    // Need to be clarified: This part will be called multiple times, why?? (One time without any data, second time with data)
    function createTreeHtml(data) {

        if (!angular.isObject(data)) {
            return 'no data to display';
        }
        else if (angular.isObject(data) && angular.isArray(data)) {
            // TODO: Wrong thing, has to be improved
            var tableHtml = '<table border="1"><tr><th>Name</th><th>Type</th><th>Description</th></tr>';
            var parentIndentation = 0;

            angular.forEach(data, function (value, index){
                tableHtml += CreateRow(value);
                parentIndentation += 1;
                tableHtml += getChildNode(value.children, parentIndentation);
            });

            return tableHtml += '</table>';
        }
        return tableHtml;
    }

    function getChildNode(childdata, indentation) {

        if (!angular.isObject(childdata)) {
            return 'no data to display';
        }
        else if (angular.isObject(childdata) && angular.isArray(childdata)) {
            var rowHtml = '';

            angular.forEach(childdata, function (value, index) {
                rowHtml += CreateRow(value);
                indentation += 1;
                rowHtml += getChildNode(value.children);
             })

            return rowHtml;
        }
    }

    function CreateRow(value, indentation){
        var rowHtml = '<tr>';

        if (angular.isDefined(value.item.name)) {
            rowHtml += '<td style="text-indent:10px">' + value.item.name + '</td>';
        }

        if (angular.isDefined(value.item.name)) {
            rowHtml += '<td>' + value.item.type + '</td>';
        }

        if (angular.isDefined(value.details.description)) {
            rowHtml += '<td>' + value.details.description + '</td>';
        }

        return rowHtml += '</tr>';
    }

    function getTreeColumnIndentDiv(element, indentation) {


        return html;
    }

//          var ITEM = 'Item';
//    var CHILDREN = 'children';
//
//    function createTableTreeHtml(data) {
//        var html = '';
//        var html = '<table ng-controller="ScFilterableTableTreeCtrl">';
//
//        // TODO:  table around!
//        // TODO: th for column headers!
//        if (!angular.isObject(data)) {
//            return 'no data to display';
//        }
//        else if (angular.isObject(data) && angular.isArray(data)) {
//
//            angular.forEach(data, function (rootNode) {
//                html += getRootNodeHtml(rootNode,0);
//            });
//            return html;
//        }
//        else {
//            html += getRootNodeHtml(data,0);
//        }
//        html += "</table>";
//        return html;
//    }
//
//    function getRootNodeHtml(rootNode, treeDepth) {
//
//        // Row for current node
//        var html = '<tr ng-click="toggleNodeExpanded(id)">';
//        html += getNodeHtml(node);
//        html += '</tr>';
//
//        // All children (recursively)
//        var isExpanded = angular.isUndefined(angrootNode.expanded) || rootNode.expanded === true;
//        if (isExpanded && angular.isObject(rootNode.children) && angular.isArray(rootNode.children)) {
//            angular.forEach(rootNode.children, function (rootNode) {
//                html += getRootNodeHtml(rootNode, treeDepth + 1);
//            });
//            return html;
//        }
//        return html;
//    }
//
//    function getNodeHtml(node) {
//
//        var html = '<td>';
//        html += getTreeColumnIndentationStartDivs(treeDepth);
//        html += node.item.type;
//        html += ': ';
//        html += node.item.name;
//        html += getTreeColumnIndentationEndDivs(treeDepth);
//        html += '</td>';
//
//        html += '<td>';
//        html += node.details.description;
//        html += '</td>';
//
//    }
//
//    function getTreeColumnIndentationStartDivs(treeDepth) {
//        html='';
//        for (var i=0; i<treeDepth; i++) {
//            html += '<div class="sc-treeTable-node-indentation">';
//        }
//        return html;
//    }
//
//    function getTreeColumnIndentationEndDivs(treeDepth) {
//        html='';
//        for (var i=0; i<treeDepth; i++) {
//            html += '</div>';
//        }
//        return html;
//    }
//
//
//    /**
//     * HTML for a simple value item in the three structure (most trivial node).
//     */
//    function getUsualValueItemNodeHtml(node) {
//        var html = '';
//        if (angular.isDefined(node.nodeLabel)) {
//            html = getNodeTitleHtml(node);
//        }
//        if (angular.isDefined(node.childNodes)) {
//            html += getChildNodesHtml(node.childNodes);
//        }
//        return html;
//    }
//
//
//

    return {
        restrict: 'E',
        scope: {
            data: '=data'
        },
        template: '<div ng-bind-html="treeHtml" class="sc-tree"></div>',
        link: function (scope) {
            scope.$watch('data', function (newData) {
                /* there is no 'ng-bind-html-unsafe' anymore. we use Strict Contextual Escaping, see
                 http://docs.angularjs.org/api/ng/service/$sce for more information
                 */
            scope.treeHtml = $sce.trustAsHtml(createTreeHtml(newData));
            });
        }
    };
});