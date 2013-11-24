'use strict';

angular.module('scenarioo.directives').directive('scTree', function (RecursionHelper) {
    return {
        restrict: 'E',
        scope: {data: '='},
        template:
            '<div ng-hide="!data.nodeLabel">' +
                '<span class="sc-node-label">{{data.nodeLabel}}</span><span ng-show="data.nodeValue || data.nodeValue == 0">: </span>' +
                '<span class="sc-node-value">{{data.nodeValue}}</span>' +
            '</div>' +
            '<ul class="sc-tree">' +
                '<li ng-repeat="child in data.childNodes" class="sc-tree">' +
                    '<sc-tree data="child"></sc-tree>' +
                '</li>' +
            '</ul>',
        compile: function(element) {
            return RecursionHelper.compile(element);
        }
    };
});