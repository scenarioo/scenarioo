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

import {Component, Input} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-annotated-screenshot',
    template: require('./annotated-screenshot.component.html'),
    styles: [require('./annotated-screenshot.component.css').toString()],
})
export class AnnotatedScreenshotComponent {

    @Input()
    visibilityToggle: boolean;

    @Input()
    screenAnnotations;

    @Input()
    toNextStepAction;

    @Input()
    screenShotUrl;

    @Input()
    diffScreenShotUrl;

    @Input()
    showDiff;

    imageScalingRatio = 1;
    imageNaturalHeight = 0;

    private hasClickAction(annotation) {
        return annotation.clickAction;
    }

    private doClickAction(annotation) {
        const clickAction = annotation.clickAction || 'DEFAULT';
        const clickActions = {
            TO_URL() {
                window.open(annotation.clickActionUrl);
            },
            TO_NEXT_STEP() {
                this.toNextStepAction();
            },
            DEFAULT() {
                this.openInfoPopup(annotation);
            },
        };
        clickActions[clickAction]();
    }

    private openInfoPopup(annotation) {

    }

    /**
     * get the text to display inside the annotation box (depending if text box is big enough to display text)
     */
    private getBoxText(screenAnnotation) {
        const isTextVisible = screenAnnotation.region.width * this.imageScalingRatio > 32 && screenAnnotation.region.height * this.imageScalingRatio > 18;
        if (isTextVisible) {
            return screenAnnotation.screenText;
        } else {
            return '';
        }
    }

    private getBoxCssStyle(screenAnnotation) {
        return {
            // The border is 2 px wide. Therefore we add these three pixels here.
            'left': (screenAnnotation.region.x * this.imageScalingRatio - 2) + 'px',
            'top': (screenAnnotation.region.y * this.imageScalingRatio - 2) + 'px',
            'width': (screenAnnotation.region.width * this.imageScalingRatio + 4) + 'px',
            'height': (screenAnnotation.region.height * this.imageScalingRatio + 4) + 'px',
            'cursor': 'pointer',
            'z-index': 100,
        };
    }

    private getTooltipText(annotation) {
        // return getClickActionText(annotation);
    }

}

angular.module('scenarioo.directives')
    .directive('scStepView',
        downgradeComponent({component: AnnotatedScreenshotComponent}) as angular.IDirectiveFactory);
