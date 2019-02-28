angular.module('scenarioo.services')
    .factory('ComparisonsResource', function (ScenariooResource) {
        return ScenariooResource('/comparisons', {}, {});
    });
