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
declare var angular: angular.IAngularStatic;

angular.module('scenarioo.controllers').controller('TitleController', TitleController);

function TitleController(ConfigurationService: ConfigurationService) {

    const vm = this;
    vm.text = '';

    ConfigurationService.applicationName().subscribe(name => {
        vm.text = `Scenarioo ${name}`;
    });
}
