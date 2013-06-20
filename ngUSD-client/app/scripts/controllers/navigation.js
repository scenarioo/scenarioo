'use strict';

angular.module('ngUSDClientApp')
    .controller('NavigationCtrl', function ($scope, $location, BranchService) {

    $scope.branches = BranchService.findAllBranches(postProcessing);

    function postProcessing() {

        var retrieveOrFirst = function(list, name) {
            if (list == null || list.length==0)  return null;
            if (name == null)  return list[0];
            var item;
            for (item in list) {
                if (item.name == name)  return item;
            }
            return list[0];
        }

        $scope.selectedBranch = retrieveOrFirst($scope.branches,
            $location.search("branch"));
        if ($scope.selectedBranch != null) {
            $scope.selectedBuild = retrieveOrFirst($scope.selectedBranch.builds,
                $location.search("build"));
        }



        $scope.resetBranch = function() {
            $location.search("branch", null);
            $scope.resetBuild();
        }
        $scope.setBranch = function(branch) {
            $scope.selectedBranch = branch;
            $location.search("branch", $scope.selectedBranch.name);
        }
        $scope.resetBuild = function() {
            $location.search("build", null);
        }
        $scope.setBuild = function(build) {
            $scope.selectedBuild = build;
            $location.search("build", $scope.selectedBuild.name);
        };
    }
});