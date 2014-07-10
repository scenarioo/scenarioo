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

angular.module('scenarioo.directives').directive('scFilterableTableTree', function (GlobalHotkeysService) {
    return {
        restrict: 'AE',
        scope: {
            treedata: '=',
            rootiscollapsed: '=',
            filter: '=',
            columns: '=',
            treemodel: '=',
            clickAction: '&'
        },
        templateUrl: 'template/treeview.html',
        link: function (scope) {
            scope.treemodel = [];
            scope.collapsedIconName = 'collapsed.png';
            scope.expandedIconName= 'expanded.png';
            scope.noneIconName = '';

            // Set's hotkey ESC to reset filter field
            bindClearFilter();

            function buildTreeModel(data, filter) {
                scope.treemodel = [];
                scope.nodeFilter = filter;

                angular.forEach(data, function (value, index) {
                    createNode(value, 0, index);
                });
            }

            function createNode(node, level, id, parent) {

                if (angular.isUndefined(node.item))
                {
                    return 'No item defined on node';
                }

                var newNode = {
                    'id': id,
                    'name': node.item.name,
                    'type': node.item.type,
                    'columnData': {},
                    'searchFields': [],
                    'level': level,
                    'children': [],
                    'matching': false,
                    'parent': parent,
                    'icon': scope.collapsedIconName,
                    'isCollapsed': scope.rootiscollapsed,
                    'isVisible': !scope.rootiscollapsed
                };

                // Fill columnData and search field values that are searchable
                newNode.searchFields.push(newNode.type);
                newNode.searchFields.push(newNode.name);
                angular.forEach(scope.columns, function(value) {
                    var columnValue = node.details[value.propertyKey];
                    newNode.columnData[value.propertyKey] = columnValue;
                    newNode.searchFields.push(columnValue);
                });

                // Root-node
                if (newNode.level === 0) {
                    setNodeProperties(newNode, scope.rootiscollapsed, true);
                }

                if (angular.isDefined(parent)) {
                    parent.children.push(newNode.id);
                }

                scope.treemodel.push(newNode);
                nodeFilter(newNode, scope.nodeFilter);

                angular.forEach(node.children, function (value, index) {
                    createNode(value, level + 1, id + '_' + index, newNode);
                });
            }

            // Traverses the tree-view top-down for all childs
            scope.toggleAllChilds = function(rootNode) {
                rootNode.isCollapsed = !rootNode.isCollapsed;
                rootNode.icon = rootNode.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;
                collapseExpandChildren(rootNode, rootNode.isCollapsed);
            };

            function collapseExpandChildren(rootnode, parentIsCollapsed) {
                angular.forEach(scope.treemodel, function(node) {
                    var childNode = rootnode.children.indexOf(node.id) > -1;

                    if (childNode) {
                        node.isVisible = !parentIsCollapsed;
                        node.isCollapsed = parentIsCollapsed;
                        node.icon = node.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;

                        collapseExpandChildren(node, node.isCollapsed);
                    }
                });
            }

            // Expand collapse only the current node
            scope.toggleCollapseNode = function(rootNode) {

                // When root expanded, onClick provokes an collapse on all childs
                if (!rootNode.isCollapsed && rootNode.children.length > 0) {
                    scope.toggleAllChilds(rootNode);
                }
                else {
                    rootNode.isCollapsed = !rootNode.isCollapsed;
                    rootNode.icon = rootNode.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;

                    angular.forEach(scope.treemodel, function (node) {
                        var childNode = rootNode.children.indexOf(node.id) > -1;

                        if (childNode) {
                            node.isVisible = !node.isVisible;
                            node.isCollapsed = node.isCollapsed;
                            node.icon = node.isCollapsed ? scope.expandedIconName : scope.collapsedIconName;
                        }
                    });
                }
            };

            // Traverses the tree-view bottom-up
            function nodeFilter(node, filter) {
                if (angular.isUndefined(node) || angular.isUndefined(filter) || filter === '') {
                    return;
                }

                var filterPattern = filter.toUpperCase();
                var filters = filterPattern.split(' ');
                var searchKeyFound = [];

                for (var filterItem in filters) {

                    for (var searchField in node.searchFields) {

                        if (node.searchFields[searchField] !== '') {

                            if ((angular.isDefined(node.searchFields[searchField]) &&
                                    node.searchFields[searchField].toUpperCase().search(filters[filterItem]) > -1)) {

                                searchKeyFound.push(true);
                                break;
                            }
                        }
                    }
                }

                if (searchKeyFound.length === filters.length) {
                    node.matching = true;
                    setNodeProperties(node, true, true);
                    expandUpToRoodNode(node);

                    return true;
                }
                else
                {
                    node.matching = false;
                    setNodeProperties(node, true, false);
                }
                return false;
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

            function bindClearFilter() {
                GlobalHotkeysService.registerPageHotkeyCode(27, function () {
                    scope.filter = '';
                });
            }

        }
    };
});