angular.module('scenarioo.services')
    .factory('ConfigResource', function (ScenariooResource) {
        return ScenariooResource('/configuration', {});
    });

