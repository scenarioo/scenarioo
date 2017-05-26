angular.module('scenarioo').component('fileViewer', {
    templateUrl: 'dashboard/comp/file-viewer/file-viewer.html',
    controllerAs: 'viewer',
    bindings: {
        feature: '=',
    },
    controller: function (FeatureService) {
        var viewer = this;
        viewer.contains = FeatureService.contains;
    } ,
});
