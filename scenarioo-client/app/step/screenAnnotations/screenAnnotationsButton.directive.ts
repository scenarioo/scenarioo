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

import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';
import {IScreenAnnotation} from '../../generated-types/backend-types';

@Directive({
    selector: 'sc-screen-annotations-button',
})
export class ScreenAnnotationsButtonDirective extends UpgradeComponent {

    @Input() screenAnnotations: IScreenAnnotation[];

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scScreenAnnotationsButton', elementRef, injector);
    }
}
