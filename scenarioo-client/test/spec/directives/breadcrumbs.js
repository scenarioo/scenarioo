'use strict';

describe('Directive :: scBreadcrumbs', function () {
    var scope,
        compile,
        location,
        httpBackend,
        elem,
        html;

    // load module
    beforeEach(module('scenarioo.directives'));

    beforeEach(inject(function ($rootScope, $compile, $location, $route, $httpBackend) {
        // create a scope
        scope = $rootScope.$new();
        $rootScope.$apply();
        $httpBackend.expectGET('template/breadcrumbs.html').respond('<some-dummy-template></some-dummy-template>');

        httpBackend = $httpBackend;
        compile = $compile;
        location = $location;

        // mock routes
        $route.routes = {
            '/': { breadcrumb: 'Home' },
            '/usecase/:uid': { breadcrumb: '<strong>Usecase</strong>: $param' },
            '/scenario/:uid/:sid': { breadcrumb: 'Scenario: $title' }
        };

        // mock title
        scope.title = 'thisIsSomeTitle';
    }));

    function breadcrumbWithPath(path) {
        // mock location
        location.path(path);

        html = '<sc-Breadcrumb></sc-Breadcrumb>';
        elem = compile(html)(scope);
        httpBackend.flush();

        //call digest on the scope!
        scope.$digest();
    }

    it('Should consist only of the root breadcrumb', function () {
        breadcrumbWithPath('/');

        var innerScope = elem.scope();
        var breadcrumbs = innerScope.breadcrumbs;

        expect(breadcrumbs.length).toBe(1);

        // One root breadcrumb
        expect(breadcrumbs[0]).toEqual({
            text: 'Home',
            showTooltip: false,
            href: '#',
            isLast: true,
            tooltip: jasmine.any(String)
        });

        // Mail information
        expect(innerScope.email.title).toBeDefined();
        expect(innerScope.email.link).toBeDefined();
    });

    it('Should consist of multiple breadcrumbs', function () {
        breadcrumbWithPath('/scenario/usecaseId/scenarioId');

        var innerScope = elem.scope();
        var breadcrumbs = innerScope.breadcrumbs;

        expect(breadcrumbs.length).toBe(3);

        // Root breadcrumb
        expect(breadcrumbs[0]).toEqual({
            text: 'Home',
            showTooltip: false,
            href: '#',
            isLast: false,
            tooltip: jasmine.any(String)
        });

        // Usecase breadcrumb with $param
        expect(breadcrumbs[1]).toEqual({
            text: '<strong>Usecase</strong>: Usecase Id',
            showTooltip: false,
            href: '#/usecase/usecaseId',
            isLast: false,
            tooltip: jasmine.any(String)
        });

        // Scenario breadcrumb with $title
        expect(breadcrumbs[2]).toEqual({
            text: 'Scenario: thisIsSomeTitle',
            showTooltip: false,
            href: '#/scenario/usecaseId/scenarioId',
            isLast: true,
            tooltip: jasmine.any(String)
        });
    });

    it('Should consist of multiple breadcrumbs', function () {
        breadcrumbWithPath('/scenario/usecaseIdWithAMuchTooLongNameWhichWillBeDisplayedInATooltipAndTheTooltipIsStrippedOfHTML/scenarioId');

        var innerScope = elem.scope();
        var breadcrumbs = innerScope.breadcrumbs;

        // Usecase breadcrumb with too long name and tooltip which is stripped of HTML tags
        expect(breadcrumbs[1]).toEqual({
            text: '<strong>Usecase</strong>: Usecase Id With A Much T..',
            showTooltip: true,
            tooltip: 'Usecase: Usecase Id With A Much Too Long Name Which Will Be Displayed In A Tooltip And The Tooltip Is Stripped Of HTML',
            href: '#/usecase/usecaseIdWithAMuchTooLongNameWhichWillBeDisplayedInATooltipAndTheTooltipIsStrippedOfHTML',
            isLast: false
        });
    });
});
