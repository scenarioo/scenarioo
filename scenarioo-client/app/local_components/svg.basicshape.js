/* global SVG:false */

SVG.BasicShape = function(width, height, x, y, options) {
    var i, settings;

    settings = {
        text:           'this is some text'
        , hasText:      false
        , fontSize:     14
        , fontColor:    '#000'
        , fontFamily:   '"Helvetica Neue",Helvetica,Arial,sans-serif'
        , fill:         '#fff'
        , opacity:      1
        , stroke:       '#000'
        , strokeWidth:  '3'
    };

    options = options || {}
    for (i in options)
        settings[i] = options[i];

    this.constructor.call(this, SVG.create('svg'));

    this.attr.width = width;
    this.attr.height = height;
    this.attr.x = x;
    this.attr.y = y;

    this.rect = this.rect(width, height, 0, 0);
    this.rect.fill({ color: settings.fill, opacity: settings.opacity })
        .stroke({ color: settings.stroke, width: settings.strokeWidth });
};

SVG.BasicShape.prototype = new SVG.Nested();


// Add methods
SVG.extend(SVG.BasicShape, {

    updateChildren: function(width, height) {
        this.each(function() {
            this.attr({
                width: width
                , height: height
            });
        });
    },

    update: function() {
        var width = this.width();
        var height = this.height();
        this.updateChildren(width, height);
    }
});

// Extend SVG container
SVG.extend(SVG.Container, {
    // Add note method
    basicShape: function(width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y));
    },
    borderShape: function(width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            opacity: 0
            , stroke: '#e74c3c'
            , strokeWidth:  '5'
        }));
    }

});

