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
angular.module('scenarioo').component('navTree', {
    templateUrl: 'dashboard/comp/navTree/navTree.html',
    controllerAs: 'navTree',
    bindings: {
        feature:'=',
        currentFeature:'=',
    },
    controller: function (FeatureService) {
        var navTree = this;
        var color = 'lightskyblue';
        navTree.clickFeature = FeatureService.clickFeature;
        navTree.contains = FeatureService.contains;

        navTree.getBGColor = function () {
            if (navTree.currentFeature.name === navTree.feature.name) {
                if (navTree.currentFeature.parentFeature == null) {
                    return color;
                }
                if (navTree.feature.parentFeature != null) {
                    if (navTree.currentFeature.parentFeature.name===navTree.feature.parentFeature.name) {
                        return color;
                    }
                }
            }
            return 'white';
        };
    }
});
