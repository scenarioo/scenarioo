/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';
// TODO #277: refactor unit tests according to #267 and #310
/*
describe('Directive :: scBreadcrumbss', function () {
    var scope,
        compile,
        location,
        httpBackend,
        elem,
        html;

    // load module's
    beforeEach(module('scenarioo.directives'));
    beforeEach(module('scenarioo.services'));

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
            '/main': { },
            '/feature/:uid': { },
            '/scenario/:uid/:sid': { }
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

    function expectBreadCrumbToEqual(breadCrumb, expected) {
        var copiedBC = angular.copy(breadCrumb);
        copiedBC.text = copiedBC.text.$$unwrapTrustedValue();
        expect(copiedBC).toEqual(expected);
    }

    it('1. Should consist only of the root breadcrumb', function () {
        breadcrumbWithPath('/');

        var innerScope = elem.scope();
        var breadcrumbs = innerScope.breadcrumbs;

        expect(breadcrumbs.length).toBe(1);

        // One root breadcrumb
        expectBreadCrumbToEqual(breadcrumbs[0], {
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

    it('2. Should consist of multiple breadcrumbs', function () {
        breadcrumbWithPath('/scenario/featureId/scenarioId');

        var innerScope = elem.scope();
        var breadcrumbs = innerScope.breadcrumbs;

        expect(breadcrumbs.length).toBe(3);

        // Root breadcrumb
        expectBreadCrumbToEqual(breadcrumbs[0], {
            text: 'Home',
            showTooltip: false,
            href: '#',
            isLast: false,
            tooltip: jasmine.any(String)
        });


        // Usecase breadcrumb with $param
        expectBreadCrumbToEqual(breadcrumbs[1], {
            text: '<strong>Use Case: </strong>',
            showTooltip: false,
            href: '#/feature/featureId',
            isLast: false,
            tooltip: jasmine.any(String)
        });

        // Scenario breadcrumb with $title
        expectBreadCrumbToEqual(breadcrumbs[2], {
            text: 'Scenario: thisIsSomeTitle',
            showTooltip: false,
            href: '#/scenario/featureId/scenarioId',
            isLast: true,
            tooltip: jasmine.any(String)
        });
    });

    it('3. Should consist of multiple breadcrumbs', function () {
        breadcrumbWithPath('/scenario/featureIdWithAMuchTooLongNameWhichWillBeDisplayedInATooltipAndTheTooltipIsStrippedOfHTML/scenarioId');

        var innerScope = elem.scope();
        var breadcrumbs = innerScope.breadcrumbs;

        // Usecase breadcrumb with too long name and tooltip which is stripped of HTML tags
        expectBreadCrumbToEqual(breadcrumbs[1], {
            text: '<strong>Usecase</strong>: Usecase Id With A Much T..',
            showTooltip: true,
            tooltip: 'Usecase: Usecase Id With A Much Too Long Name Which Will Be Displayed In A Tooltip And The Tooltip Is Stripped Of HTML',
            href: '#/feature/featureIdWithAMuchTooLongNameWhichWillBeDisplayedInATooltipAndTheTooltipIsStrippedOfHTML',
            isLast: false
        });
    });
});
*/
