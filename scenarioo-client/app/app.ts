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

import {addRoutes} from './app.routes';
import './vendor';
import './styles/scenarioo.less';

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.filters', []);
angular.module('scenarioo.screenAnnotations', ['scenarioo.filters', 'ngRoute']);
angular.module('scenarioo.directives', ['scenarioo.filters', 'ngRoute', 'twigs.globalHotkeys']);
angular.module('scenarioo.services', ['ngResource', 'ngRoute', 'LocalStorageModule']);
angular.module('scenarioo.controllers', ['scenarioo.services', 'scenarioo.directives']);
import './shared/utils/number.filter';

angular.module('scenarioo', ['scenarioo.controllers', 'ui.bootstrap', 'scenarioo.screenAnnotations'])

    .config(($routeProvider) => {
        addRoutes($routeProvider);

    }).run(($rootScope, GlobalHotkeysService, $location, $uibModalStack, ApplicationInfoPopupService, $templateCache) => {

    // These templates are loaded dynamically, thus we preload it and put it into our template cache.
    $templateCache.put('shared/navigation/navigation.html', require('./shared/navigation/navigation.html'));
    $templateCache.put('build/useCasesTab.html', require('./build/useCasesTab.html'));
    $templateCache.put('build/useCasesTab.html', require('./build/useCasesTab.html'));
    $templateCache.put('build/customTab.html', require('./build/customTab.html'));
    $templateCache.put('build/sketchesTab.html', require('./build/sketchesTab.html'));

    // Initialze modals to close when the location changes
    $rootScope.$on('$locationChangeSuccess', () => {
        $uibModalStack.dismissAll();
    });

    $rootScope.$on('$viewContentLoaded', () => {
        // Workaround because angular reloads the page a couple of times if query parameter were not set, which leads
        // to a reload of all controllers and a dismissal of the open PopUp. Thus it disappears very quickly.
        setTimeout(() => ApplicationInfoPopupService.showApplicationInfoPopupIfRequired(), 50);
    });

    // Register global hotkeys
    GlobalHotkeysService.registerGlobalHotkey('m', () => {
        $location.path('/manage').search('tab=builds');
    });
    GlobalHotkeysService.registerGlobalHotkey('c', () => {
        $location.path('/manage').search('tab=configuration');
    });
    GlobalHotkeysService.registerGlobalHotkey('h', () => {
        $location.path('/');
    });
});

// needs to stay here
import './build';
import './manage';
import './objectRepository/objectRepository.controller';
import './scenario';
import './search/search.controller';
import './shared';
import './sketcher';
import './step';
import './components';
import './useCase/usecase.controller';
import './diffViewer';

import {AppModule} from './app.module';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

// tslint:disable-next-line
platformBrowserDynamic().bootstrapModule(AppModule);
