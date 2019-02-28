angular.module('scenarioo.services')
    .factory('ScenarioResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName/scenario/:scenarioName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName',
                scenarioName: '@scenarioName'
            }, {});
    });
