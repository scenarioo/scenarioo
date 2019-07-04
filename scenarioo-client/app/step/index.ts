import {downgradeComponent} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './label-metadata/label-metadata.component';
import './screenAnnotations/annotatedScreenshot.component';
import './screenAnnotations/screenAnnotationsButton.directive';
import './screenAnnotations/screenAnnotationInfoPopup.controller';
import './screenAnnotations/screenAnnotations.service';
import './stepNotFound/stepNotFound.component';
import './step.controller';

declare var angular: angular.IAngularStatic;

// If we move this into the component, where it belongs, scenarioo directives won't be available yet for tests.
angular.module('scenarioo.directives')
    .directive('scLabelMetadata',
        downgradeComponent({component: LabelMetadataComponent}) as angular.IDirectiveFactory);
