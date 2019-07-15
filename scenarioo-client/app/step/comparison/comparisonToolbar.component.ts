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
import {ConfigurationService} from '../../services/configuration.service';

angular
    .module('scenarioo.directives')
    .component('scComparisonToolbar', {
        bindings: {
            step: '<',
            comparisonViewOptions: '<',
        },
        template: require('./comparisonToolbar.html'),
        controller: ComparisonToolbarController,
    });
function ComparisonToolbarController(localStorageService, ConfigurationService: ConfigurationService) {
    const ctrl = this;

    ctrl.setComparisonView = (viewId: string) => {
        ctrl.comparisonViewOptions.viewId = viewId;
        setLocalStorageValue('diffViewerStepComparisonViewId', viewId);
    };

    ctrl.isComparisonView = (viewId: string) : boolean => ctrl.step && ctrl.step.diffInfo && ctrl.step.diffInfo.isAdded
        ? viewId === 'SideBySide' // fixed side by side view for added steps
        : ctrl.comparisonViewOptions.viewId === viewId;

    ctrl.switchComparisonSingleScreenView = () => {
        const viewId = ctrl.isComparisonView('CurrentScreen') ? 'OtherScreen' : 'CurrentScreen';
        ctrl.setComparisonView(viewId);
    };

    ctrl.isComparisonChangesToBeHighlightedAvailable = () : boolean =>
        ctrl.step && ctrl.step.diffInfo && ctrl.step.diffInfo.changeRate !== 0 && !ctrl.step.diffInfo.isAdded;

    ctrl.isComparisonChangesHighlighted = () : boolean=>
        // highlighting is turned on, and there are changes in this screenshot to be highlighted
        ctrl.isComparisonChangesToBeHighlightedAvailable() && ctrl.comparisonViewOptions.changesHighlighted;

    ctrl.toggleComparisonChangesHighlighted = () => {
        ctrl.comparisonViewOptions.changesHighlighted = !ctrl.comparisonViewOptions.changesHighlighted;
        setLocalStorageValue('diffViewerStepComparisonChangesHighlighted', ctrl.comparisonViewOptions.changesHighlighted);
    };

    ctrl.getComparisonViewHighlightChangesColor = () => ConfigurationService.diffViewerDiffImageColor();

    function setLocalStorageValue(storageKey: string, value: any) {
        localStorageService.set(storageKey, '' + value);
    }
}
