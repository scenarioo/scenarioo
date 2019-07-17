import {downgradeComponent} from '@angular/upgrade/static';
import {DetailareaComponent} from './detailarea/detailarea.component';
declare var angular: angular.IAngularStatic;

// If we move this into the component, where it belongs, scenarioo directives won't be available yet for tests.
angular.module('scenarioo.directives')
    .directive('scDetailarea',
        downgradeComponent({component: DetailareaComponent}) as angular.IDirectiveFactory);
