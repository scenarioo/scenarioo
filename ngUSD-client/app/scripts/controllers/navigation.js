'use strict';

NgUsdClientApp.controller('NavigationCtrl', function ($scope, $location, $cookieStore, BranchService, BuildStateService, AdminService, Config, $q, $rootScope) {

    //configuration
    var parameterUrl = {'build': Config.buildUrlParameter, 'branch': Config.branchUrlParameter};
    var parameterCookie = {'build': Config.buildUrlParameter, 'branch': Config.branchUrlParameter};
    var attributeRelativePath = {'build': 'linkName', 'branch': 'branch.name'};

    var statesToClass = BuildStateService.ListBuildStates();

    $scope.updating = false;

    $scope.branches = BranchService.findAllBranches();

    $scope.modalInfoOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-small'
    };

    var defaultBranch = Config.defaultBranch();
    var defaultBuild = Config.defaultBuild();
    $scope.applicationInformation = Config.applicationInformation();

    $q.all([$scope.branches, defaultBranch, defaultBuild]).then(function (results) {
        var branches = results[0];
        var defaultValue = {'branch': results[1], 'build': results[2]};

        function reload() {
            $scope.selectedBranch = retrieveOrCookieOrDefault(branches, 'branch');
            $scope.selectedBuild = retrieveOrCookieOrDefault($scope.selectedBranch.builds, 'build');
        }

        reload();

        // Trigger reload when query parameters change
        $scope.$watch(function () {
            return $location.search();
        }, function () {
            reload();
        });

        function getQueryParameter(paramName) {
            var params = $location.search();
            if (params === null || params.length === 0 || params[paramName] === null) {
                return null;
            }
            return {key: paramName, value: params[paramName]};
        }

        function retrieveOrCookieOrDefault(list, type) {
            // Get name
            var param = getQueryParameter(parameterUrl[type]);
            var value = null;
            if (param !== null) {
                value = param.value;
            } else {
                value = getCookie(parameterCookie[type]);
                if (value !== null) {
                    setParameter(type, value);
                } else {
                    value = defaultValue[type];
                }
            }

            // Retrieve object by name
            if (list === null || list.length === 0) {
                return null;
            }
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                if (resolve(item, attributeRelativePath[type]) === value) {
                    return item;
                }
            }

            // Invalid parameter
            if (param !== null) {
                console.warn('Invalid parameter for "' + type + '" with value "' + param + '".');
                setParameter(type, null);
            } else {
                console.warn('Invalid default value for branch (default value: "' + defaultValue.branch + '") or build (default value: "' + defaultValue.build + '").');
            }
            return list[0];
        }

        function getCookie(cookie) {
            return $cookieStore.get(cookie);
        }

        function setCookie(cookie, value) {
            $cookieStore.put(cookie, value);
        }

        function isCookiesEnabled() {
            return navigator.cookieEnabled;
        }


        function resolve(item, attributeName) {
            var attributeNames = attributeName.split('.');
            var subItem = item;
            for (var i = 0; i < attributeNames.length; i++) {
                subItem = subItem[attributeNames[i]];
            }
            return subItem;
        }

        $scope.setBranch = function (branch) {
            // Adrian: Is this line really necessary?
            $scope.selectedBranch = branch;
            setParameter('branch', (branch.branch.name === defaultValue.branch) ? null : branch.branch.name);
            setParameter('build', null);
            reload();
        };

        $scope.setBuild = function (branch, build) {
            // Adrian: Are those two lines really necessary?
            $scope.selectedBranch = branch;
            $scope.selectedBuild = build;
            setParameter('branch', (branch.branch.name === defaultValue.branch && build.linkName === defaultValue.build) ? null : branch.branch.name);
            setParameter('build', (build.linkName === defaultValue.build) ? null : build.build.name);
            reload();
        };

        function setParameter(type, value) {
            $location.search(parameterUrl[type], value);
            setCookie(parameterCookie[type], value);
        }

        statesToClass.then(function (result) {
            $scope.getStatusType = function (status) {
                return result[status];
            };
        });

        $scope.getDisplayName = function (build) {
            if (build.build.name !== build.linkName) {
                return build.linkName;
            } else {
                return 'Revision: ' + build.build.revision;
            }
        };

        $scope.updateData = function () {
            $scope.updating = true;
            var result = AdminService.updateData({});
            result.then(function () {
                $scope.updating = false;
            }, function () {
                $scope.updating = false;
            });
        };

        function isAFirstTimeUser() {
            var previouslyVisited = getCookie('previouslyVisited');
            if (previouslyVisited || !isCookiesEnabled()) {
                return false;
            }
            setCookie('previouslyVisited', true);
            return true;
        }

        $rootScope.infoModal = {showing: isAFirstTimeUser(), tab: null};

        $rootScope.openInfoModal = function (tabValue) {
            $rootScope.infoModal = {showing: true, tab: tabValue};
        };

        $rootScope.closeInfoModal = function () {
            $rootScope.infoModal = {showing: false};
        };
    });
});