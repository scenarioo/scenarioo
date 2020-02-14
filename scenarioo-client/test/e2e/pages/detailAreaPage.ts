'use strict';

import {by, element} from 'protractor';

class DetailAreaPage {

    private collapseButton = element(by.id('sc-showHideDetailsButton'));
    private detailsContentPanel = element(by.css('sc-detailarea .details-content'));

    async assertDetailsExpanded() {
        await expect(this.collapseButton.getText()).toEqual('Hide Details');
        return expect(this.detailsContentPanel.isDisplayed()).toBe(true);
    }

    async assertDetailsCollapsed() {
        await expect(this.collapseButton.getText()).toEqual('Show Details');
        return expect(this.detailsContentPanel.isDisplayed()).toBe(false);
    }

    async expandDetails() {
        return this.collapseButton.click();
    }

    async collapseDetails() {
        return this.collapseButton.click();
    }

    async assertSectionExpanded(sectionName) {
        const section = element(by.id('sc-section-' + sectionName));
        return expect(section.getAttribute('aria-expanded')).toEqual('true');
    }

    async assertSectionCollapsed(sectionName) {
        const section = element(by.id('sc-section-' + sectionName));
        return expect(section.getAttribute('aria-expanded')).toEqual('false');
    }

    async expandSection(sectionName) {
        await this.assertSectionCollapsed(sectionName);
        const toggleSection = element(by.id('sc-toggleSection-' + sectionName));
        return await toggleSection.click();
    }

    async collapseSection(sectionName) {
        await this.assertSectionExpanded(sectionName);
        const toggleSection = element(by.id('sc-toggleSection-' + sectionName));
        return await toggleSection.click();
    }
}

export default new DetailAreaPage();
