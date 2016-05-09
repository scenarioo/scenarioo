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
angular.module('scenarioo.controllers').controller('ScreenAnnotationInfoPopupController', function ($scope, $uibModalInstance, $window, $filter, ScreenAnnotationsService, annotation, goToNextStep) {

    var vm = this;
    vm.annotation = annotation;
    vm.goToNextStep = goToNextStep;
    vm.detailsTrees = {};

    vm.getIconClass = getIconClass;
    vm.getTitleText = getTitleText;
    vm.getDescriptionSection = getDescriptionSection;
    vm.getClickActionText = getClickActionText;
    vm.hasDetails = hasDetails;
    vm.doClickAction = doClickAction;
    vm.getClickActionIconStyle = getClickActionIconStyle;

    activate();


    function activate() {
        var createMetadataTree = $filter('scMetadataTreeCreator');
        vm.detailsTrees = createMetadataTree(annotation.details);
    }

    function getIconClass() {
        return ScreenAnnotationsService.getIconClass(vm.annotation);
    }

    function getTitleText() {
        return ScreenAnnotationsService.getTitleText(vm.annotation);
    }

    function getDescriptionSection() {
        return ScreenAnnotationsService.getDescriptionSection(vm.annotation);
    }

    function getClickActionText() {
        return ScreenAnnotationsService.getClickActionText(vm.annotation);
    }

    function getClickActionIconStyle() {
        if (vm.annotation.clickAction === 'TO_NEXT_STEP') {
            return 'icon-chevron-right';
        }
        else {
            return 'icon-external-link';
        }
    }

    function doClickAction() {
        if (vm.annotation.clickAction) {
            var clickActions = {
                TO_URL: function () {
                    $window.open(vm.annotation.clickActionUrl);
                },
                TO_NEXT_STEP: goToNextStep
            };
            clickActions[vm.annotation.clickAction]();
        }
    }

    function hasDetails() {
        return !angular.equals({}, annotation.details);
    }

});
