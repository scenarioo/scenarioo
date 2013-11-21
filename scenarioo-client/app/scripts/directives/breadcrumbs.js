'use strict';

angular.module('scenarioo.directives').directive('usdBreadcrumb', function ($location, $route, $compile, $filter) {
    var limit = 50;


    var breadcrumbDescriptionObject = {
        scope: true,
        restrict: 'E',
        priority: 0,
        replace: true,
        templateUrl: 'template/breadcrumbs.html',
        link: function (scope, element, attrs) {

            var restParameters = splitPath($location.path());
            var pathOfCurrentBreadcrumb = '';
            scope.breadcrumbs = [];

            angular.forEach(restParameters, function (item, index) {
                item = item.trim();
                var isLastBreadcrumb = index === (restParameters.length - 1);
                if (item === '' || (pathOfCurrentBreadcrumb[pathOfCurrentBreadcrumb.length - 1] !== '/')) {
                    pathOfCurrentBreadcrumb += '/';
                }
                pathOfCurrentBreadcrumb += encodeURIComponent(item);

                var keyOfMatchingRoute = findPath($route.routes, pathOfCurrentBreadcrumb);
                var matchingRoute = $route.routes[keyOfMatchingRoute];

                if (matchingRoute) {
                    var text = $filter('scHumanReadable')(decodeURIComponent(item));

                    // Override displayed text with breadcrumb configuration in the routes of the app
                    if (matchingRoute.breadcrumb) {
                        text = matchingRoute.breadcrumb.replace('$param', text);

                        if (scope.title && isLastBreadcrumb)
                            text = text.replace('$title', scope.title);
                    }

                    // Create breadcrumb object
                    var hasTooltip = text.length > limit && !isLastBreadcrumb;
                    var breadcrumb = {
                        text: hasTooltip ? getShortenedText(text) : text,
                        tooltip: removeHtmlTags(text),
                        showTooltip: hasTooltip,
                        href: '#' + getBreadCrumbPathWithParameters(pathOfCurrentBreadcrumb, keyOfMatchingRoute),
                        isLast: isLastBreadcrumb
                    }

                    scope.breadcrumbs.push(breadcrumb);
                }
            });
            scope.email= {
                title: encodeURIComponent('Link to the User Scenario Documentation'),
                link: encodeURIComponent($location.absUrl())
            };
            element.debuggingHelp = "test";
        }
    };

    function getShortenedText(text) {
        if (text.length > limit) {
            var shortenedText = text.substr(0, limit);
            return shortenedText + '..';
        }
        return text;
    }

    function removeHtmlTags(text) {
        return text.replace(/<(?:.|\n)*?>/gm, '');
    }

    function findPath(routes, path) {
        for (var key in routes) {
            var routePath = key;

            // Replace route parameters
            routePath = routePath.replace(new RegExp(':[^/]+', 'gm'), '[^/]+');

            // Replace first part of the routepath
            routePath = routePath.replace(new RegExp('^/[^/]+/', 'gm'), '/[^/]+/');

            if (path.match(new RegExp('^' + routePath + '$', '')) !== null) {
                return key;
            }
        }
    }

    function splitPath(path) {
        var parts = path.split('/');
        // Small fix to avoid problems with path "/"
        if (parts.length > 1 && parts[0] === '' && parts[1] === '') {
            parts.splice(0, 1);
        }
        return parts;
    }

    function getBreadCrumbPathWithParameters(path, routeKey) {
        var parameters = splitPath(path);
        var parts = splitPath(routeKey);
        for (var i = 0; i < parts.length; i++) {
            if (parts[i].match('^:')) {
                parts[i] = parameters[i];
            }
        }
        return parts.join('/');
    }
    return breadcrumbDescriptionObject;
});