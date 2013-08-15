'use strict';

NgUsdClientApp.directive("objecttolist", function($filter, $compile) {
    var objectToListObject = {
        restrict:'A',
        scope: true,
        link:function (scope, element, attrs) {
            var uniqueId = 0;

            scope.$watch(attrs['objecttolist'], function() {
                var text = scope.$eval(attrs['objecttolist']);
                if (!text) { return; }
                if (!(typeof text === 'object')) {
                    element.html(text);
                }

                var path = attrs['objecttolist'];
                if (typeof text === 'object') {
                    var obj = text;
                    var keys = Object.keys(obj);
                    while (typeof obj === 'object' && (keys.length===1 || (keys.length===2 && obj.$$hashKey))) {
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
                angular.forEach(obj, function(value, key) {
                    if (key=="$$hashKey") {
                        return;
                    }
                    amount++;
                    if (amount == 5) {
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
                    moreInfoLink.bind('click', function() {
                        if (listMore.css('display')=='none') {
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

NgUsdClientApp.directive("sortandfilter", function($compile, $filter) {
    var sortableDescriptionObject = {
        restrict:'A',
        scope: true,
        link:function (scope, element, attrs) {
            scope.$watch(attrs['sortandfilter'], function() {
                var sortAndFilter = attrs['sortandfilter'];
                if (!sortAndFilter) {
                    return;
                }
                element.addClass("link");

                var iconStr = "<i class='icon-sort pull-right' ng-show=\"table.sort.column!='"+sortAndFilter+"'\"></i>"+
                    "<i class='icon-sort-up pull-right' ng-show=\"table.sort.column=='"+sortAndFilter+"' && !table.sort.reverse\"></i>" +
                    "<i class='icon-sort-down pull-right' ng-show=\"table.sort.column=='"+sortAndFilter+"' && table.sort.reverse\"></i>";
                var filterStr = "";
                if (!element.hasClass("filter-none")) {
                    filterStr = "<div class='tableFilter' ng-show=\"table.filtering\"><input type='text' ng-model='table.search[\""+sortAndFilter+"\"]' stop-event='click' placeholder='Enter search criteria...'></div>"
                }
                var iconsAndFilter = angular.element(iconStr+filterStr);
                var iconsAndFilterCompiled = $compile(iconsAndFilter)(scope);
                element.append(iconsAndFilterCompiled);
                element.bind('click', function() {
                    if (!scope.table.sort) {
                        scope.table.sort = {};
                    }
                    var changed = scope.table.sort.column != sortAndFilter;
                    if (changed) {
                        scope.table.sort.column = sortAndFilter;
                    }
                    if (changed) {
                        scope.table.sort.reverse = false;
                    } else {
                        scope.table.sort.reverse = !scope.table.sort.reverse;
                    }
                    scope.$apply();
                });
            });
        }
    };
    return sortableDescriptionObject;
});

NgUsdClientApp.directive('stopEvent', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            element.bind(attr.stopEvent, function (e) {
                e.stopPropagation();
            });
        }
    };
});

NgUsdClientApp.directive('usdBreadcrumb', function($location, $route, $compile, $filter) {
    var limit = 25;
    var breadcrumbDescriptionObject = {
        restrict:'E',
        priority:0,
        replace:true,
        transclude:true,
        templateUrl:'template/breadcrumbs.html',
        scope: false,
        link:function (scope, element, attrs) {
            var ul = angular.element(element.children()[0]);
            var paths = splitPath($location.path());
            var path = "";
            var htmlCode = "";
            angular.forEach(paths, function (item, index) {
                item=item.trim();
                var last = index == (paths.length - 1);
                if (item=="" || (path[path.length-1]!='/')) {
                    path += '/';
                }
                path += encodeURIComponent(item);

                var routeKey = findPath($route.routes, path);
                var route = $route.routes[routeKey];
                if(route) {
                    var longText = decodeURIComponent(item);
                    longText = $filter('toHumanReadable')(longText);
                    var text = getShortenedText(longText);
                    var tooltip = getTooltip(longText);
                    if (route.breadcrumb) {
                        text = route.breadcrumb.replace("$param", text);
                        tooltip = "";
                    }
                    var href = "#" + getBreadCrumbPathWithParameters(path, routeKey);
                    var lnk;
                    if (!last) {
                        lnk = "<li " + tooltip + "><a href='" + href + "' " + tooltip + ">" + text + "</a> <span class='divider'> / </span></li>"
                    } else {
                        if (attrs['name']) {
                            tooltip = "";
                            text = attrs['name'];
                        }
                        scope.emailTitle = encodeURIComponent($filter('toHumanReadable')(removeHtmlTags(item)));
                        lnk = "<li class='active'  " + tooltip + ">" + text + "</li>";
                    }
                    htmlCode = htmlCode + lnk;
                }
            });
            if (!scope.emailTitle) {
                scope.emailTitle = encodeURIComponent("Link to the User Scenario Documentation");
            }
            scope.emailLink = $location.absUrl();
            var div = angular.element(element.children()[0]);
            var ul = angular.element(div.children()[0]);
            ul.html(htmlCode);
            $compile(element)(scope);
        }
    };
    return breadcrumbDescriptionObject;

    function getShortenedText(text) {
        if (text.length>limit) {
            var shortenedText = text.substr(0, limit);
            return shortenedText + "..";
        }
        return text;
    }

    function removeHtmlTags(text) {
        return text.replace(/<(?:.|\n)*?>/gm, '');
    }

    function getTooltip(text) {
        if (text.length>limit) {
            return "tooltip='"+text+"' tooltip-placement='top'";
        }
        return "";
    }

    function findPath(routes, path) {
        for (var key in routes) {
            var routePath = replacePathParameters(key);
            routePath = removeFirstPartOfPath(routePath);
            if (path.match(new RegExp("^"+routePath+"$", "")) != null) {
                return key;
            }
        }
    }

    function replacePathParameters(routepath) {
        // Replace route parameters
        var routePathAsRegex = routepath.replace(new RegExp(":[^/]+", "gm"), "[^/]+");
        return routePathAsRegex;
    }

    function removeFirstPartOfPath(routepath) {
        // Replace first part of the routepath
        var routePathAsRegex = routepath.replace(new RegExp("^/[^/]+/", "gm"), "/[^/]+/");
        return routePathAsRegex;
    }

    function splitPath(path) {
        var parts = path.split('/');
        //Small fix to avoid problems with path "/"
        if (parts.length>1 && parts[0]==="" && parts[1]==="") {
            parts.splice(0, 1);
        }
        return parts;
    }

    function getBreadCrumbPathWithParameters(path, routeKey) {
        var parameters = splitPath(path);
        var parts = splitPath(routeKey);
        for (var i=0; i<parts.length; i++) {
            if (parts[i].match("^:")) {
                parts[i] = parameters[i];
            }
        }
        return parts.join("/");
    }
});