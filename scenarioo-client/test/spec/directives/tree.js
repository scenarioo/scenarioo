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
        expect(element.html()).toBe('<div ng-hide="!data.nodeLabel" class="ng-scope" style="display: none; "><span class="sc-node-label ng-binding"></span><span ng-show="data.nodeValue || data.nodeValue == 0" style="display: none; ">: </span><span class="sc-node-value ng-binding"></span></div><ul class="sc-tree ng-scope"><!-- ngRepeat: child in data.childNodes --></ul>');
    });

    it('displays empty tree if data is not of type object', function() {
        $scope.data = 'content';
        compileDirective();
        expect(element.html()).toBe('<div ng-hide="!data.nodeLabel" class="ng-scope" style="display: none; "><span class="sc-node-label ng-binding"></span><span ng-show="data.nodeValue || data.nodeValue == 0" style="display: none; ">: </span><span class="sc-node-value ng-binding"></span></div><ul class="sc-tree ng-scope"><!-- ngRepeat: child in data.childNodes --></ul>');
    });

    it('displays a tree if tree data is provided', function () {
        $scope.data = TREE_DATA;
        compileDirective();
        expect(element.html()).toBe('<div ng-hide="!data.nodeLabel" class="ng-scope"><span class="sc-node-label ng-binding">root</span><span ng-show="data.nodeValue || data.nodeValue == 0" style="display: none; ">: </span><span class="sc-node-value ng-binding"></span></div><ul class="sc-tree ng-scope"><!-- ngRepeat: child in data.childNodes --><li ng-repeat="child in data.childNodes" class="sc-tree ng-scope"><sc-tree data="child" class="ng-isolate-scope ng-scope"><div ng-hide="!data.nodeLabel" class="ng-scope"><span class="sc-node-label ng-binding">myKey</span><span ng-show="data.nodeValue || data.nodeValue == 0">: </span><span class="sc-node-value ng-binding">myValue</span></div><ul class="sc-tree ng-scope"><!-- ngRepeat: child in data.childNodes --></ul></sc-tree></li><li ng-repeat="child in data.childNodes" class="sc-tree ng-scope"><sc-tree data="child" class="ng-isolate-scope ng-scope"><div ng-hide="!data.nodeLabel" class="ng-scope"><span class="sc-node-label ng-binding">keyTwo</span><span ng-show="data.nodeValue || data.nodeValue == 0">: </span><span class="sc-node-value ng-binding">valueTwo</span></div><ul class="sc-tree ng-scope"><!-- ngRepeat: child in data.childNodes --></ul></sc-tree></li></ul>');
    });

    function compileDirective() {
        var html = '<sc-tree data="data"></sc-tree>';
        element = $compile(html)($scope);
        $scope.$apply();
    }

});