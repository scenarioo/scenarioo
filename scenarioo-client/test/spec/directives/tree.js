'use strict';

describe('Directive :: scTree', function () {

    var $scope, $compile, $httpBackend;
    var element;

    var TREE_DATA = {
        nodeLabel : 'root',
        childNodes : [
            {
                nodeLabel: 'myKey',
                nodeValue: 'myValue'
            },
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    beforeEach(module('scenarioo.directives'));

    beforeEach(inject(function ($rootScope, _$compile_, _$httpBackend_) {
        $scope = $rootScope.$new();
        $rootScope.$apply();

        $compile = _$compile_;
        $httpBackend = _$httpBackend_;
    }));

    it('knows the behaviour of isArray and isObject', function() {
        expect(angular.isArray({})).toBeFalsy();
        expect(angular.isArray([])).toBeTruthy();
        expect(angular.isObject({})).toBeTruthy();
        expect(angular.isObject([])).toBeTruthy(); // Array is seen as object!!
    });

    it('displays empty tree if data is undefined', function () {
        $scope.data = undefined;
        compileDirective();
        expect(element.html()).toBe('<div ng-bind-html-unsafe="treeHtml" class="ng-binding">no data to display</div>');
    });

    it('displays empty tree if data is not of type object', function() {
        $scope.data = 'content';
        compileDirective();
        expect(element.html()).toBe('<div ng-bind-html-unsafe="treeHtml" class="ng-binding">no data to display</div>');
    });

    it('displays a tree if tree data is provided', function () {
        $scope.data = TREE_DATA;
        compileDirective();
        expect(element.html()).toBe('<div ng-bind-html-unsafe="treeHtml" class="ng-binding"><span><span class="sc-node-label">root</span></span><ul class="sc-tree"><li class="sc-tree"><span><span class="sc-node-label">myKey</span><span class="sc-node-label">: </span><span class="sc-node-value">myValue</span></span></li><li class="sc-tree"><span><span class="sc-node-label">keyTwo</span><span class="sc-node-label">: </span><span class="sc-node-value">valueTwo</span></span></li></ul></div>');
    });

    function compileDirective() {
        var html = '<sc-tree data="data"></sc-tree>';
        element = $compile(html)($scope);
        $scope.$apply();
    }

});