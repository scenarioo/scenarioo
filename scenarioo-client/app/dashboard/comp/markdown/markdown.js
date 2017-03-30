function MDC($http, $sce) {
    var markdown = this;

    showdown.setFlavor('allOn');
    var converter = new showdown.Converter();
    $http.get(markdown.file).success(function (data){
        markdown.content = $sce.trustAsHtml(converter.makeHtml(data));
    });
}

angular.module('scenarioo').component('markdown', {
    controllerAs:'markdown',
    template:'<div ng-bind-html="markdown.content"></div>',
    bindings:{
        file: '@'
    },
    controller:MDC
});
