
angular.module('scenarioo.services')
    .factory('CustomTabContentResource', function (ScenariooResource) {
        return ScenariooResource('/branches/:branchName/builds/:buildName/customTabObjects/:tabId',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                tabId: '@tabId'
            }, {});
    });
