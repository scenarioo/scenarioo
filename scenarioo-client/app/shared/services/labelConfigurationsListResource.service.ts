angular.module('scenarioo.services')
    .factory('LabelConfigurationsListResource', function (ScenariooResource) {
        return ScenariooResource('/labelconfigurations/list', {}, {});
    });
