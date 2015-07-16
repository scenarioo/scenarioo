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
        controller: controller,
        scope: {
            screenAnnotations: '=',
            getScreenShotUrl: '=',
            visibilityToggle: '='
        }
    };

    return directive;

    function link(scope, element) {
        scope.imageScalingRatio = 1;

        var imageElement = element.find('img.sc-screenshot');

        $(imageElement).load(function () {
            updateImageScalingRatio();
        });

        $(window).resize(function () {
            updateImageScalingRatio();
        });

        function updateImageScalingRatio() {
            var imageNaturalWidth = imageElement.get(0).naturalWidth;
            var imageDisplayWidth = imageElement.width();
            scope.imageNaturalHeight = imageElement.get(0).naturalHeight;
            scope.imageScalingRatio = imageDisplayWidth / imageNaturalWidth;
            scope.$digest();
        }
    }

    function controller($scope) {
        // Icons from http://fortawesome.github.io/Font-Awesome/3.2.1/icons/
        var styleToIconClassMap = {
            click: 'icon-hand-up',
            keyboard: 'icon-keyboard',
            // TODO #149 Rename to something else if Rolf agrees
            validate: 'icon-check',
            // TODO #149 Remove if Rolf agrees
            navigateToUrl: 'icon-globe',
            error: 'icon-exclamation-sign',
            warn: 'icon-warning-sign',
            info: 'icon-info-sign',
            highlight: 'icon-quote-right'
        };

        $scope.getBoxCssStyle = function getBoxCssStyle(screenAnnotation) {
            return {
                // The border is 3 px wide. Therefore we add these three pixels here.
                left: (screenAnnotation.region.x * $scope.imageScalingRatio - 3) + 'px',
                top: (screenAnnotation.region.y * $scope.imageScalingRatio - 3) + 'px',
                width: (screenAnnotation.region.width * $scope.imageScalingRatio + 6) + 'px',
                height: (screenAnnotation.region.height * $scope.imageScalingRatio + 6) + 'px'
            };
        };

        $scope.getIconCssStyle = function getIconCssStyle(screenAnnotation) {
            return {
                left: (screenAnnotation.region.x + screenAnnotation.region.width ) * $scope.imageScalingRatio + 'px',
                bottom: ($scope.imageNaturalHeight - screenAnnotation.region.y) * $scope.imageScalingRatio + 'px'
            };
        };

        $scope.getIconClass = function getIconClass(screenAnnotationStyle) {
            if(angular.isUndefined(screenAnnotationStyle)) {
                return '';
            }

            var styleClass = styleToIconClassMap[screenAnnotationStyle];

            if(angular.isUndefined(styleClass)) {
                return '';
            }

            return styleClass;
        };
    }
}
