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

angular.module('scenarioo.services').service('ScreenshotUrlService', ScreenshotUrlService);

function ScreenshotUrlService() {

    function getDiffScreenShotUrl(step, selected, comparisonName, useCaseName, scenarioName, stepIndex) {
        if (step && stepIndex >= 0 && useCaseName) {
            const encodedBranch = encodeURIComponent(selected.branch);
            const encodedBuild = encodeURIComponent(selected.build);
            const encodedComparison = encodeURIComponent(comparisonName);
            const encodedUseCase = encodeURIComponent(useCaseName);
            const encodedScenario = encodeURIComponent(scenarioName);

            return `rest/diffViewer/baseBranchName/${encodedBranch}/baseBuildName/${encodedBuild}/comparisonName/${encodedComparison}/useCaseName/${encodedUseCase}/scenarioName/${encodedScenario}/stepIndex/${stepIndex}/stepDiffScreenshot`;
        }
    }

    function getComparisonScreenShotUrl(comparisonBranchName, comparisonBuildName, useCaseName, scenarioName, comparisonScreenshotName) {
        if (comparisonBranchName && comparisonBuildName && useCaseName && scenarioName && comparisonScreenshotName) {
            const encodedComparisonBranch = encodeURIComponent(comparisonBranchName);
            const encodedComparisonBuild = encodeURIComponent(comparisonBuildName);
            const encodedUseCase = encodeURIComponent(useCaseName);
            const encodedScenario = encodeURIComponent(scenarioName);
            const encodedComparisonScreenshot = encodeURIComponent(comparisonScreenshotName);

            return `rest/branch/${encodedComparisonBranch}/build/${encodedComparisonBuild}/usecase/${encodedUseCase}/scenario/${encodedScenario}/image/${encodedComparisonScreenshot}`;
        }
    }

    return {
        getDiffScreenShotUrl: getDiffScreenShotUrl,
        getComparisonScreenShotUrl: getComparisonScreenShotUrl
    };
}
