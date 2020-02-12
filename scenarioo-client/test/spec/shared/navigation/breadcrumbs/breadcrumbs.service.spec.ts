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

import {BreadcrumbsService} from '../../../../../app/shared/navigation/breadcrumbs/breadcrumbs.service';
import {HumanReadablePipe} from '../../../../../app/pipes/humanReadable.pipe';

describe('BreadcrumbsService', () => {

    let breadcrumbsService: BreadcrumbsService;

    const NAV_PARAMETERS_FOR_STEP = {
        usecase: 'search_book',
        scenario: 'SearchBookThatDoesNotExist',
        pageName: 'search_results',
        pageOccurrence: 0,
        stepInPageOccurrence: 1,
    };

    const NAVIGATION_ELEMENTS_FOR_STEP = [
        { label: '<i class="fas fa-home"></i> Home', route: 'build/build.html', isLastNavigationElement: false, textForTooltip: ' Home' },
        { label: '<strong>Use Case:</strong> Search book', route: '/usecase/search_book/', isLastNavigationElement: false, textForTooltip: 'Use Case: Search book' },
        { label: '<strong>Scenario:</strong> Search Book That Does Not Exist', route: '/scenario/search_book/SearchBookThatDoesNotExist/', isLastNavigationElement: false, textForTooltip: 'Scenario: Search Book That Does Not Exist' },
        { label: '<strong>Step:</strong> search_results/0/1', route: '/step/search_book/SearchBookThatDoesNotExist/:pageName/:pageOccurrence/:stepInPageOccurrence/', isLastNavigationElement: true, textForTooltip: 'Step: search_results/0/1' },
    ];

    beforeEach(() => {
        breadcrumbsService = new BreadcrumbsService(new HumanReadablePipe());
    });

    it('getNavigationElements with undefined parameters returns undefined',  () => {
        expect(breadcrumbsService.getNavigationElements(undefined, undefined)).toBeUndefined();
    });

    it('loadNavigationElements returns a path with four elements for a step', () => {
        const navigationElements = breadcrumbsService.getNavigationElements('step', NAV_PARAMETERS_FOR_STEP);

        expect(breadcrumbsService.getNavigationElements('step', NAV_PARAMETERS_FOR_STEP)).toEqual(NAVIGATION_ELEMENTS_FOR_STEP);
        expectUseCaseAndScenarioAreHumanReadable(navigationElements);
        expectStepIsNotMadeHumanReadable(navigationElements);
    });

    function expectStepIsNotMadeHumanReadable(navigationElements) {
        expect(navigationElements[3].label).toEqual('<strong>Step:</strong> search_results/0/1');
    }

    function expectUseCaseAndScenarioAreHumanReadable(navigationElements) {
        expect(navigationElements[1].label).toEqual('<strong>Use Case:</strong> Search book');
        expect(navigationElements[2].label).toEqual('<strong>Scenario:</strong> Search Book That Does Not Exist');
    }

});
