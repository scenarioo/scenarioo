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


/**
 * Shows a screenshot plus annotations, if available. The annotations are shown as a separate layer on top of the
 * screenshot.
 */
angular
    .module('scenarioo.directives')
    .directive('scAnnotatedScreenshot', annotatedScreenshot);

function annotatedScreenshot() {

    var directive = {
        restrict: 'E',
        templateUrl: 'views/annotatedScreenshot.html',
        link: link,
        scope: {
            screenAnnotations: '=',
            getScreenShotUrl: '='
        }
    };

    return directive;

    function link(scope, element) {
        scope.imageScalingRatio = 1;

        var imageElement = element.find('img.sc-screenshot');

        $(imageElement).load(function() {
            updateImageScalingRatio();
        });

        $(window).resize(function() {
            updateImageScalingRatio();
        });

        function updateImageScalingRatio() {
            var imageNaturalWidth = imageElement.get(0).naturalWidth;
            var imageDisplayWidth = imageElement.width();
            scope.imageScalingRatio = imageDisplayWidth / imageNaturalWidth;
            scope.$digest();
        }
    }

}
