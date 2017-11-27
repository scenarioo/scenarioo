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

angular.module('scenarioo.directives').directive('scFilterableTableTree', function (GlobalHotkeysService, TreeNodeService, $filter) {

    var textLimit = 400;

    return {
        restrict: 'AE',
        scope: {
            treedata: '=',
            rootIsCollapsed: '=',
            expandFirstChildUpToRootNode: '=',
            filter: '=',
            columns: '=',
            treemodel: '=',
            clickAction: '&',
            firstColumnTitle: '@'
        },
        template: require('./filterableTableTree.html'),
        link: function (scope: any) {
            scope.treemodel = [];

            // Set's hotkey ESC to reset filter field
            bindClearFilter();

            function buildTreeModel(data, filter) {
                scope.treemodel = [];
                scope.nodeFilter = filter;
                setDefaultTitleForFirstColumnToName();

                angular.forEach(data, function (value, index) {
                    createNode(value, 0, index);
                });
            }

            function setDefaultTitleForFirstColumnToName() {
                if (scope.firstColumnTitle === undefined) {
                    scope.firstColumnTitle = 'Name';
                }
            }

            function createNode(node, level, id, parent?) {

                if (angular.isUndefined(node.item)) {
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
                    'icon': '',
                    'isCollapsed': scope.rootIsCollapsed,
                    'isVisible': !scope.rootIsCollapsed
                };

                // Fill columnData and search field values that are searchable
                newNode.searchFields.push(newNode.type);
                newNode.searchFields.push(newNode.name);
                angular.forEach(scope.columns, function (value) {
                    var columnValue = node.details[value.propertyKey];
                    columnValue = extractFirstHtmlElementText(columnValue);

                    if (angular.isDefined(columnValue)) {
                        newNode.columnData[value.propertyKey] = columnValue;
                        newNode.searchFields.push(columnValue);
                    }
                });

                TreeNodeService.setCollapsingAndVisibility(newNode, scope);

                if (angular.isDefined(parent)) {
                    parent.children.push(newNode.id);
                }

                scope.treemodel.push(newNode);
                nodeFilter(newNode, scope.nodeFilter);

                angular.forEach(node.children, function (value, index) {
                    createNode(value, level + 1, id + '_' + index, newNode);
                    TreeNodeService.setIconName(newNode);
                });
            }

            // Traverses the tree-view top-down for all childs
            scope.toggleAllChilds = function (rootNode) {
                TreeNodeService.setNodeProperties(rootNode, !rootNode.isCollapsed, rootNode.isVisible);
                collapseExpandChildren(rootNode, rootNode.isCollapsed);
            };

            scope.formatNodeName = function (node) {
                // Only format scenario and usecase as human readable (all other text have to be generated how they should be displayed, by specification)
                var name = node.name;
                if ((node.type === 'scenario') || (node.type === 'usecase')) {
                    name = $filter('scHumanReadable')(name);
                }
                return name;
            };

            function collapseExpandChildren(rootnode, parentIsCollapsed) {
                angular.forEach(scope.treemodel, function (node) {
                    var childNode = rootnode.children.indexOf(node.id) > -1;

                    if (childNode) {
                        TreeNodeService.setNodeProperties(node, parentIsCollapsed, !parentIsCollapsed);
                        collapseExpandChildren(node, node.isCollapsed);
                    }
                });
            }

            scope.isNodeIconEmpty = function (node) {
                if (node.icon === '' || angular.isUndefined(node.icon)) {
                    return true;
                }

                return false;
            };

            // Expand collapse only the current node
            scope.toggleCollapseNode = function (rootNode) {

                // When root is expanded, onClick provokes an collapse on all child's
                if (!rootNode.isCollapsed && rootNode.children.length > 0) {
                    scope.toggleAllChilds(rootNode);
                }
                else {
                    TreeNodeService.setNodeProperties(rootNode, !rootNode.isCollapsed, rootNode.isVisible);

                    angular.forEach(scope.treemodel, function (node) {
                        var childNode = rootNode.children.indexOf(node.id) > -1;

                        if (childNode) {
                            TreeNodeService.setNodeProperties(node, node.isCollapsed, !node.isVisible);
                        }
                    });
                }
            };

            // Traverses the tree-view bottom-up
            function nodeFilter(node, filter) {
                if (angular.isUndefined(node) || angular.isUndefined(filter) || filter === '') {
                    return false;
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
                    TreeNodeService.setNodeProperties(node, true, true);
                    expandUpToRootNode(node);

                    return true;
                }
                else {
                    node.matching = false;
                    TreeNodeService.setNodeProperties(node, true, false);
                }
                return false;
            }

            function expandUpToRootNode(childNode) {
                if (angular.isDefined(childNode)) {
                    var parentNode = scope.treemodel[scope.treemodel.indexOf(childNode.parent)];

                    if (angular.isDefined(parentNode)) {
                        TreeNodeService.setNodeProperties(parentNode, false, true);
                    }

                    expandUpToRootNode(parentNode);
                }
            }

            function bindClearFilter() {
                GlobalHotkeysService.registerPageHotkeyCode(27, function () {
                    scope.filter = '';
                });
            }

            function convertToPlainText(text) {
                return text.replace(/<\/?[^>]+(>|$)/g, '');
            }

            function getShortenedText(text) {
                if (text.length > textLimit) {
                    var shortenedText = text.substr(0, textLimit);
                    return shortenedText + '...';
                }
                return text;
            }

            function extractFirstHtmlElementText(columnValue) {
                if (angular.isUndefined(columnValue)) {
                    return undefined;
                }

                var matching = columnValue.match(/(<((p|div).*?)(?=<\/(p|div)>))/i);

                if (matching !== null) {
                    // Only the first match will be processed
                    columnValue = convertToPlainText(matching[0]);
                }

                return getShortenedText(columnValue);
            }

            scope.$watchCollection('[treedata, filter]', function (newValues) {
                buildTreeModel(newValues[0], newValues[1]);
            });
        }
    };
});
