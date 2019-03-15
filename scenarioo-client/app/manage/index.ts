import {downgradeComponent} from '@angular/upgrade/static';
import {LabelColorsComponent} from './labelColors/labelColors.component';
import {GeneralSettingsComponent} from "./generalSettings/generalSettings.component";
import {StatusStylingComponent} from "./generalSettings/statusStyling/statusStyling.component";
import {DisplayOptionsComponent} from "./generalSettings/displayOptions/displayOptions.component";
import {AdditionalColumnsComponent} from "./generalSettings/additionalColumns/additionalColumns.component";
import {BranchesBuildsComponent} from "./generalSettings/branchesBuilds/branchesBuilds.component";
import {FulltextSearchComponent} from "./generalSettings/fulltextSearch/fulltextSearch.component";

declare var angular: angular.IAngularStatic;

import './config.service';
import './manage.controller';
import './buildImport/buildsList.controller';
import './buildImport/buildImportDetails.controller';
import './generalSettings/generalSettings.controller';
import './branchAliases/branchAliases.controller';
import './labelColors/labelColors.controller';
import './comparisons/comparisons.controller';
import './comparisons/comparisonDetails.controller';
import './comparisons/createComparisonModal.controller';
import './comparisons/comparisonStatusMapper.service';

angular.module('scenarioo.directives')
    .directive('generalSettings', downgradeComponent({component: GeneralSettingsComponent}) as angular.IDirectiveFactory)
    .directive('fulltextSearch', downgradeComponent({component: FulltextSearchComponent}) as angular.IDirectiveFactory)
    .directive('branchesBuilds', downgradeComponent({component: BranchesBuildsComponent}) as angular.IDirectiveFactory)
    .directive('additionalColumns', downgradeComponent({component: AdditionalColumnsComponent}) as angular.IDirectiveFactory)
    .directive('displayOptions', downgradeComponent({component: DisplayOptionsComponent}) as angular.IDirectiveFactory)
    .directive('statusStyling', downgradeComponent({component: StatusStylingComponent}) as angular.IDirectiveFactory)
    .directive('labelColors', downgradeComponent({component: LabelColorsComponent}) as angular.IDirectiveFactory);
