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

angular.module('scenarioo.controllers').controller('UseCasesTabController', function ($scope, $location, $filter, GlobalHotkeysService, BranchesAndBuildsService, SelectedBranchAndBuildService, UseCasesResource, LabelConfigurationsResource) {

  var transformMetadataToTree = $filter('scMetadataTreeCreator');
  var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
  var dateTimeFormatter = $filter('scDateTime');
  SelectedBranchAndBuildService.callOnSelectionChange(loadUseCases);

  // FIXME this code is duplicated. How can we extract it into a service?
  LabelConfigurationsResource.query({}, function (labelConfiguratins) {
    $scope.labelConfigurations = labelConfiguratins;
  });

  function loadUseCases(selected) {

    BranchesAndBuildsService.getBranchesAndBuildsService()
      .then(function onSuccess(branchesAndBuilds) {
        $scope.branchesAndBuilds = branchesAndBuilds;
      }).then(function () {
        UseCasesResource.query(
          {'branchName': selected.branch, 'buildName': selected.build},
          function onSuccess(result) {
            $scope.useCases = result;

            var branch = $scope.branchesAndBuilds.selectedBranch.branch;
            var build = $scope.branchesAndBuilds.selectedBuild.build;
            $scope.branchInformationTree = createBranchInformationTree(branch);
            $scope.buildInformationTree = createBuildInformationTree(build);
            $scope.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
            $scope.metadataTreeBuilds = transformMetadataToTreeArray(build.details);
          });
      });

  }

  $scope.goToUseCase = function (useCaseName) {
    $location.path('/usecase/' + useCaseName);
  };

  $scope.onNavigatorTableHit = function (useCase) {
    $scope.goToUseCase(useCase.name);
  };

  $scope.table = {search: {searchTerm: ''}, sort: {column: 'name', reverse: false}};

  $scope.resetSearchField = function () {
    $scope.table.search = {searchTerm: ''};
  };

  function createBranchInformationTree(branch) {
    var branchInformationTree = {};
    branchInformationTree.Description = branch.description;
    return transformMetadataToTree(branchInformationTree);
  }

  function createBuildInformationTree(build) {
    var buildInformationTree = {};
    buildInformationTree.Date = dateTimeFormatter(build.date);
    buildInformationTree.Revision = build.revision;
    buildInformationTree.Status = build.status;
    return transformMetadataToTree(buildInformationTree);
  }

  // FIXME this code is duplicated. How can we extract it into a service?
  $scope.getLabelStyle = function (labelName) {
    if ($scope.labelConfigurations) {
      var labelConfig = $scope.labelConfigurations[labelName];
      if (labelConfig) {
        return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
      }
    }
  };
});
