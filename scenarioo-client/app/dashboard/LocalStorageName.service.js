
angular.module('scenarioo').service('LocalStorageNameService', function () {
    var service = this;

    service.LATEST_VIEW_NAME = "latestView";
    localStorage.setItem(service.LATEST_VIEW_NAME, "feature");


});
