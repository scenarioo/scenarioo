function addRoutes($routeProvider) {
    /**
     * breadcrumbId: id of the breadcrumb elements to use for this page as defined in breadcrumbs.service.js
     */
    $routeProvider
        .when('/', {
            template: require('./build/build.html'),
            controller: 'BuildController',
            controllerAs: 'main',
            breadcrumbId: 'main'
        })
        .when('/manage', {
            template: require('./manage/manage.html'),
            controller: 'ManageController',
            controllerAs: 'vm',
            breadcrumbId: 'manage'
        })
        .when('/usecase/:useCaseName', {
            template: require('./useCase/usecase.html'),
            controller: 'UseCaseController',
            controllerAs: 'useCase',
            useCaseName: '@useCaseName',
            breadcrumbId: 'usecase'
        })
        .when('/scenario/:useCaseName/:scenarioName', {
            template: require('./scenario/scenario.html'),
            controller: 'ScenarioController',
            controllerAs: 'vm',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
            breadcrumbId: 'scenario'
        })
        .when('/search/:searchTerm', {
            template: require('./search/search.html'),
            controller: 'SearchController',
            controllerAs: 'vm',
            breadcrumbId: 'search',
            searchTerm: '@searchTerm'
        })
        .when('/object/:objectType/:objectName', {
            template: require('./objectRepository/objectRepository.html'),
            controller: 'ObjectRepositoryController',
            controllerAs: 'vm',
            objectType: '@objectType',
            objectName: '@objectName',
            breadcrumbId: 'object'
        })
        .when('/step/:useCaseName/:scenarioName/:pageName/:pageOccurrence/:stepInPageOccurrence', {
            template: require('./step/step.html'),
            controller: 'StepController',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName',
            pageName: '@pageName',
            pageOccurrence: '@pageOccurrence',
            stepInPageOccurrence: '@stepInPageOccurrence',
            breadcrumbId: 'step'
        })
        .when('/stepsketch/:issueId/:scenarioSketchId/:stepSketchId', {
            template: require('./sketcher/stepSketch.html'),
            controller: 'StepSketchController',
            controllerAs: 'vm',
            breadcrumbId: 'stepsketch'
        })
        .when('/editor', {
            template: require('./sketcher/sketcherEditor.html'),
            controller: 'SketcherEditorController',
            controllerAs: 'vm'
        })
        .when('/editor/:issueId/:scenarioSketchId/:stepSketchId', {
            template: require('./sketcher/sketcherEditor.html'),
            controller: 'SketcherEditorController',
            controllerAs: 'vm',
            issueId: '@issueId',
            scenarioSketchId: '@scenarioSketchId',
            stepSketchId: '@stepSketchId'
        })
        .otherwise({
            redirectTo: '/'
        });
}

module.exports = addRoutes;
