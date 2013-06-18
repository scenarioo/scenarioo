/**
 * Created with JetBrains WebStorm.
 * User: ngUSD
 * Date: 6/17/13
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */

function NavigationCtrl($scope) {
    $scope.branches = [{'id': 'trunk', builds: ['Build 1', 'Build 2'], current: 'Build 1'} , {id: 'februar'}];
}