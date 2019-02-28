
angular.module('scenarioo.services')
    .factory('ObjectListResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/object/:objectType',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                objectType: '@objectType'
            }, {});
    });
