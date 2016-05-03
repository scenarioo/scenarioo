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

angular.module('scenarioo.services').factory('DiffViewerService', function ($location, $rootScope, HostnameAndPort) {


    function getDiffScreenShotUrl(step, selected, comparisonName, usecaseName, scenarioName, stepIndex ) {

        if (angular.isUndefined(step)) {
            return undefined;
        }

        if (angular.isUndefined(stepIndex)) {
            return undefined;
        }

        if (angular.isUndefined(usecaseName)) {
            return undefined;
        }

        return HostnameAndPort.forLink() + 'rest/diffViewer/' + selected.branch + '/' + selected.build + '/' + comparisonName + '/' + usecaseName + '/' + scenarioName + '/' + stepIndex + '/stepDiffScreenshot';

    }





     return {
              /**
         * Returns the Diff Screenshot URL
         */
        getDiffScreenShotUrl: getDiffScreenShotUrl
    };

});
