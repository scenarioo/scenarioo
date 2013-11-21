'use strict';

angular.module('scenarioo.directives').directive('scMetadata', function ($filter) {
    var objectToListObject = {
        restrict: 'A',
        scope: true,
        link: function (scope, element, attrs) {

            scope.$watch(attrs.scMetadata, function () {
                var text = scope.$eval(attrs.scMetadata);
                if (!text) {
                    return;
                }
                if ((typeof text !== 'object')) {
                    element.html(text);
                }

                var path = attrs.scMetadata;
                if (typeof text === 'object') {
                    var obj = text;
                    var keys = Object.keys(obj);
                    while (typeof obj === 'object' && (keys.length === 1 || (keys.length === 2 && obj.$$hashKey))) {
                        var firstKey = keys[0];
                        path += '.' + firstKey;
                        obj = obj[firstKey];
                    }
                    var list = addValues(obj, path);
                    var listElement = angular.element(list);
                    //$compile(listElement.contents())(scope);
                    element.append(listElement);
                }
            });

            function addValues(obj, path) {
                var list = angular.element('<dl></dl>');
                var currentList = list;
                var listMore;
                var amount = 0;
                angular.forEach(obj, function (value, key) {
                    if (key === '$$hashKey') {
                        return;
                    }
                    amount++;
                    if (amount === 5) {
                        listMore = angular.element('<dl></dl>');
                        currentList = listMore;
                    }
                    currentList.append('<dt> ' + $filter('toHumanReadable')(key) + '</dt>');
                    var valueList = '';
                    if (typeof value === 'object') {
                        var newPath = path + '.' + key;
                        valueList = addValues(value, newPath);
                        currentList.append(valueList);
                    } else {
                        valueList = $filter('toHumanReadable')(value);
                    }
                    var valueDd = angular.element('<dd></dd>');
                    valueDd.append(valueList);
                    currentList.append(valueDd);
                });

                var objRepresentation = angular.element('<div></div>');
                objRepresentation.append(list);
                if (listMore) {
                    var moreInfoLink = angular.element('<a class="link">more ...</a>');
                    moreInfoLink.bind('click', function () {
                        if (listMore.css('display') === 'none') {
                            listMore.css('display', 'block');
                            moreInfoLink.text('less ...');
                        } else {
                            listMore.css('display', 'none');
                            moreInfoLink.text('more ...');
                        }
                    });
                    listMore.css('display', 'none');
                    objRepresentation.append(listMore).append(moreInfoLink);
                }
                return objRepresentation;
            }
        }
    };
    return objectToListObject;
});