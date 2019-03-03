angular.module('scenarioo.services')

    .factory('PageVariantService', function (ScenariooResource, $q) {
        function getPromise($q, fn) {
            return function (parameters) {
                var deferred = $q.defer();
                fn(parameters, function (result) {
                    deferred.resolve(result);
                }, function (error) {
                    deferred.reject(error);
                });
                return deferred.promise;
            };
        }

        var pageVariantService = ScenariooResource('/branch/:branchName/build/:buildName/search/pagevariants/',
            {
                branchName: '@branchName',
                buildName: '@buildName'
            }, {});

        pageVariantService.getPageVariantCount = getPromise($q, function (parameters, fnSuccess, fnError) {
            return pageVariantService.get(parameters, fnSuccess, fnError);
        });
        return pageVariantService;
    })
