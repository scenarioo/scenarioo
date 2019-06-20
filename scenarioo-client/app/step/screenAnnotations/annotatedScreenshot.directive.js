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
    .component('scAnnotatedScreenshot', {
        bindings: {
            screenAnnotations: '<',
            screenShotUrl: '<',
            diffScreenShotUrl: '<',
            showDiff: '<',
            visibilityToggle: '<',
            toNextStepAction: '&'
        },
        template: require('./annotatedScreenshot.html'),
        controller: annotatedScreenshotController
    });

function annotatedScreenshotController($element, $uibModal, ScreenAnnotationsService, $window) {

    const vm = this;

    vm.imageScalingRatio = 1;
    vm.imageNaturalHeight = 0;
    vm.getBoxCssStyle = getBoxCssStyle;
    vm.getBoxText = getBoxText;
    vm.getIconCssStyle = getIconCssStyle;
    vm.getIconClass = getIconClass;
    vm.openInfoPopup = openInfoPopup;
    vm.doClickAction = doClickAction;
    vm.hasClickAction = hasClickAction;
    vm.getTooltipText = getTooltipText;

    var realImageElement = $element.find('img.sc-real-screenshot');
    var diffImageElement = $element.find('img.sc-diff-screenshot');

    var relevantImageElement = diffImageElement;
    if (!vm.showDiff) {
        relevantImageElement = realImageElement;
    }

    $(relevantImageElement).on('load', updateImageScalingRatio);
    $($window).on('resize', updateImageScalingRatio);

    this.$onDestroy = () => {
        $($window).off('resize', updateImageScalingRatio);
        $(relevantImageElement).off('load', updateImageScalingRatio);
    };

    function updateImageScalingRatio() {
        var relevantNaturalWidth = relevantImageElement.get(0).naturalWidth;
        var relevantDisplayWidth = relevantImageElement.width();
        vm.imageNaturalHeight = relevantImageElement.get(0).naturalHeight;
        vm.imageScalingRatio = relevantDisplayWidth / relevantNaturalWidth;

        if (vm.showDiff) {
            var realNaturalWidth = realImageElement.get(0).naturalWidth;
            var realNaturalHeight = realImageElement.get(0).naturalHeight;

            realImageElement.get(0).width = realNaturalWidth * vm.imageScalingRatio + 4;
            realImageElement.get(0).height = realNaturalHeight * vm.imageScalingRatio + 4;
        }

        //scope.$digest();
    }

    /**
     * get the text to display inside the annotation box (depending if text box is big enough to display text)
     */
    function getBoxText(screenAnnotation) {
        var isTextVisible = screenAnnotation.region.width * vm.imageScalingRatio > 32 && screenAnnotation.region.height * vm.imageScalingRatio > 18;
        if (isTextVisible) {
            return screenAnnotation.screenText;
        } else {
            return '';
        }
    }

    function getBoxCssStyle(screenAnnotation) {
        return {
            // The border is 2 px wide. Therefore we add these three pixels here.
            left: (screenAnnotation.region.x * vm.imageScalingRatio - 2) + 'px',
            top: (screenAnnotation.region.y * vm.imageScalingRatio - 2) + 'px',
            width: (screenAnnotation.region.width * vm.imageScalingRatio + 4) + 'px',
            height: (screenAnnotation.region.height * vm.imageScalingRatio + 4) + 'px',
            cursor: 'pointer',
            'z-index': 100
        };
    }

    function getIconCssStyle(screenAnnotation) {
        return {
            left: (screenAnnotation.region.x + screenAnnotation.region.width) * vm.imageScalingRatio + 'px',
            bottom: (vm.imageNaturalHeight - screenAnnotation.region.y) * vm.imageScalingRatio + 'px',
            cursor: 'pointer',
            'z-index': 90
        };
    }

    function getIconClass(screenAnnotation) {
        return ScreenAnnotationsService.getIconClass(screenAnnotation);
    }

    function openInfoPopup(annotation) {

        $uibModal.open({
            template: require('./screenAnnotationInfoPopup.html'),
            controller: 'ScreenAnnotationInfoPopupController',
            controllerAs: 'annotationPopup',
            resolve: {
                annotation: function () {
                    return annotation;
                },
                goToNextStep: function () {
                    return vm.toNextStepAction;
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
            TO_NEXT_STEP: function () {
                vm.toNextStepAction();
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
