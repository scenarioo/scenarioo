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
import {Injectable} from '@angular/core';
import {HumanReadablePipe} from '../../../pipes/humanReadable.pipe';

/**
 * Configure breadcrumb elements for different objects that can occur in a concrete breadcrumb path for a page
 */

const homeElement: BreadcrumbElement = {
    label: '<i class="fas fa-home"></i> Home',
    route: 'build/build.html', // maybe better rename to linkUrl
};

const manageElement: BreadcrumbElement = {
    label: '<i class="fas fa-cogs"></i> Manage',
    route: 'manage/manage.html',
};

const useCaseElement: BreadcrumbElement = {
    label: '<strong>Use Case:</strong> [usecase]',
    route: '/usecase/:usecase/',
};

const scenarioElement: BreadcrumbElement = {
    label: '<strong>Scenario:</strong> [scenario]',
    route: '/scenario/:usecase/:scenario/',
};

const stepElement: BreadcrumbElement = {
    label: '<strong>Step:</strong> [pageName]/[pageOccurrence]/[stepInPageOccurrence]',
    route: '/step/:usecase/:scenario/:pageName/:pageOccurrence/:stepInPageOccurrence/',
};

const objectElement: BreadcrumbElement = {
    label: '<strong>[objectType]:</strong> [objectName]',
    route: '/object/:objectType/:objectName',
};

const stepSketchElement: BreadcrumbElement = {
    label: '<strong>Sketch</strong>',
};

const searchElement: BreadcrumbElement = {
    label: '<strong>Search Results for [searchTerm]</strong>',
};

interface BreadcrumbElement {
    label: string;
    route?: string;
}

interface NavigationElement extends BreadcrumbElement {
    isLastNavigationElement?: boolean;
    textForTooltip?: string;
}

interface BreadcrumbDeclaration {
    breadcrumbPath: BreadcrumbElement[];
}

type BreadcrumbId = string;

/**
 *  Configure breadcrumb paths that can be assigned to routes (see app.js) to display them as breadcrumbs for according pages.
 *  Key of the elements is the 'breadcrumbId', use it to link one of this path to a routing in app.js
 */

type BreadcrumbTypeMap = Record<BreadcrumbId, BreadcrumbDeclaration>;
const breadcrumbPaths: BreadcrumbTypeMap = {
    usecase: {
        breadcrumbPath: [homeElement, useCaseElement],
    },

    scenario: {
        breadcrumbPath: [homeElement, useCaseElement, scenarioElement],
    },

    step: {
        breadcrumbPath: [homeElement, useCaseElement, scenarioElement, stepElement],
    },

    main: {
        breadcrumbPath: [homeElement],
    },

    object: {
        breadcrumbPath: [homeElement, objectElement],
    },

    manage: {
        breadcrumbPath: [homeElement, manageElement],
    },

    stepsketch: {
        breadcrumbPath: [homeElement, stepSketchElement],
    },

    search: {
        breadcrumbPath: [homeElement, searchElement],
    },
};

@Injectable()
export class BreadcrumbsService {
    constructor(private humanReadable: HumanReadablePipe) {
    }
    public getNavigationElements(breadcrumbId, navParameters): NavigationElement[] {
        if (breadcrumbId === undefined || navParameters === undefined) {
            return undefined;
        }

        const breadcrumbDeclaration = breadcrumbPaths[breadcrumbId];
        const lastIndex = breadcrumbDeclaration.breadcrumbPath.length - 1;

        if (breadcrumbDeclaration !== undefined) {
            return breadcrumbDeclaration.breadcrumbPath.map((breadcrumbElement, index) => {
                const navigationElement: NavigationElement = {
                    ...breadcrumbElement,
                };
                navigationElement.isLastNavigationElement = lastIndex === index;
                if (navigationElement.route) {
                    navigationElement.route = this.setValuesInRoute(navigationElement.route, navParameters);
                }
                navigationElement.label = this.setValuesInLabel(navigationElement.label, navParameters);
                navigationElement.textForTooltip = this.convertToPlainText(navigationElement.label);
                return navigationElement;
            });
        }
        return [];
    }

    private convertToPlainText(text) {
        return text.replace(/<\/?[^>]+(>|$)/g, '');
    }

    private setValuesInRoute(text, navParameter) {
        const placeholders = text.match(/:.*?[^<](?=\/)/g);

        if (placeholders !== null) {
            placeholders.forEach((placeholder) => {
                placeholder = placeholder.replace(':', '');
                if (placeholder === 'usecase' || placeholder === 'scenario' || placeholder === 'issueId' || placeholder === 'scenarioSketchId') {
                    text = text.replace(':' + placeholder, navParameter[placeholder]);
                }
            });
        }
        return text;
    }

    private getText(navParameter, placeholder) {
        const value = navParameter[placeholder];

        if (placeholder === 'usecase' || placeholder === 'scenario') {
            return this.humanReadable.transform(value);
        }

        return value;
    }

    private setValuesInLabel(text, navParameter) {
        const placeholders = text.match(/\[.*?(?=])./g);

        if (placeholders !== null) {
            placeholders.forEach((placeholder) => {
                placeholder = placeholder.replace(/[\[\]]/g, '');
                text = text.replace(/[\[\]]/g, '');
                text = text.replace(placeholder, this.getText(navParameter, placeholder));
            });
            text = decodeURIComponent(text);
        }
        return text;
    }
}
