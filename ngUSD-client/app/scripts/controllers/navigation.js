'use strict';

NgUsdClientApp
    .controller('NavigationCtrl', ['$scope', '$location', 'BranchService', 'BuildStateService', 'Config', function ($scope, $location, BranchService, BuildStateService, Config) {

    //configuration
    var parameterBuild = Config.buildUrlParameter;
    var parameterBranch = Config.branchUrlParameter;

    var defaultBuild = Config.buildDefaultValue;
    var defaultBranch = Config.branchDefaultValue;

    var statesToClass = BuildStateService.ListBuildStates();

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
            $scope.selectedBranch = retrieveOrDefault($scope.branches, "branch.name",
                branchParam, defaultBranch);
            if ($scope.selectedBranch != null) {
                var buildParam = getQueryParameter(params, parameterBuild);
                $scope.selectedBuild = retrieveOrDefault($scope.selectedBranch.builds, "linkName",
                    buildParam, defaultBuild);
            }
        }
        function getQueryParameter(params, paramName) {
            if (params == null || params.length==0 || params[paramName]==null)  return null;
            return {key: paramName, value: params[paramName]};
        }

        function retrieveOrDefault(list, attributeName, param, defaultValue) {
            var value=defaultValue;
            if (param != null) value=param.value;

            // Retrieve
            if (list == null || list.length==0)  return null;
            for (var i=0; i<list.length; i++) {
                var item = list[i];
                if (resolve(item, attributeName) == value)  return item;
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

        function resolve(item, attributeName) {
            var attributeNames = attributeName.split(".");
            var subItem = item;
            for (var i=0; i<attributeNames.length; i++) {
                subItem = subItem[attributeNames[i]];
            }
            return subItem;
        }

        $scope.setBranch = function(branch) {
            $scope.selectedBranch = branch;
            $location.search(parameterBranch, (branch.branch.name == defaultBranch)? null : $scope.selectedBranch.branch.name);
            $location.search(parameterBuild,  null);
            reload();
        }
        $scope.setBuild = function(branch, build) {
            $scope.selectedBuild = build;
            $location.search(parameterBranch,  (branch.branch.name == defaultBranch && build.linkName == defaultBuild)? null :$scope.selectedBranch.branch.name);
            $location.search(parameterBuild,  (build.linkName == defaultBuild)? null : $scope.selectedBuild.build.name);
            reload();
        };
    }


    $scope.getStatusType = function(status){
        return statesToClass[status];
    }

    $scope.getDisplayName = function(build) {
        if (build.build.name != build.linkName) {
            return build.linkName;
        } else {
            return "Revision: "+build.build.revision;
        }

    }
}]);