import {downgradeComponent} from '@angular/upgrade/static';
import {GeneralSettingsComponent} from './generalSettings/generalSettings.component';
import {LabelColorsComponent} from './labelColors/labelColors.component';

declare var angular: angular.IAngularStatic;

import './config.service';
import './manage.controller';
import './buildImport/buildsList.controller';
import './buildImport/buildImportDetails.controller';
import './branchAliases/branchAliases.controller';
import './comparisons/comparisons.controller';
import './comparisons/comparisonDetails.controller';
import './comparisons/createComparisonModal.controller';
import './comparisons/comparisonStatusMapper.service';

angular.module('scenarioo.directives')
    .directive('generalSettings', downgradeComponent({component: GeneralSettingsComponent}) as angular.IDirectiveFactory)
    .directive('labelColors', downgradeComponent({component: LabelColorsComponent}) as angular.IDirectiveFactory);
