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

angular.module('scenarioo.controllers').controller('objectReferenceTreeCtrl', function ($scope, $location, $route, $uibModal, ObjectIndexListResource, ObjectListResource) {

    $scope.readObjectIndex = function (build) {
        ObjectIndexListResource.query({
            branchName: build.identifier.branchName, buildName: build.identifier.buildName,
            objectType: build.identifier.objectType, objectName: build.identifier.objectName
        }, {});
    };

    $scope.readList = function (build) {
        ObjectListResource.get({
            branchName: build.identifier.branchName, buildName: build.identifier.buildName,
            objectType: build.identifier.objectType
        }, {});
    };
});
