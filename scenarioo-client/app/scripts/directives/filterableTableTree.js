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

            function buildTreeModel(data, filter){
                scope.treemodel = [];
                scope.nodeFilter = filter;

                angular.forEach(data, function (value, index) {
                    createNode(value, 0, index);
                });
            }

            function createNode(node, level, id, parent){
                var newNode = {
                    'id': id,
                    'name': node.item.name,
                    'type': node.item.type,
                    'description': node.details.description,
                    'searchFields': [node.item.name, node.item.type, node.details.description],
                    'level': level,
                    'children': [],
                    'isCollapsed': true,
                    'isVisible': false}

                if (newNode.level == 0) {
                    newNode.isCollapsed = true;                    
                    newNode.isVisible = true;
                }
                
                if (angular.isUndefined(scope.nodeFilter) || scope.nodeFilter == "") {
                    scope.treemodel.push(newNode);
                }
                else if (nodeFilter(newNode.searchFields, scope.nodeFilter)){
                    scope.treemodel.push(newNode);
                }

                if (parent) {
                    parent.children.push(newNode.id, newNode.isVisible);
                }
                
                angular.forEach(node.children, function (value, index) {
                    createNode(value, level + 1, id + "_" + index, newNode);
                });
            };

            scope.toggleCollapse = function(index){
                var rootNode = scope.treemodel[index];

                rootNode.isCollapsed = !rootNode.isCollapsed;
                setCollapseChildren(rootNode, rootNode.isCollapsed);
            };

            function setCollapseChildren(rootnode, parentIsCollapsed) {
                angular.forEach(scope.treemodel, function(node, index) {
                    var isThisNodeChildOfRootNode = rootnode.children.indexOf(node.id) > -1;

                    if (isThisNodeChildOfRootNode) {
                        node.isVisible = !parentIsCollapsed;
                        node.isCollapsed = parentIsCollapsed;
                        setCollapseChildren(node, node.isCollapsed);
                    }
                });
            }

            function nodeFilter(searchFields, filter) {
                var filterPattern = filter.toUpperCase();
                var match = false;
                var filters = filterPattern.split(" ");
                var filterMatches = [];

                for (var filter in filters) {
                    for (var field in searchFields) {
                        match = (searchFields[field].toUpperCase().indexOf(filters[filter]) > -1);
                        return match;
                    }
                };

                return match;
            }

            scope.$watchCollection('[treedata, filter]', function(newValues) {
                buildTreeModel(newValues[0], newValues[1]);
            });
        }
    };
});