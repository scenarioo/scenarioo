angular.module('scenarioo.services')
    .factory('ObjectsForTypeResource', (ScenariooResource) => {
        return ScenariooResource('/branches/:branchName/builds/:buildName/objects/service',
            {
                branchName: '@branchName',
                buildName: '@buildName',
            }, {});
    });
