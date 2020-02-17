export function addRoutes($routeProvider) {
    $routeProvider
        .when('/', {
            template: require('./build/build.html'),
        })
        .when('/manage', {
            template: require('./manage/manage.html'),
            controllerAs: 'vm',
        })
        .when('/usecase/:useCaseName', {
            template: require('./useCase/usecase.html'),
            useCaseName: '@useCaseName',
        })
        .when('/scenario/:useCaseName/:scenarioName', {
            template: require('./scenario/scenario.html'),
            controller: 'ScenarioController',
            controllerAs: 'vm',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
        })
        .when('/search/:searchTerm', {
            template: require('./search/search.html'),
            controller: 'SearchController',
            controllerAs: 'vm',
            searchTerm: '@searchTerm',
        })
        .when('/object/:objectType/:objectName', {
            template: require('./objectRepository/objectRepository.html'),
            controller: 'ObjectRepositoryController',
            controllerAs: 'vm',
            objectType: '@objectType',
            objectName: '@objectName',
        })
        .when('/step/:useCaseName/:scenarioName/:pageName/:pageOccurrence/:stepInPageOccurrence', {
            template: require('./step/step.html'),
            controller: 'StepController',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
            pageName: '@pageName',
            pageOccurrence: '@pageOccurrence',
            stepInPageOccurrence: '@stepInPageOccurrence',
        })
        .when('/stepsketch/:issueId/:scenarioSketchId/:stepSketchId', {
            template: require('./sketcher/stepSketch.html'),
            controller: 'StepSketchController',
            controllerAs: 'vm',
        })
        .when('/editor', {
            template: require('./sketcher/sketcherEditor.html'),
            controller: 'SketcherEditorController',
            controllerAs: 'vm',
        })
        .when('/editor/:issueId/:scenarioSketchId/:stepSketchId', {
            template: require('./sketcher/sketcherEditor.html'),
            controller: 'SketcherEditorController',
            controllerAs: 'vm',
            issueId: '@issueId',
            scenarioSketchId: '@scenarioSketchId',
            stepSketchId: '@stepSketchId',
        })
        .otherwise({
            redirectTo: '/',
        });
}
