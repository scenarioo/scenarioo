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


import {addRoutes} from "./app.routes";
import * as angular from "angular";
import './vendor'
import './styles/scenarioo.less'

angular.module('scenarioo.filters', []);
angular.module('scenarioo.screenAnnotations', ['scenarioo.filters', 'ngRoute']);
angular.module('scenarioo.directives', ['scenarioo.filters', 'ngRoute', 'twigs.globalHotkeys', 'unsavedChanges']);
angular.module('scenarioo.services', ['ngResource', 'ngRoute', 'LocalStorageModule']);
angular.module('scenarioo.controllers', ['scenarioo.services', 'scenarioo.directives']);
require('./shared/utils/number.filter');


angular.module('scenarioo', ['scenarioo.controllers', 'ui.bootstrap', 'scenarioo.screenAnnotations'])

    .config(function ($routeProvider) {
        addRoutes($routeProvider);

    }).run(function ($rootScope, ConfigService, GlobalHotkeysService, $location, $uibModalStack, ApplicationInfoPopupService, $templateCache) {

    // These templates are loaded dynamically, thus we preload it and put it into our template cache.
    $templateCache.put('shared/navigation/navigation.html', require('./shared/navigation/navigation.html'));
    $templateCache.put('build/useCasesTab.html', require('./build/useCasesTab.html'));
    $templateCache.put('build/useCasesTabNgx.html', require('./build/useCasesTabNgx.html'));
    $templateCache.put('build/customTab.html', require('./build/customTab.html'));
    $templateCache.put('manage/buildImport/buildsList.html', require('./manage/buildImport/buildsList.html'));
    $templateCache.put('manage/generalSettings/generalSettings.html', require('./manage/generalSettings/generalSettings.html'));
    $templateCache.put('manage/branchAliases/branchAliases.html', require('./manage/branchAliases/branchAliases.html'));
    $templateCache.put('manage/labelColors/labelColors.html', require('./manage/labelColors/labelColors.html'));
    $templateCache.put('build/sketchesTab.html', require('./build/sketchesTab.html'));


    // Initialze modals to close when the location changes
    $rootScope.$on('$locationChangeSuccess', function () {
        $uibModalStack.dismissAll();
    });

    $rootScope.$on('$viewContentLoaded', function () {
        ApplicationInfoPopupService.showApplicationInfoPopupIfRequired();
    });

    // Register global hotkeys
    GlobalHotkeysService.registerGlobalHotkey('m', function () {
        $location.path('/manage').search('tab=builds');
    });
    GlobalHotkeysService.registerGlobalHotkey('c', function () {
        $location.path('/manage').search('tab=configuration');
    });
    GlobalHotkeysService.registerGlobalHotkey('h', function () {
        $location.path('/');
    });

    // Load config
    ConfigService.load();
});

import './build';
import './manage';
import './objectRepository/objectRepository.controller';
import './scenario';
import './search/search.controller';
import './shared';
import './sketcher';
import './step';
import './useCase/usecase.controller';
import './diffViewer';


import {AppModule} from "./app.module";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";


// angular.bootstrap(document, ['scenarioo']);
platformBrowserDynamic().bootstrapModule(AppModule);

