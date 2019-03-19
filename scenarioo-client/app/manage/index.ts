import {downgradeComponent} from '@angular/upgrade/static';
import {LabelColorsComponent} from './labelColors/labelColors.component';
import {BranchAliasesComponent} from './branchAliases/branchAliases.component';

declare var angular: angular.IAngularStatic;

import './config.service';
import './manage.controller';
import './buildImport/buildsList.controller';
import './buildImport/buildImportDetails.controller';
import './generalSettings/generalSettings.controller';
import './branchAliases/branchAliases.controller';
import './comparisons/comparisons.controller';
import './comparisons/comparisonDetails.controller';
import './comparisons/createComparisonModal.controller';
import './comparisons/comparisonStatusMapper.service';

angular.module('scenarioo.directives')
    .directive('labelColors', downgradeComponent({component: LabelColorsComponent}) as angular.IDirectiveFactory)
    .directive('branchAliases', downgradeComponent({component: BranchAliasesComponent}) as angular.IDirectiveFactory);
