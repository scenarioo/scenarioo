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

function ScreenshotUrlService(HostnameAndPort) {

    function getDiffScreenShotUrl(step, selected, comparisonName, featureName, scenarioName, stepIndex) {
        if (step && stepIndex >= 0 && featureName) {
            return HostnameAndPort.forLink() + 'rest/diffViewer/baseBranchName/' + selected.branch + '/baseBuildName/' + selected.build + '/comparisonName/' + comparisonName + '/featureName/' + featureName + '/scenarioName/' + scenarioName + '/stepIndex/' + stepIndex + '/stepDiffScreenshot';
        }
    }

    function getComparisonScreenShotUrl(comparisonBranchName, comparisonBuildName, featureName, scenarioName, comparisonScreenshotName) {
        if (comparisonBranchName && comparisonBuildName && featureName && scenarioName && comparisonScreenshotName) {
            return HostnameAndPort.forLink() + 'rest/branch/' + comparisonBranchName + '/build/' + comparisonBuildName + '/feature/' + featureName + '/scenario/' + scenarioName + '/image/' + comparisonScreenshotName;
        }
    }

    return {
        getDiffScreenShotUrl: getDiffScreenShotUrl,
        getComparisonScreenShotUrl: getComparisonScreenShotUrl
    };
}
