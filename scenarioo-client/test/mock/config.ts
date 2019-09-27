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

import {ReplaySubject} from 'rxjs';
declare var angular: angular.IAngularStatic;


angular.module('scenarioo.services').service('ConfigMock', () => {
    return ({
        branch: undefined,
        build: undefined,
        appInfo: new ReplaySubject<String>(1),

        isLoaded() {
            return angular.isDefined(this.build) && angular.isDefined(this.build);
        },

        selectedBranch() {
            return this.branch;
        },

        selectedBuild() {
            return this.build;
        },

        selectedBuildAndBranch() {
            return {
                branch: this.selectedBranch(),
                build: this.selectedBuild()
            }
        },

        applicationInformation() {
            return this.appInfo.asObservable();
        },

        setSelectedBranch(branch) {
            this.branch = branch;
        },

        setSelectedBuil(build) {
            this.build = build;
        },

        setApplicationInformation(applicationInformation) {
            return this.appInfo.next(applicationInformation);
        },

        scenarioPropertiesInOverview: () =>
            [
                {
                    text: 'User Profile',
                    property: 'details.properties.userProfile',
                    attr: 'userProfile'
                }
            ]
    });
});
