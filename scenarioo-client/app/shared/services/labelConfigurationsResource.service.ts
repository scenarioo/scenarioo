angular.module('scenarioo.services')
    .factory('LabelConfigurationsResource', (ScenariooResource) => {
        return ScenariooResource('/labelconfigurations', {}, {query: {isArray: false}});
    });
