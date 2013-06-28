'use strict';

NgUsdClientApp.filter('camelCase', function() {
    return function(input) {
        return input.charAt(0).toUpperCase() + input.substr(1).replace(/([A-Z])/g, function(s, group1) {
            return " "+group1.toUpperCase();
        });
    }
});