'use strict';

beforeEach(function() {
    this.addMatchers({
        toEqualData: function(expect) {
            return angular.equals(expect, this.actual);
        }
    });
});