angular.module('scenarioo.services')
    .factory('ObjectIndexListResource', (ScenariooResource) => {
        return ScenariooResource('/branch/:branchName/build/:buildName/object/:objectType/name?name=:objectName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                objectType: '@objectType',
                objectName: '@objectName',
            }, {});
    });
