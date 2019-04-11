'use strict';

import {by, element} from 'protractor';

class ComparisonDetailLogDialog {

    async assertTitle(expectedTitle) {
        return expect(element(by.tagName('h1')).getText()).toBe(expectedTitle);
    }

    async assertBuildDetails(expectedBranch, expectedBuild) {
        return this.assertDetails('buildDetail', expectedBranch, expectedBuild);
    }

    async assertComparedBuildDetails(expectedBranch, expectedBuild) {
        return this.assertDetails('comparedBuildDetail', expectedBranch, expectedBuild);
    }

    async assertLogContains(expectedText) {
        return expect(element(by.id('comparisonLog')).getText()).toContain(expectedText);
    }

    private async assertDetails(elementId: string, expectedBranch, expectedBuild) {
        await expect(element(by.id(elementId)).getText()).toContain('Branch: ' + expectedBranch);
        return expect(element(by.id(elementId)).getText()).toContain('Build: ' + expectedBuild);
    }
}

export default new ComparisonDetailLogDialog();
