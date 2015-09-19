/* global SVG:false */
/* eslint no-extra-semi:0*/

;
(function () {
    SVG.DrawBorder = function (width, height, x, y) {

        this.constructor.call(this, SVG.create('rect'));

        this.size(width, height)
            .move(x, y)
            .fill({color: '#fff', opacity: 0})
            .stroke({color: '#000', width: 1, dasharray: '10,10', opacity: 0.8});
    };

    SVG.DrawBorder.prototype = new SVG.Shape();


    // Add methods
    SVG.extend(SVG.DrawBorder, {});

    // Extend SVG container
    SVG.extend(SVG.Container, {
        drawBorder: function (width, height, x, y) {
            return this.put(new SVG.DrawBorder(width, height, x, y));
        }

    });
}).call(this);

