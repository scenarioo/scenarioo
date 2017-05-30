angular.module('scenarioo').component('markdown', {
    controllerAs:'markdown',
    template:'<div ng-bind-html="markdown.content"></div>',
    bindings:{
        file: '@'
    },
    controller:MDC
});

function MDC($http, $sce, SelectedBranchAndBuildService, HostnameAndPort) {
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
            replace(links,'href', container.attributes['data-refer'].value);
            links = container.getElementsByTagName('img');
            replace(links, 'src', container.attributes['data-refer'].value);
            for (var j = 0; j < links.length; j++) {
                links[j].setAttribute('style', (links[j].getAttribute('style')?links[j].getAttribute('style'):'')+'max-width:100%;');
            }
        }
    }

    function replace(links, attributes, reference) {
        for (var j = 0; j < links.length; j++){
            var url = links[j].getAttribute(attributes);
            if (!url.startsWith('http')){
                url = replaceFirstSlash(url);
                var newUrl = HostnameAndPort.forLink() + baseRestUrl + url + '&referer='+encodeURIComponent(reference);
                links[j].setAttribute(attributes, newUrl);
            }
        }
    }

    showdown.setFlavor('github');
    var converter = new showdown.Converter();

    var baseRestUrl = 'rest/branch/' + SelectedBranchAndBuildService.selected().branch + '/build/' + SelectedBranchAndBuildService.selected().build +
        '/documentation?&path=';
    var newUrl = markdown.file;
    newUrl = replaceFirstSlash(newUrl);
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
            markdown.content = $sce.trustAsHtml('<div class="md-container" data-refer="'+newUrl+'">'+converter.makeHtml(data)+'</div>');
        }else{
            markdown.content = $sce.trustAsHtml('<pre class="highlight"><code>'+data+'</code></pre>');
        }
        angular.element(document).ready(function () {
            replaceLocalLinksInContainer();
            highlight();
        });
    });

    function replaceFirstSlash(url) {
        if (url.startsWith('/')){
            url = url.substring(1, url.length);
        }
        return url;
    }
}



