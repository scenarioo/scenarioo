'use strict';

NgUsdClientApp.directive('usdBreadcrumb', function($location, $route                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ) {
    var breadcrumbDescriptionObject = {
        restrict:'E',
        priority:0,
        replace:true,
        transclude:true,
        templateUrl:'template/breadcrumbs.html',
        scope: false,
        link:function (scope, element) {
            var ul = angular.element(element.children()[0]);
            var paths = splitPath($location.path());
            var path = "";
            angular.forEach(paths, function (item) {
                item=item.trim();
                var last = paths.indexOf(item) == (paths.length - 1);
                if (item=="" || (path[path.length-1]!='/')) {
                    path += '/';
                }
                path += item;

                var routeKey = findPath($route.routes, path);
                var route = $route.routes[routeKey];
                if(route) {
                    var text = item;
                    if (route.breadcrumb != undefined) {
                        text = route.breadcrumb;
                    }
                    var href = "#" + getBreadCrumbPathWithParameters(path, routeKey);
                    var lnk;
                    if (!last) {
                        lnk = "<li><a href='" + href + "'>" + text + "</a> <span class='divider'>&gt;</span></li>"
                    } else {
                        lnk = "<li class='active'>" + text + "</li>";
                    }
                    this.append(lnk);
                }
            }, ul);
        }
    };
    return breadcrumbDescriptionObject;

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