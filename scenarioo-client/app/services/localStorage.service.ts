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

import {Injectable} from '@angular/core';
import {PlatformLocation} from '@angular/common';
import {UrlContextExtractorService} from '../shared/utils/urlContextExtractor.service';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class LocalStorageService {

    currentBrowserLocation: string;

    constructor(private platformLocation: PlatformLocation,
                private urlContextExtractorService: UrlContextExtractorService,
    ) {
    }

    get(key: string): string {
        return localStorage.getItem(this.getScenariooContextPathAwareKey(key));
    }

    getBoolean(key: string, defaultValue: boolean): boolean {
        const value = this.get(key);
        if (value === 'true') {
            return true;
        } else if (value === 'false') {
            return false;
        } else {
            return defaultValue;
        }
    }

    set(key: string, value: string) {
        localStorage.setItem(this.getScenariooContextPathAwareKey(key), value);
    }

    setBoolean(key: string, value: boolean) {
        this.set(key, '' + value);
    }

    remove(key: string) {
        return localStorage.removeItem(this.getScenariooContextPathAwareKey(key));
    }

    clearAll() {
        return localStorage.clear();
    }

    /**
     * @param key the key to make scenarioo context aware
     * @returns {string} key with scenarioo context path from location url inside.
     */
    getScenariooContextPathAwareKey(key: string): string {

        // TODO: Workaround until the full migration, when the Angular router is available
        // this.currentBrowserLocation = this.location.prepareExternalUrl(this.location.path());
        this.currentBrowserLocation = (this.platformLocation as any).location.href;

        return '/' + this.urlContextExtractorService.getContextPathFromUrl(this.currentBrowserLocation) + '/' + key;
    }

}

angular.module('scenarioo.services')
    .factory('LocalStorageService', downgradeInjectable(LocalStorageService));
