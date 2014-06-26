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

angular.module('scenarioo.directives').directive('scFilterableTableTree', function () {
    return {
        restrict: 'AE',
        scope: {
            treedata: '=',
            filter: "="
        },
        templateUrl: 'template/treeview.html',
        link: function (scope, elem, attrs) {
            scope.treemodel = [];
            scope.collapsedIconName = 'collapsed.png';
            scope.expandedIconName= 'expanded.png';

            function buildTreeModel(data, filter){
                scope.treemodel = [];
                scope.nodeFilter = filter;

                angular.forEach(data, function (value, index) {
                    createNode(value, 0, index);
                });
            }

            function createNode(node, level, id, parent){

                if (angular.isUndefined(node.item))
                {
                    return "No item defined on node";
                }

                var newNode = {
                    'id': id,
                    'name': node.item.name,
                    'type': node.item.type,
                    'description': node.details.description,
                    // TODO: fill all columns configured in configuration
                    'searchFields': [node.item.name, node.item.type, node.details.description],
                    'level': level,
                    'children': [],
                    'matching': false,
                    'parent': parent,
                    'icon': scope.collapsedIconName,
                    'isCollapsed': true,
                    'isVisible': false}

                if (newNode.level == 0) {
                    setNodeProperties(newNode, true, true);
                }
               
                if (angular.isDefined(parent)) {
                   parent.children.push(newNode.id);
                }

                scope.treemodel.push(newNode);
                nodeFilter(newNode, scope.nodeFilter);

                angular.forEach(node.children, function (value, index) {
                    createNode(value, level + 1, id + "_" + index, newNode);
                });
            };

            // Traverses the tree-view top-down
            scope.toggleCollapse = function(index){
                var rootNode = scope.treemodel[index];

                rootNode.isCollapsed = !rootNode.isCollapsed;
                rootNode.icon = rootNode.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;
                collapseChrildren(rootNode, rootNode.isCollapsed);
            };

            function collapseChrildren(rootnode, parentIsCollapsed) {
                angular.forEach(scope.treemodel, function(node, index) {
                    var childNode = rootnode.children.indexOf(node.id) > -1;

                    if (childNode) {
                        node.isVisible = !parentIsCollapsed;
                        node.isCollapsed = parentIsCollapsed;
                        node.icon = node.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;

                        collapseChrildren(node, node.isCollapsed);
                    }
                });
            }

            // Traverses the tree-view bottom-up
            function nodeFilter(node, filter) {
                if (angular.isUndefined(node) || angular.isUndefined(filter) || filter == "")
                {
                    return;
                }

                var filterPattern = filter.toUpperCase();
                var filters = filterPattern.split(" ");

                for (var filter in filters) {
                    for (var searchField in node.searchFields) {

                        if (node.searchFields[searchField].toUpperCase().search(filters[filter]) > -1) {
                            node.matching = true;
                            setNodeProperties(node, true, true);
                            expandUpToRoodNode(node);
                        }
                    }
                };
            }

            function expandUpToRoodNode(childNode) {
                if (angular.isDefined(childNode)) {
                    var parentNode = scope.treemodel[scope.treemodel.indexOf(childNode.parent)];
                    
                    if (angular.isDefined(parentNode)) {
                        setNodeProperties(parentNode, false, true);
                    }

                    expandUpToRoodNode(parentNode);
                }
            }

            function setNodeProperties(node, isCollapsed, isVisible) {
                node.isCollapsed = isCollapsed;
                node.isVisible = isVisible;
                node.icon = node.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;
            }

            scope.$watchCollection('[treedata, filter]', function(newValues) {
                buildTreeModel(newValues[0], newValues[1]);
            });
        }
    };
});