angular.module('scenarioo').component('treeNav', {
    templateUrl: 'dashboard/treeNav/treeNav.html',
    controllerAs: 'treeNav',
    bindings: {
        rootFeature:'=',
        currentFeature:'=',
        clickFeature:'=',
        size:'='
    },
    controller: function () {
        var treeNav = this;

        function getOffset(el) {
            el = el.getBoundingClientRect();
            return {
                left: el.left + window.scrollX,
                top: el.top + window.scrollY
            }
        }

        var elem = document.getElementsByClassName('fuu')[0];

        var st = 'min-width: 0px;white-space: nowrap;overflow: hidden;';

        elem.setAttribute('style','width: '+ (treeNav.size) +'px; '+st);
        localStorage.setItem('MAV_SIZE_LEFT', treeNav.size);

        var resizing = false;
        document.addEventListener('mousemove', function (event) {
            if (resizing){
                elem.setAttribute('style','width: '+ (event.clientX -getOffset(elem).left) +'px; '+st);
                localStorage.setItem('MAV_SIZE_LEFT', elem.offsetWidth);
            }
        });
        document.addEventListener('mouseup', function (event) {
            resizing = false;
        });
        treeNav.resize = function () {
            resizing = true;
        }
    }
});
