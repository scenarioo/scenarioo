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

angular.module('scenarioo.directives').directive('scFilterableTableTree', function ($sce, $controller) {

        // Need to be clarified: This part will be called multiple times, why?? (One time without any data, second time with data)
        function createTreeHtml(data) {

            if (!angular.isObject(data)) {
                return 'no data to display';
            }
            else if (angular.isObject(data) && angular.isArray(data)) {
                var tableHtml = '<table id="treeviewtable" ng-table="tableParams" class="table table-curved table-hover table-responsive usecase-table ng-isolate-scope ng-pristine ng-valid"><tr><th>Name</th><th>Type</th><th>Description</th></tr>';
                var indentation = 1;
                var parentId = 1;

                angular.forEach(data, function (value, index) {
                    parentId = $controller.rowId;
                    tableHtml += createTableRowHtml(value, indentation, parentId);
                    tableHtml += getChildNode(value.children, indentation, parentId);
                });

                return tableHtml += '</table>';
            }

            return tableHtml;
        }

        function getChildNode(childdata, indentation, parentId) {
            indentation += 1;

            if (!angular.isObject(childdata)) {
                return 'no data to display';
            }
            else if (angular.isObject(childdata) && angular.isArray(childdata)) {
                var rowHtml = '';
                
                parentId = $controller.rowId;
                $controller.rowId += 1;

                angular.forEach(childdata, function (value, index) {
                    rowHtml += createTableRowHtml(value, indentation, parentId);
                    rowHtml += getChildNode(value.children, indentation, parentId);
                });

                return rowHtml;
            }
        }

        function createTableRowHtml(value, indentation, parentId) {

            if (angular.isDefined(value)) {

                var parentAttribute = '';
                if ($controller.rowId != parentId)
                {
                    parentAttribute = ' parent-id="' + parentId + '"';
                }

                var rowHtml = '<tr onClick="toggleElements(this.getAttribute(&quot;id&quot;))" id="' + $controller.rowId + '"' + parentAttribute + '>';

                if (angular.isDefined(value.item.name)) {
                    rowHtml += '<td>' + getHtmlIndent(value.item.name, indentation) + '</td>';
                }

                if (angular.isDefined(value.item.type)) {
                    rowHtml += '<td>' + value.item.type + '</td>';
                }

                if (angular.isDefined(value.details.description)) {
                    rowHtml += '<td>' + value.details.description + '</td>';
                }

                return rowHtml += '</tr>';

            }
        }

        function getHtmlIndent(htmlElementToIndent, indentation) {

            var indentHtml = '';

            for (var i = 0; i < indentation; i++) {
                indentHtml += '<ul>';
            }

            indentHtml += htmlElementToIndent;

            for (var i = 0; i < indentation; i++) {
                indentHtml += '</ul>';
            }

            return indentHtml;
        }

        return {
            restrict: 'E',
            scope: {
                data: '=data',
            },
            template: '<div ng-bind-html="treeHtml" class="sc-filter-tree"></div>',
            replace: true,

            link: function (scope, elem, attrs, ctrl) {
                $controller.rowId = 1;
                scope.$watch('data', function (newData) {
                    // there is no 'ng-bind-html-unsafe' anymore. we use Strict Contextual Escaping, see
                    // http://docs.angularjs.org/api/ng/service/$sce for more information
                    scope.treeHtml = $sce.trustAsHtml(createTreeHtml(newData));
                });

            }
    }
});
