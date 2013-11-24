'use strict'

jasmine.Matchers.prototype.toEqualData = function(expect) {
    return angular.equals(expect, this.actual);
}