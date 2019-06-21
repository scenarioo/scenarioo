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

angular.module('scenarioo.directives')
    .component('scMetaDataPanel', {
        transclude: true,
        bindings: {
            linkingVariable: '<',
        },
        template: require('./metaDataPanel.html'),
        controller: metaDataPanelController,
    });

function metaDataPanelController($element) {
    this.$onChanges = (changes) => {
        if (changes.linkingVariable) {
            toggleClassesOnPanels($element, changes.linkingVariable.currentValue);
        }
    };

    function toggleClassesOnPanels(elem, showingMetaData) {
        const elementChildren = elem.children();
        const mainAndDetailPanelRow = elementChildren[0];
        const panelChildren = mainAndDetailPanelRow.children;
        const mainPanel = panelChildren[0];
        const metaDataPanel = panelChildren[1];
        if (!mainPanel || !metaDataPanel) {
            // view not initialized yet
            return;
        }
        mainPanel.setAttribute('id', 'sc-main-panel');
        metaDataPanel.setAttribute('id', 'sc-metadata-panel');
        if (showingMetaData) {
            mainPanel.setAttribute('class', 'col-lg-8');
            metaDataPanel.setAttribute('class', 'col-lg-4 hero-unit meta-data');
            metaDataPanel.style.display = 'block';
        } else {
            mainPanel.setAttribute('class', 'col-lg-12');
            metaDataPanel.setAttribute('class', 'hero-unit meta-data');
            metaDataPanel.style.display = 'none';
        }
    }
}
