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
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-sketches-tab',
})
export class SketchesTabDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scSketchesTab', elementRef, injector);
    }
}
