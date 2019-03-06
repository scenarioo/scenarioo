angular.module('scenarioo.services')

    .factory('PageVariantService', (ScenariooResource, $q) => {
        function getPromise(fn) {
            return (parameters) => {
                const deferred = $q.defer();
                fn(parameters, (result) => {
                    deferred.resolve(result);
                }, (error) => {
                    deferred.reject(error);
                });
                return deferred.promise;
            };
        }

        const pageVariantService = ScenariooResource('/branch/:branchName/build/:buildName/search/pagevariants/',
            {
                branchName: '@branchName',
                buildName: '@buildName',
            }, {});

        pageVariantService.getPageVariantCount = getPromise((parameters, fnSuccess, fnError) => pageVariantService.get(parameters, fnSuccess, fnError));
        return pageVariantService;
    });
