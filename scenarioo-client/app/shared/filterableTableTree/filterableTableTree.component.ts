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

angular.module('scenarioo.directives')
    .component('scFilterableTableTree', {

        bindings: {
            treedata: '<',
            rootIsCollapsed: '<',
            expandFirstChildUpToRootNode: '<',
            filter: '<',
            columns: '<',
            treemodel: '=',
            clickAction: '&',
            firstColumnTitle: '@',
        },
        template: require('./filterableTableTree.html'),
        controller: filterableTableTreeController,
    });

function filterableTableTreeController(GlobalHotkeysService, TreeNodeService, $filter) {
    const vm = this;
    const textLimit = 400;
    vm.treemodel = [];
    vm.toggleAllChildren = toggleAllChildren;
    vm.formatNodeName = formatNodeName;
    vm.isNodeIconEmpty = isNodeIconEmpty;
    vm.toggleCollapseNode = toggleCollapseNode;

    this.$onChanges = (changes) => {
        if (changes.treedata || changes.filter) {
            buildTreeModel();
        }
    };

    // Set's hotkey ESC to reset filter field
    bindClearFilter();

    function buildTreeModel() {
        vm.treemodel = [];
        vm.nodeFilter = vm.filter;
        setDefaultTitleForFirstColumnToName();

        angular.forEach(vm.treedata, (value, index) => {
            createNode(value, 0, index);
        });
    }

    function setDefaultTitleForFirstColumnToName() {
        if (vm.firstColumnTitle === undefined) {
            vm.firstColumnTitle = 'Name';
        }
    }

    function createNode(node, level, id, parent?) {

        if (angular.isUndefined(node.item)) {
            return 'No item defined on node';
        }

        const newNode = {
            id,
            name: node.item.name,
            type: node.item.type,
            columnData: {},
            searchFields: [],
            level,
            children: [],
            matching: false,
            parent,
            icon: '',
            isCollapsed: vm.rootIsCollapsed,
            isVisible: !vm.rootIsCollapsed,
        };

        // Fill columnData and search field values that are searchable
        newNode.searchFields.push(newNode.type);
        newNode.searchFields.push(newNode.name);
        angular.forEach(vm.columns, (value) => {
            let columnValue = node.details[value.propertyKey];
            columnValue = extractFirstHtmlElementText(columnValue);

            if (angular.isDefined(columnValue)) {
                newNode.columnData[value.propertyKey] = columnValue;
                newNode.searchFields.push(columnValue);
            }
        });

        TreeNodeService.setCollapsingAndVisibility(newNode, vm);

        if (angular.isDefined(parent)) {
            parent.children.push(newNode.id);
        }

        vm.treemodel.push(newNode);
        nodeFilter(newNode, vm.nodeFilter);

        angular.forEach(node.children, (value, index) => {
            createNode(value, level + 1, id + '_' + index, newNode);
            TreeNodeService.setIconName(newNode);
        });
    }

    // Traverses the tree-view top-down for all childs
    function toggleAllChildren(rootNode) {
        TreeNodeService.setNodeProperties(rootNode, !rootNode.isCollapsed, rootNode.isVisible);
        collapseExpandChildren(rootNode, rootNode.isCollapsed);
    }

    function formatNodeName(node) {
        // Only format scenario and usecase as human readable (all other text have to be generated how they should be displayed, by specification)
        let name = node.name;
        if ((node.type === 'scenario') || (node.type === 'usecase')) {
            name = $filter('scHumanReadable')(name);
        }
        return name;
    }

    function collapseExpandChildren(rootnode, parentIsCollapsed) {
        angular.forEach(vm.treemodel, (node) => {
            const childNode = rootnode.children.indexOf(node.id) > -1;

            if (childNode) {
                TreeNodeService.setNodeProperties(node, parentIsCollapsed, !parentIsCollapsed);
                collapseExpandChildren(node, node.isCollapsed);
            }
        });
    }

    function isNodeIconEmpty(node) {
        return node.icon === '' || angular.isUndefined(node.icon);
    }

    // Expand collapse only the current node
    function toggleCollapseNode(rootNode) {

        // When root is expanded, onClick provokes an collapse on all child's
        if (!rootNode.isCollapsed && rootNode.children.length > 0) {
            vm.toggleAllChilds(rootNode);
        } else {
            TreeNodeService.setNodeProperties(rootNode, !rootNode.isCollapsed, rootNode.isVisible);

            angular.forEach(vm.treemodel, (node) => {
                const childNode = rootNode.children.indexOf(node.id) > -1;

                if (childNode) {
                    TreeNodeService.setNodeProperties(node, node.isCollapsed, !node.isVisible);
                }
            });
        }
    }

    // Traverses the tree-view bottom-up
    function nodeFilter(node, filter) {
        if (angular.isUndefined(node) || angular.isUndefined(filter) || filter === '') {
            return false;
        }

        const filterPattern = filter.toUpperCase();
        const filters = filterPattern.split(' ');
        const searchKeyFound = [];

        for (const filterItem in filters) {
            for (const searchField in node.searchFields) {
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
        } else {
            node.matching = false;
            TreeNodeService.setNodeProperties(node, true, false);
        }
        return false;
    }

    function expandUpToRootNode(childNode) {
        if (angular.isDefined(childNode)) {
            const parentNode = vm.treemodel[vm.treemodel.indexOf(childNode.parent)];

            if (angular.isDefined(parentNode)) {
                TreeNodeService.setNodeProperties(parentNode, false, true);
            }

            expandUpToRootNode(parentNode);
        }
    }

    function bindClearFilter() {
        GlobalHotkeysService.registerPageHotkeyCode(27, () => {
            vm.filter = '';
        });
    }

    function convertToPlainText(text) {
        return text.replace(/<\/?[^>]+(>|$)/g, '');
    }

    function getShortenedText(text) {
        if (text.length > textLimit) {
            const shortenedText = text.substr(0, textLimit);
            return shortenedText + '...';
        }
        return text;
    }

    function extractFirstHtmlElementText(columnValue) {
        if (angular.isUndefined(columnValue)) {
            return undefined;
        }

        const matching = columnValue.match(/(<((p|div).*?)(?=<\/(p|div)>))/i);

        if (matching !== null) {
            // Only the first match will be processed
            columnValue = convertToPlainText(matching[0]);
        }

        return getShortenedText(columnValue);
    }
}
