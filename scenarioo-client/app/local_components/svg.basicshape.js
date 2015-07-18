/* global SVG:false */

SVG.BasicShape = function (width, height, x, y, options) {
    var i, settings;

    settings = {
        text: 'Double-click to enter text'
        , hasText: false
        , isNote: false
        , fontSize: 14
        , fontColor: '#000'
        , fontFamily: '"Helvetica Neue",Helvetica,Arial,sans-serif'
        , fill: '#fff'
        , opacity: 1
        , stroke: '#000'
        , strokeWidth: '3'
        , padding: 10
        , halign: 'left'
        , valign: 'top'
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

    if (settings.hasText) {

        var self = this;
        console.log(self);

        if (settings.isNote) {
            this.isNote = true;
            self.createNotePolygon();
        }

        /* add note text */
        this.textareaId = this.id() + '-noteText';


        this.textNode = this.text(settings.text)
            .move(settings.padding, settings.padding)
            .fill(settings.fontColor)
            .attr('style', 'cursor:pointer;')
            .font({
                anchor: 'left'
                , size: settings.fontSize
                , family: settings.fontFamily
                , weight: '300'
            })
            .dblclick(function () {
                self.showTextWriteMode();
            });

        this.fobjNode = this.foreignObject()
            .front()
            .attr({
                width: width
                , height: height
                , class: 'noteToolText'
            })
            .appendChild('textarea', {
                textContent: settings.text
                , style: 'font-size:' + settings.fontSize
                , id: this.textareaId
                , onblur: function () {
                    self.showTextReadMode();
                }
                , onmouseup: function () {
                    self.unSelect();
                }
                , onclick: function () {
                    self.unSelect();
                }
            })
            .hide();
    }

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
        this.rect.attr({
            width: atts.width
            , height: atts.height
        });
        if (this.fobjNode) {
            this.fobjNode.attr({
                width: atts.width
                , height: atts.height
            });
        }
        if (this.isNote) {
            this.updateNotePolygon();
        }
    },

    showTextReadMode: function () {
        var currentText = document.getElementById(this.textareaId).value;

        if (currentText) {
            this.fobjNode.hide();
            this.textNode.text(currentText).show();
        }
    },

    showTextWriteMode: function () {
        this.fobjNode.show();
        this.textNode.hide();

        this.unSelect();
    },

    createNotePolygon: function () {
        this.polygon = this.polygon('0,0').fill('#f1c40f').opacity(0.8);
        this.polyline = this.polyline('0,0').fill('none').stroke({width: 1}).opacity(0.2);
        this.g = this.group().add(this.polygon).add(this.polyline);
    },

    updateNotePolygon: function () {
        this.polygon.plot(''
            + this.rect.width() - 15 + ',0 '
            + this.rect.width() + ',15 '
            + this.rect.width() + ',' + this.rect.height() + ' '
            + '0,' + this.rect.height() + ' '
            + '0,0'
        );

        this.polyline.plot([
                [this.rect.width() - 15, 0],
                [this.rect.width() - 15, 15],
                [this.rect.width(), 15]]
        );
    },

    // http://stackoverflow.com/questions/4561845/firing-event-on-dom-attribute-change
    registerAttrChangeEvent: function () {

        var self = this;

        window.MutationObserver = window.MutationObserver
        || window.WebKitMutationObserver
        || window.MozMutationObserver;

        if (window.MutationObserver || window.MutationObserver !== undefined) {
            // Find the element that you want to "watch"
            var target = self.node,
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
        } else {
            self.on('DOMAttrModified.shape', function () {
                self.update();
            });
        }
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
    },
    noteShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            opacity: 0
            , strokeWidth: '0'
            , hasText: true
            , isNote: true
        }));
    },
    textShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            opacity: 0
            , fill: '#fff'
            , strokeWidth: '0'
            , hasText: true
        }));
    },
    buttonShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            fill: '#3498db'
            , strokeWidth: '0'
            , hasText: true
            , halign: 'center'
            , valign: 'middle'
        }));
    },
    textShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            opacity: 0
            , fill: '#fff'
            , strokeWidth: '0'
            , hasText: true
        }));
    },
    buttonShape: function (width, height, x, y) {
        return this.put(new SVG.BasicShape(width, height, x, y, {
            fill: '#3498db'
            , strokeWidth: '0'
            , hasText: true
            , halign: 'center'
            , valign: 'middle'
        }));
    }

});

