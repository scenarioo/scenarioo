'use strict';

angular.module('scenarioo.directives').directive('scTree', function () {

    function createTreeHtml(data) {
        if(!angular.isObject(data) || (angular.isObject(data) && angular.isArray(data))) {
            return 'no data to display';
        }

        return getNodeHtml(data);
    }

    function getNodeHtml(node) {
        var html = '';
        if(angular.isDefined(node.nodeLabel)) {
            html = getNodeTitleHtml(node);
        }
        if(angular.isDefined(node.childNodes)) {
            html += getChildNodesHtml(node.childNodes);
        }

        return html;
    }

    function getNodeTitleHtml(data) {
        return '<span>' +
                    '<span class="sc-node-label">' + data.nodeLabel + '</span>' +
                    getNodeValueHtml(data.nodeValue) +
               '</span>';
    }

    function getNodeValueHtml(nodeValue) {
        if(angular.isUndefined(nodeValue)) {
            return '';
        }
        return '<span class="sc-node-label">: </span><span class="sc-node-value">' + nodeValue + '</span>';
    }

    function getChildNodesHtml(childNodes) {
        if(angular.isUndefined(childNodes) || !angular.isArray(childNodes) || childNodes.length == 0) {
            return '';
        }
        var html = '<ul class="sc-tree">';
        angular.forEach(childNodes, function(value, key) {
            html += '<li class="sc-tree">' + getNodeHtml(value) + '</li>';
        });
        html += '</ul>';
        return html;
    }

    return {
        restrict: 'E',
        scope: {data: '=data'},
        template: '<div ng-bind-html-unsafe="treeHtml"></div>',
        link: function(scope) {
            scope.$watch('data', function(newData) {
                scope.treeHtml = createTreeHtml(newData);
            });
        }
    };
});