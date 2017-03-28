function MDC($http, $sce) {
    var markdown = this;

    var http = $http;

    console.log('fuuuu');

    var converter = new showdown.Converter();
    http.get('dashboard/test.md').success(function (data){
        console.log(data);
        markdown.content = $sce.trustAsHtml(converter.makeHtml(data));
    });
}

angular.module('scenarioo').component('markdown', {
    controllerAs:'markdown',
    template:'<div ng-bind-html="markdown.content"></div>',
    controller:MDC
});
