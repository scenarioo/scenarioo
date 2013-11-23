'use strict';

angular.module('scenarioo.directives').directive('scTree', function (RecursionHelper) {
    return {
        restrict: 'E',
        scope: {data: '='},
        template:
            '<div>' +
                '<span class="sc-node-label">{{data.nodeLabel}}: </span>' +
                '<span class="sc-node-value">{{data.nodeValue}}</span>' +
            '</div>' +
            '<ul>' +
                '<li ng-repeat="child in data.childNodes">' +
                    '<sc-tree data="child"></sc-tree>' +
                '</li>' +
            '</ul>',
        compile: function(element) {
            return RecursionHelper.compile(element);
        }
    };
});