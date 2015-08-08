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
/* eslint no-console:0*/

angular.module('scenarioo.services').factory('SketchStep', function () {

    var SKETCHSTEP_LOADED_EVENT = 'sketchStepLoaded';

    var sketchStepData = {};


    return {
        SKETCHSTEP_LOADED_EVENT: SKETCHSTEP_LOADED_EVENT,

        getRawSketchStepDataCopy: function () {
            return angular.copy(sketchStepData);
        },

        saveSketchStep: function (changedSketchStep, successCallback, errorCallback) {

            changedSketchStep.$save(function (updatedSketchStep) {
                if (successCallback) {
                    successCallback(updatedSketchStep);
                }
            },
            function (error) {
                console.log(error);
                if (errorCallback) {
                    errorCallback('SketchStep could not be saved');
                }
            });
        }
    };

});
