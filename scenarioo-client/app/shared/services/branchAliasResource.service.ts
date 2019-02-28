angular.module('scenarioo.services')
    .factory('BranchAliasesResource', function (ScenariooResource) {
        return ScenariooResource('/branchaliases', {}, {});
    });
