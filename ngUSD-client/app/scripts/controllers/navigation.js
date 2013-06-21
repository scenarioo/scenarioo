'use strict';

NgUsdClientApp
    .controller('NavigationCtrl', ['$scope', '$location', 'BranchService', 'Config', function ($scope, $location, BranchService, Config) {

    //configuration
    var parameterBuild = Config.buildUrlParameter;
    var parameterBranch = Config.branchUrlParameter;

    var defaultBuild = Config.buildDefaultValue;
    var defaultBranch = Config.branchDefaultValue;

    $scope.branches = BranchService.findAllBranches(postProcessing);

    function postProcessing() {
        reload();

        // Trigger reload when query parameters change
        $scope.$watch(function () { return $location.search(); }, function() {
            reload();
        });

        function reload() {
            var params = $location.search();
            var branchParam = getQueryParameter(params, parameterBranch);
            $scope.selectedBranch = retrieveOrDefault($scope.branches,
                branchParam, defaultBranch);
            if ($scope.selectedBranch != null) {
                var buildParam = getQueryParameter(params, parameterBuild);
                $scope.selectedBuild = retrieveOrDefault($scope.selectedBranch.builds,
                    buildParam, defaultBuild);
            }
        }
        function getQueryParameter(params, paramName) {
            if (params == null || params.length==0 || params[paramName]==null)  return null;
            return {key: paramName, value: params[paramName]};
        }

        function retrieveOrDefault(list, param, defaultValue) {
            var value=defaultValue;
            if (param != null) value=param.value;

            // Retrieve
            if (list == null || list.length==0)  return null;
            for (var i=0; i<list.length; i++) {
                var item = list[i];
                if (item["name"] == value)  return item;
            }
            // Invalid parameter
            if (param!=null)  {
                $location.search(param.key, null);
                console.warn("Invalid parameter '"+param.key+"' with value '"+param.value+"'.");
                param = null;
            } else {
                console.warn("Invalid default value for branch (default value: '"+defaultBranch+") or build (default value: '"+defaultBuild+"').");
            }
            return list[0];
        }

        $scope.setBranch = function(branch) {
            $scope.selectedBranch = branch;
            $location.search(parameterBranch, (branch.name == defaultBranch)? null : $scope.selectedBranch.name);
            $location.search(parameterBuild,  null);
            reload();
        }
        $scope.setBuild = function(branch, build) {
            $scope.selectedBuild = build;
            $location.search(parameterBranch,  (branch.name == defaultBranch && build.name == defaultBuild)? null :$scope.selectedBranch.name);
            $location.search(parameterBuild,  (build.name == defaultBuild)? null : $scope.selectedBuild.name);
            reload();
        };
    }


    $scope.getStatusType = function(status){
        switch (status) {
            case "successful": return "label label-success";
            case "failed":  return "label label-important";
            default: return "label";
        }
    }

    $scope.displayName = function(branch) {
        return branch.name !=null && branch.name.length!=0;
    }
}]);