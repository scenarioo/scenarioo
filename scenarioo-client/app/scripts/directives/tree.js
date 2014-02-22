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

angular.module('scenarioo.directives').directive('scTree', function ($sce) {

    function createTreeHtml(data) {
        if (!angular.isObject(data) || (angular.isObject(data) && angular.isArray(data))) {
            return 'no data to display';
        }
        return getRootNodeHtml(data);
    }

    function getRootNodeHtml(rootNode) {
        var html = '';
        if (angular.isDefined(rootNode.nodeLabel)) {
            html += '<ul><li>';
        }
        html += getNodeHtml(rootNode);
        if (angular.isDefined(rootNode.nodeLabel)) {
            html += '</li></ul>';
        }
        return html;
    }

    function getNodeHtml(node) {
        var html = '';
        if (angular.isDefined(node.nodeLabel)) {
            html = getNodeTitleHtml(node);
        }
        if (angular.isDefined(node.childNodes)) {
            html += getChildNodesHtml(node.childNodes);
        }

        return html;
    }

    function getNodeTitleHtml(data) {
        return '<span class="sc-node-label">' + data.nodeLabel + '</span>' + getNodeValueHtml(data.nodeValue);
    }

    function getNodeValueHtml(nodeValue) {
        if (angular.isUndefined(nodeValue)) {
            return '';
        }
        return '<span class="sc-node-label">: </span><span class="sc-node-value">' + nodeValue + '</span>';
    }

    function getChildNodesHtml(childNodes) {
        if (angular.isUndefined(childNodes) || !angular.isArray(childNodes) || childNodes.length === 0) {
            return '';
        }
        var html = '<ul>';
        angular.forEach(childNodes, function (value) {
            html += '<li>' + getNodeHtml(value) + '</li>';
        });
        html += '</ul>';
        return html;
    }

    return {
        restrict: 'E',
        scope: {data: '=data'},
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