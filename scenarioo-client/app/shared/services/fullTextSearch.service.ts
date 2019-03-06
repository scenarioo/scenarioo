angular.module('scenarioo.services')

    .factory('FullTextSearchService', (ScenariooResource, $q) => {
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

        const searchService = ScenariooResource('/branch/:branchName/build/:buildName/search?q=:q',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                q: '@q',
                includeHtml: '@includeHtml',
            }, {});

        searchService.search = getPromise((parameters, fnSuccess, fnError) => searchService.get(parameters, fnSuccess, fnError));

        return searchService;
    });
