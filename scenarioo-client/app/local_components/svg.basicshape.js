/* global SVG:false */

/*SVG.BasicShape = SVG.invent({
    // Define the type of element that should be created
    create: 'svg'

    // Specify from which existing class this shape inherits
    , inherit: SVG.Container

    // Add custom methods to invented shape
    , extend: {
        // Create method to proportionally scale the rounded corners
        size: function(width, height) {
            return this.attr({
                width:  width
                , height: height
                , rx:     height / 5
                , ry:     height / 5
            })
        },
        width; function(width) {
                return
}
    }

    // Add method to parent elements
    , construct: {
        // Create a rounded element
        basicShape: function(width, height, x, y) {
            return this.put(new SVG.BasicShape).size(width, height);
        }

    }
})*/


SVG.BasicShape = function(width, height, x, y, options) {
    var i, settings;

    settings = {
        text:         'this is some text'
        , fontsize:     14
        , fontcolor:    '#000'
        , fontfamily:   '"Helvetica Neue",Helvetica,Arial,sans-serif'
        , fill:         '#fff'
        , stroke:       '#000'
        , strokewidth:  '2'
    };

    options = options || {}
    for (i in options)
        settings[i] = options[i];

    //this.constructor.call(this, SVG.create('rect'));
    this.constructor.call(this, SVG.create('svg'));

    //this.size(width, height).move(x, y);
    //this.move(x, y);
    this.attr.width = width;
    this.attr.height = height;
    this.x = x;
    this.y = y;
    this.rect = this.rect(width, height, 0, 0);

    this.rect.fill({ color: settings.fill, opacity: 1 })
        .stroke({ color: settings.stroke, width: 3 });


};

SVG.BasicShape.prototype = new SVG.Container();


// Add methods
SVG.extend(SVG.BasicShape, {
    /*attr: function(attributes) {
        console.log(attributes);
        console.log(this);

        this.width = attributes.width;
        this.height = attributes.height;
        this.x = attributes.x;
        this.y = attributes.y;

        this.setAttribute('width', attributes.width);

        console.log(this.rect);

        this.rect.width = attributes.width;
        this.rect.height = attributes.height;

        return this;
    },*/

    update: function() {
        this.rect.size(this.width(), this.height());
        return this;
    },

    showText: function() {}
});

// Extend SVG container
SVG.extend(SVG.Container, {
    // Add note method
    basicShape: function(width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y));
    }

});

