angular.module('scenarioo.services')
    .factory('ComparisonRecalculateResource', (ScenariooResource) => {
        return ScenariooResource('/builds/:branchName/:buildName/comparisons/:comparisonName/recalculate',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                comparisonName: '@comparisonName',
            },
            {
                post: {
                    method: 'POST',
                },
            });
    });
