angular.module('scenarioo').component('fileViewer', {
    templateUrl: 'dashboard/comp/file-viewer/file-viewer.html',
    controllerAs: 'viewer',
    bindings: {
        feature: '=',
    },
    controller: function () {
        var viewer = this;
        viewer.contains = function(feature, field) {
            return feature[field] != null;
        };
    } ,
});
