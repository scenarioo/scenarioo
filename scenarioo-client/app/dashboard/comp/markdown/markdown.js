function MDC($http, $sce) {
    var markdown = this;

    markdown.highlight = function () {
        var hls = document.getElementsByClassName("highlight");
        for (var i = 0; i < hls.length; i++){
            hljs.highlightBlock(hls.item(i));
        }
    };

    showdown.setFlavor('github');
    var converter = new showdown.Converter();
    $http.get(markdown.file).success(function (data){
        markdown.content = $sce.trustAsHtml(
            '<pre class="highlight"><code>'+data+'</code></pre>'
        );
        markdown.highlight();
        if (markdown.file.endsWith('.md')) {
            markdown.content = $sce.trustAsHtml(converter.makeHtml(data));
        }
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
