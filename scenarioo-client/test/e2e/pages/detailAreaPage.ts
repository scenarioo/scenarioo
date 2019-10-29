'use strict';

import {by, element} from 'protractor';

class DetailAreaPage {

    private toggleMetaDataButton = element(by.id('sc-showHideDetailsButton'));
    private metaDataPanel = element(by.id('sc-metadata-panel'));

    async assertDetailsAreaExpanded() {
        await expect(this.toggleMetaDataButton.getText()).toEqual('Hide details');
        return expect(this.metaDataPanel.isDisplayed()).toBe(true);
    }

    async assertDetailsAreaCollapsed() {
        await expect(this.toggleMetaDataButton.getText()).toEqual('Show details');
        return expect(this.metaDataPanel.isDisplayed()).toBe(false);
    }

    async expandDetailsArea() {
        return this.toggleMetaDataButton.click();
    }

    async collapseDetailsArea() {
        return this.toggleMetaDataButton.click();
    }

    async assertSectionExpanded(detailsSection) {
        const section = element(by.id('sc-section-' + detailsSection));
        return expect(section.getAttribute('aria-expanded')).toEqual('true');
    }

    async assertSectionCollapsed(detailsSection) {
        const section = element(by.id('sc-section-' + detailsSection));
        return expect(section.getAttribute('aria-expanded')).toEqual('false');
    }

    async expandDetailsSection(detailsSection) {
        await this.assertSectionCollapsed(detailsSection);
        const toggleSection = element(by.id('sc-toggleSection-' + detailsSection));
        return await toggleSection.click();
    }

    async collapseDetailsSection(detailsSection) {
        await this.assertSectionExpanded(detailsSection);
        const toggleSection = element(by.id('sc-toggleSection-' + detailsSection));
        return await toggleSection.click();
    }
}

export default new DetailAreaPage();
