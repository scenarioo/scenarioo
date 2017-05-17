function MDC($http, $sce, SelectedBranchAndBuildService, HostnameAndPort, $rootScope) {
    var markdown = this;

    function highlight() {
        var hls = document.getElementsByClassName('highlight');
        for (var i = 0; i < hls.length; i++){
            hljs.highlightBlock(hls.item(i));
        }
    }

    function replaceLocalLinksInContainer() {
        var containers = document.getElementsByClassName('md-container');
        for (var i = 0; i < containers.length; i++){
            var container = containers[i];

            var links = container.getElementsByTagName('a');
            replace(links,'href');
            links = container.getElementsByTagName('img');
            replace(links, 'src');
            for (var j = 0; j < links.length; j++) {
                var url = links[j].setAttribute('style', (links[j].getAttribute('style')?links[j].getAttribute('style'):'')+'max-width:100%;');
            }
        }
    }

    function replace(links, attr) {
        for (var j = 0; j < links.length; j++){
            var url = links[j].getAttribute(attr);
            console.log('found url', url);
            if (!url.startsWith('http')){
                if (url.startsWith('/')) url = url.substring(1, url.length);
                var newUrl = HostnameAndPort.forLink() + baseRestUrl + url;
                links[j].setAttribute(attr, newUrl);
            }
        }
    }

    showdown.setFlavor('github');
    var converter = new showdown.Converter();

    var baseRestUrl = 'rest/branch/' + SelectedBranchAndBuildService.selected().branch + '/build/' + SelectedBranchAndBuildService.selected().build +
        '/documentation/';
    var newUrl = markdown.file;
    if (newUrl.startsWith('/')) newUrl = newUrl.substring(1, newUrl.length);
    var url = HostnameAndPort.forLink()+ baseRestUrl + encodeURIComponent(newUrl);
    if (markdown.file.startsWith('http')){
        url = markdown.file;
    }

    $http({
        url: url,
        method: 'GET',
        responseType: 'text',
        transformResponse:null
    }).success(function (data){
        markdown.content = '';
        if (markdown.file.endsWith('.md')) {
            markdown.content = $sce.trustAsHtml('<div class="md-container">'+converter.makeHtml(data)+'</div>');
        }else{
            markdown.content = $sce.trustAsHtml(
                '<pre class="highlight"><code>'+data+'</code></pre>'
            );
        }
        angular.element(document).ready(function () {
            replaceLocalLinksInContainer();
            highlight();
        });
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
