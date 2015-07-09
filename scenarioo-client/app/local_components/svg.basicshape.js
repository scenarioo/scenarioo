/* global SVG:false */

SVG.BasicShape = function (width, height, x, y, options) {
    var i, settings;

    settings = {
        text: 'this is some text'
        , hasText: false
        , fontSize: 14
        , fontColor: '#000'
        , fontFamily: '"Helvetica Neue",Helvetica,Arial,sans-serif'
        , fill: '#fff'
        , opacity: 1
        , stroke: '#000'
        , strokeWidth: '3'
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
    this.rect.fill({color: settings.fill, opacity: settings.opacity})
        .stroke({color: settings.stroke, width: settings.strokeWidth});
};

SVG.BasicShape.prototype = new SVG.Nested();


// Add methods
SVG.extend(SVG.BasicShape, {

    update: function () {
        var atts = {
            width: this.width()
            , height: this.height()
        };
        this.updateChildren(atts);
    },

    updateChildren: function (atts) {
        this.each(function () {
            this.attr({
                width: atts.width
                , height: atts.height
            });
        });
    },

    // http://stackoverflow.com/questions/4561845/firing-event-on-dom-attribute-change
    registerAttrChangeEvent: function () {

        var self = this;

        window.MutationObserver = window.MutationObserver
        || window.WebKitMutationObserver
        || window.MozMutationObserver;
        // Find the element that you want to "watch"
        var target = document.querySelector('#' + self.id()),
        // create an observer instance
        observer = new MutationObserver(function (mutation) {
            /** this is the callback where you
             do what you need to do.
             The argument is an array of MutationRecords where the affected attribute is
             named "attributeName". There is a few other properties in a record
             but I'll let you work it out yourself.
             **/
            //console.log(mutation);
            self.update();
        }),
            // configuration of the observer:
            config = {
                attributes: true // this is to watch for attribute changes.
            };

        // pass in the element you wanna watch as well as the options
        observer.observe(target, config);

        // later, you can stop observing
        // observer.disconnect();
    }
});

// Extend SVG container
SVG.extend(SVG.Container, {
    // Add note method
    basicShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y));
    },
    borderShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            opacity: 0
            , stroke: '#e74c3c'
            , strokeWidth: '5'
        }));
    }

});

