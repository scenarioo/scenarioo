angular.module('scenarioo.services')
    .factory('ComparisonsResource', (ScenariooResource) => {
        return ScenariooResource('/comparisons', {}, {});
    });
