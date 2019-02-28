angular.module('scenarioo.services')
    .factory('VersionResource', function (ScenariooResource) {
        return ScenariooResource('/version', {}, {});
    });
