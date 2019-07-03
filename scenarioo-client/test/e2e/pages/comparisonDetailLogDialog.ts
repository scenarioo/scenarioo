'use strict';

import {$, $$, by, element} from 'protractor';

class ComparisonDetailLogDialog {

    async assertTitle(expectedTitle: string) {
        return expect($('h1').getText()).toBe(expectedTitle);
    }

    async assertBuildDetails(expectedBranch: string, expectedBuild: string) {
        return this.assertDetails('#buildDetail', expectedBranch, expectedBuild);
    }

    async assertComparedBuildDetails(expectedBranch: string, expectedBuild: string) {
        return this.assertDetails('#comparedBuildDetail', expectedBranch, expectedBuild);
    }

    async assertLogContains(expectedText: string) {
        return expect($$('#comparisonLog').first().getText()).toContain(expectedText);
    }

    private async assertDetails(elementId: string, expectedBranch: string , expectedBuild: string) {
        await expect($$(elementId).first().getText()).toContain('Branch: ' + expectedBranch);
        return expect($$(elementId).first().getText()).toContain('Build: ' + expectedBuild);
    }
}

export default new ComparisonDetailLogDialog();
