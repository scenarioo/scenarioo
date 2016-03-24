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
        templateUrl: 'template/annotatedScreenshot.html',
        link: link,
        controller: controller,
        scope: {
            screenAnnotations: '=',
            getScreenShotUrl: '=',
            visibilityToggle: '=',
            toNextStepAction: '&'
        }
    };

    return directive;

    function link(scope, element) {

        scope.imageScalingRatio = 1;
        scope.imageNaturalHeight = 0;

        var imageElement = element.find('img.sc-screenshot');

        $(imageElement).load(updateImageScalingRatio);

        $(window).resize(updateImageScalingRatio);

        function updateImageScalingRatio() {
            var imageNaturalWidth = imageElement.get(0).naturalWidth;
            var imageDisplayWidth = imageElement.width();
            scope.imageNaturalHeight = imageElement.get(0).naturalHeight;
            scope.imageScalingRatio = imageDisplayWidth / imageNaturalWidth;
            scope.$digest();
        }

    }

    function controller($scope, $uibModal, ScreenAnnotationsService, $window) {

        $scope.getBoxCssStyle = getBoxCssStyle;
        $scope.getBoxText = getBoxText;
        $scope.getIconCssStyle = getIconCssStyle;
        $scope.getIconClass = getIconClass;
        $scope.openInfoPopup = openInfoPopup;
        $scope.doClickAction = doClickAction;
        $scope.hasClickAction = hasClickAction;
        $scope.getTooltipText = getTooltipText;

        /**
         * get the text to display inside the annotation box (depending if text box is big enough to display text)
         */
        function getBoxText(screenAnnotation) {
            var isTextVisible = screenAnnotation.region.width * $scope.imageScalingRatio > 32 && screenAnnotation.region.height * $scope.imageScalingRatio > 18;
            if (isTextVisible) {
                return screenAnnotation.screenText;
            } else {
                return '';
            }
        }

        function getBoxCssStyle(screenAnnotation) {
            return {
                // The border is 2 px wide. Therefore we add these three pixels here.
                left: (screenAnnotation.region.x * $scope.imageScalingRatio - 2) + 'px',
                top: (screenAnnotation.region.y * $scope.imageScalingRatio - 2) + 'px',
                width: (screenAnnotation.region.width * $scope.imageScalingRatio + 4) + 'px',
                height: (screenAnnotation.region.height * $scope.imageScalingRatio + 4) + 'px',
                cursor: 'pointer',
                'z-index': 100
            };
        }

        function getIconCssStyle(screenAnnotation) {
            return {
                left: (screenAnnotation.region.x + screenAnnotation.region.width ) * $scope.imageScalingRatio + 'px',
                bottom: ($scope.imageNaturalHeight - screenAnnotation.region.y) * $scope.imageScalingRatio + 'px',
                cursor: 'pointer',
                'z-index': 90
            };
        }

        function getIconClass(screenAnnotation) {
            return ScreenAnnotationsService.getIconClass(screenAnnotation);
        }

        function openInfoPopup(annotation) {

            $uibModal.open({
                templateUrl: 'template/screenAnnotationInfoPopup.html',
                controller: 'ScreenAnnotationInfoPopupController',
                controllerAs: 'annotationPopup',
                resolve: {
                    annotation: function () {
                        return annotation;
                    },
                    goToNextStep: function() {
                        return $scope.toNextStepAction;
                    }
                },
                windowClass: 'modal-small screen-annotation-popup'
            });

        }

        function doClickAction(annotation) {

            var clickAction = annotation.clickAction || 'DEFAULT';
            var clickActions = {
                TO_URL: function () {
                    $window.open(annotation.clickActionUrl);
                },
                TO_NEXT_STEP: function() {
                    $scope.toNextStepAction();
                },
                DEFAULT: function () {
                    openInfoPopup(annotation);
                }
            };
            clickActions[clickAction]();
        }

        function getTooltipText(annotation) {
            return ScreenAnnotationsService.getClickActionText(annotation);
        }

        function hasClickAction(annotation) {
            return annotation.clickAction;
        }

    }

}
