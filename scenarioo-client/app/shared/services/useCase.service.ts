angular.module('scenarioo.services')

    .factory('UseCaseService', function (ScenariooResource, $q) {
        var useCaseService = ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName'
            }, {});

        useCaseService.getUseCase = getPromise($q, function (parameters, fnSuccess, fnError) {
            return useCaseService.get(parameters, fnSuccess, fnError);
        });
        return useCaseService;
    })

