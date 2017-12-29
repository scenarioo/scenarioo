import {downgradeComponent} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './label-metadata/label-metadata.component';
import * as angular from 'angular';

import './screenAnnotations/annotatedScreenshot.directive';
import './screenAnnotations/screenAnnotationsButton.directive';
import './screenAnnotations/screenAnnotationInfoPopup.controller';
import './screenAnnotations/screenAnnotations.service';
import './step.controller';

// If we move this into the component, where it belongs, scenarioo directives won't be available yet for tests.
angular.module('scenarioo.directives')
    .directive('scLabelMetadata',
        downgradeComponent({component: LabelMetadataComponent}) as angular.IDirectiveFactory);
