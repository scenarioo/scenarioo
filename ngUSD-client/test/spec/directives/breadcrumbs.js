'use strict';

describe('Directive: Breadcrumbs', function () {
    var scope,
        compile,
        location,
        route,
        elem,
        html;

    // load module
    beforeEach(module('ngUSDClientApp.directives'));

    beforeEach(inject(function ($rootScope, $compile, $location, $route, $templateCache) {
        // create a scope
        scope = $rootScope.$new();
        $rootScope.$apply();
        $templateCache.put('template/breadcrumbs.html', '<div class="row-fluid breadcrumb-row"><div class="breadcrumb-div"><ul class="breadcrumb"></ul></div><div class="pull-right email-div"><a href="mailto:?subject=ngUSD:%20{{emailTitle}}&body={{emailLink}}"><i class="icon-envelope icon-with-padding"></i>Email this page</a></div></div>');

        scope = $rootScope;
        compile = $compile;
        route = $route;
        location = $location;
    }));


    it('Should create breadcrumbs', function() {
        // mock location
        location.path('/new/path');

        // mock routes
        route.routes = ['/new/:id'];

        html = '<div><usd-breadcrumb></usd-breadcrumb></div>';

        elem = compile(html)(scope);

        //call digest on the scope!
        scope.$digest();

        console.log(elem.html());
        expect(elem.html()).toBe('asds');
    });

    afterEach(function() {
    });
});
