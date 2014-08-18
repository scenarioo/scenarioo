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

angular.module('scenarioo.services').factory('TreeNode', function () {
    var iconType = {
        COLLAPSED: 'images/collapsed.png',
        EXPANDED: 'images/expanded.png',
        NONE: ''
    };

    function setIconName(node) {
        if (node.children.length > 0) {
            node.icon = node.isCollapsed ? iconType.EXPANDED : iconType.COLLAPSED;
        }
        else {
            node.icon = iconType.NONE;
        }
    }

    function setNodeProperties(node, isCollapsed, isVisible) {
        node.isCollapsed = isCollapsed;
        node.isVisible = isVisible;
        setIconName(node);
    }

    return {

        /*
            Invert the expand or collapse status and corresponding value
         */
        expandAndCollapseTree: function (treemodel, $scope) {
            var isCollapsed = $scope.toggleLabel === 'expand' ? false : true;

            angular.forEach(treemodel, function (node) {
                setNodeProperties(node, isCollapsed, !isCollapsed);

                if (node.level === 0) {
                    node.isVisible = true;
                }
            });
            $scope.toggleLabel = isCollapsed ? $scope.toggleLabel = 'expand' : $scope.toggleLabel = 'collapse';
        },

        /*
            Sets collapsing for root node and first child node
         */
        setCollapsingAndVisibility: function (newNode, $scope) {
            if (newNode.level === 0) {
                setNodeProperties(newNode, $scope.rootIsCollapsed, true);
            }
            else if (newNode.level === 1 && $scope.expandFirstChildUpToRootNode) {
                setNodeProperties(newNode, true, true);
            }
            else {
                setNodeProperties(newNode, true, false);
            }
        },

        setNodeProperties: function (node, isCollapsed, isVisible) {
            setNodeProperties(node, isCollapsed, isVisible);
        },

        setIconName: function (node) {
            setIconName(node);
        }
    };
});



