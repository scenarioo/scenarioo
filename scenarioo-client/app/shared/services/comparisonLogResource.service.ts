angular.module('scenarioo.services')
    .factory('ComparisonLogResource', (ScenariooResource) => {
        return ScenariooResource('/builds/:branchName/:buildName/comparisons/:comparisonName/log',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                comparisonName: '@comparisonName',
            }, {
                get: {
                    method: 'GET',
                    headers: {Accept: 'text/plain'},
                    transformResponse(data) {
                        return {content: data};
                    },
                },
            });
    });
