'use strict';

angular.module('ngUSDClientApp.filter').filter('toHumanReadable', function () {
    return function (input) {
        var text = input;
        if (text) {
            // First Char
            text = text.charAt(0).toUpperCase() + text.substr(1);
            // Underline
            text = text.replace(/([_])/g, ' ');

            // Camel Case
            // example 1: ThisIsSomeText
            var regex = /([a-z])([A-Z])/g;
            var replaceFn = function (s, group0, group1) {
                return group0 + ' ' + group1;
            };
            // example 2: ABadExample
            text = text.replace(regex, replaceFn);
            regex = /([A-Z])([A-Z])([a-z])/g;
            replaceFn = function (s, group0, group1, group2) {
                return group0 + ' ' + group1 + group2;
            };
            text = text.replace(regex, replaceFn);
        }
        return text;
    };
});
