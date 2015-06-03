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

'use strict';

angular.module('scenarioo.services').service('ConfigMock', function () {

    return {
        branch: undefined,
        build: undefined,
        appInfo: undefined,

        isLoaded: function () {
            return angular.isDefined(this.build) && angular.isDefined(this.build);
        },

        selectedBranch: function () {
            return this.branch;
        },

        selectedBuild: function () {
            return this.build;
        },

        selectedBuildAndBranch: function () {
            return {
                branch: this.selectedBranch(),
                build: this.selectedBuild()
            };
        },

        applicationInformation: function() {
            return this.appInfo;
        },

        setSelectedBranch: function (branch) {
            this.branch = branch;
        },

        setSelectedBuild: function (build) {
            this.build = build;
        },

        setApplicationInformation: function(applicationInformation) {
            this.appInfo = applicationInformation;
        },

        scenarioPropertiesInOverview: function () {
            return [
                {
                    text: 'User Profile',
                    property: 'details.properties.userProfile',
                    attr: 'userProfile'
                }
            ];
        }
    };

});
