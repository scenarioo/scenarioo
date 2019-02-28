angular.module('scenarioo.services')
    .factory('LabelConfigurationsResource', function (ScenariooResource) {
        return ScenariooResource('/labelconfigurations', {}, {'query': {isArray: false}});
    });
