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

angular.module('scenarioo.services').factory('PagesAndSteps', function () {
    return {
        populatePagesAndSteps: function (pagesAndScenarios) {
            for (var indexPage = 0; indexPage < pagesAndScenarios.pagesAndSteps.length; indexPage++) {
                var page = pagesAndScenarios.pagesAndSteps[indexPage];
                page.page.index = indexPage + 1;
                for (var indexStep = 0; indexStep < page.steps.length; indexStep++) {
                    var step = page.steps[indexStep];
                    step.page = page.page;
                    step.index = indexStep;
                    step.number = (indexStep === 0) ? page.page.index : page.page.index + '.' + indexStep;
                    if (!step.title) {
                        step.title = 'undefined';
                    }
                }
            }
            return pagesAndScenarios;
        }
    };
});