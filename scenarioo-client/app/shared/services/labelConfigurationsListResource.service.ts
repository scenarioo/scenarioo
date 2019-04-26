angular.module('scenarioo.services')
    .factory('LabelConfigurationsListResource', (ScenariooResource) => {
        return ScenariooResource('/labelconfigurations/list', {}, {});
    });
