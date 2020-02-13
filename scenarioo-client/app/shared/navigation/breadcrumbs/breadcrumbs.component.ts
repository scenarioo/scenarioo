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

import {Component, Input, OnInit} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {DomSanitizer} from '@angular/platform-browser';
import * as $ from 'jquery';
import {SketcherLinkService} from './sketcherLink.service';
import {BreadcrumbsService} from './breadcrumbs.service';
import {RouteParamsService} from '../../route-params.service';

declare var angular: angular.IAngularStatic;

interface BreadcrumbInfo {
    text: string;
    tooltip: string;
    showTooltip: boolean;
    href: string;
    isLast: boolean;
}

interface BreadcrumbParams {
    usecase: string;
    scenario: string;
    searchTerm: string;
    objectType: string;
    objectName: string;
    pageName: string;
    pageOccurrence: number;
    stepInPageOccurrence: number;
}

@Component({
    selector: 'sc-breadcrumbs',
    template: require('./breadcrumbs.component.html'),
})
export class Breadcrumbs implements OnInit {
    limit = 50;
    breadcrumbs: BreadcrumbInfo[];
    @Input()
    breadcrumbId: string;

    constructor(private sketcherLink: SketcherLinkService, private breadcrumbsService: BreadcrumbsService, private routeParamsService: RouteParamsService, private sanitizer: DomSanitizer) {

    }

    ngOnInit(): void {
        const breadcrumbId = this.breadcrumbId;
        const navParameters = this.getNavigationParameters();

        const navElements = this.breadcrumbsService.getNavigationElements(breadcrumbId, navParameters);
        this.breadcrumbs = navElements.map((breadcrumbItem) => {
            const isLabelTextShortened = breadcrumbItem.label.length > this.limit && !breadcrumbItem.isLastNavigationElement;
            const breadcrumbLabelText = this.getShortenedLabelText(breadcrumbItem, isLabelTextShortened);
            return {
                text: breadcrumbLabelText,
                tooltip: breadcrumbItem.textForTooltip,
                showTooltip: isLabelTextShortened,
                href: `#${breadcrumbItem.route}`,
                isLast: breadcrumbItem.isLastNavigationElement,
            };
        });
    }

    private getShortenedLabelText(breadcrumbItem, isLabelTextShortened): string {
        return isLabelTextShortened ? this.getShortenedText(breadcrumbItem.label) : breadcrumbItem.label;
    }

    private getShortenedText(text): string {
        if (text.length > this.limit) {
            const shortenedText = text.substr(0, this.limit);
            return shortenedText + '...';
        }
        return text;
    }

    private getNavigationParameters(): BreadcrumbParams {
        return {
            usecase: this.routeParamsService.useCaseName,
            scenario: this.routeParamsService.scenarioName,
            pageName: this.routeParamsService.pageName,
            pageOccurrence: parseInt(this.routeParamsService.pageOccurrence, 10),
            stepInPageOccurrence: parseInt(this.routeParamsService.stepInPageOccurrence, 10),
            objectType: this.routeParamsService.objectType,
            objectName: this.routeParamsService.objectName,
            // displaying the searchTerm can be handled properly as soon as search.controller.js is migrated
            // double decoding is necessary due to double encoding in URL, see https://github.com/angular/angular.js/issues/10479
            searchTerm: $('<div/>').text(decodeURIComponent(decodeURIComponent(this.routeParamsService.searchTerm))).html(),
        };
    }
}

angular.module('scenarioo.directives')
    .directive('scBreadcrumbs',
        downgradeComponent({component: Breadcrumbs}) as angular.IDirectiveFactory);
