'use strict';

import { by, ElementFinder, $ } from 'protractor';

class BreadcrumbPage {

    private breadcrumbs: ElementFinder = $('.breadcrumb');

    async clickOnBreadcrumb(breadcrumbId) {
        return this.breadcrumbs.element(by.id(breadcrumbId)).click();
    }

    async assertBreadcrumbElementText(breadcrumbId, useCaseName) {
        const useCaseElement =  this.breadcrumbs.element(by.id(breadcrumbId));
        return expect(useCaseElement.getText()).toContain(useCaseName);
    }

    async assertThatTooltipIsShown(toolTipId, toolTipText) {
        const toolTipElement =  this.breadcrumbs.element(by.id(toolTipId));
        const toolTipAttribute = await toolTipElement.getAttribute('uib-tooltip');

        return expect(toolTipAttribute).toBe(toolTipText);
    }

}

export default new BreadcrumbPage();
