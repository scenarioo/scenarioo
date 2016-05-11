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

angular.module('scenarioo.services').factory('ScreenAnnotationsService', function () {

    var service = {};
    service.hasPopup = hasPopup;
    service.getIconClass = getIconClass;
    service.getTitleText = getTitleText;
    service.getDescriptionSection = getDescriptionSection;
    service.getClickActionText = getClickActionText;
    return service;

    function hasPopup(annotation) {
        return getTitleText(annotation) !== '' || hasDetails(annotation);
    }

    function hasDetails(annotation) {
        return annotation.details.length > 0;
    }

    function getTitleText(annotation) {
        if (annotation.title !== '') {
           return annotation.title;
        }
        else if (annotation.screenText !== '') {
            return annotation.screenText;
        }
        else {
            return annotation.description;
        }
    }

    function getDescriptionSection(annotation) {
        if (getTitleText(annotation) === annotation.description) {
            return ''; // allready displayed in title
        }
        else {
            return annotation.description;
        }
    }

    function getClickActionText(annotation) {
        if (annotation.clickActionText) {
            return annotation.clickActionText;
        }
        else if (annotation.clickAction === 'TO_NEXT_STEP') {
            return 'Go to next step';
        }
        else if (annotation.clickAction === 'TO_URL') {
            return 'Open ' + annotation.clickActionUrl;
        }
        else {
            return null; // no tooltip for missing click action (user can open popup for more info)
        }
    }

    function getIconClass(screenAnnotation) {

        // Icons from http://fortawesome.github.io/Font-Awesome/3.2.1/icons/
        var styleToIconClassMap = {
            CLICK: 'icon-hand-up',
            KEYBOARD: 'icon-keyboard',
            EXPECTED: 'icon-check',
            NAVIGATE_TO_URL: 'icon-globe',
            ERROR: 'icon-exclamation-sign',
            WARN: 'icon-warning-sign',
            INFO: 'icon-info-sign',
            HIGHLIGHT: 'icon-quote-right',
            DEFAULT: 'icon-comment-alt'
        };

        if (angular.isUndefined(screenAnnotation.style)) {
            return '';
        }
        var styleClass = styleToIconClassMap[screenAnnotation.style];

        if (angular.isUndefined(styleClass)) {
            return '';
        }

        return styleClass;
    }


});
