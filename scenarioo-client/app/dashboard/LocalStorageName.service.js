
angular.module('scenarioo.services').service('LocalStorageNameService', function () {
    var service = this;

    service.LATEST_VIEW_NAME = 'latestView';
    localStorage.setItem(service.LATEST_VIEW_NAME, 'feature');

    return service;
});
