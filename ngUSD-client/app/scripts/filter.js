'use strict';

NgUsdClientApp.filter('toHumanReadable', function() {
    return function(input) {
        var text = input;
        if (text) {
            // First Char
            text = text.charAt(0).toUpperCase() + text.substr(1);
            // Underline
            text = text.replace(/([_])/g, " ");
            // Camel Case
            text = text.charAt(0) + text.substr(1).replace(/([A-Z])([a-z])/g, function(s, group1, group2) {
                return " "+group1.toUpperCase()+group2;
            });
        }
        return text;
    }
});