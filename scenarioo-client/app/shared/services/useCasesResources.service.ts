angular.module('scenarioo.services')
    .factory('UseCasesResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName'
            }, {});
    });
