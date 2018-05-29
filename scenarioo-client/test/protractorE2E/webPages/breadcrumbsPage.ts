'use strict';
import {by, element, ElementFinder} from "protractor";

var util = require('util');

export default class BreadcrumbPage {

    private path: string = "/";
    private breadcrumbs: ElementFinder = element(by.css('.breadcrumb'));

    async clickOnBreadcrumb(breadcrumbId) {
        this.breadcrumbs.element(by.id(breadcrumbId)).click();
    };

    async assertBreadcrumbElementText(breadcrumbId, useCaseName) {
        var useCaseElement =  this.breadcrumbs.element(by.id(breadcrumbId));
        expect(useCaseElement.getText()).toContain(useCaseName);
    };

    async assertThatTooltipIsShown(toolTipId, toolTipText){
        var toolTipElement =  this.breadcrumbs.element(by.id(toolTipId));
        var toolTipAttribute = toolTipElement.getAttribute('tooltip');

        expect(toolTipAttribute).toBe(toolTipText);
    };

}

