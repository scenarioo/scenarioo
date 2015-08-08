/* global SVG:false */
/* eslint no-extra-semi:0*/

;
(function () {
    SVG.CompositeShape = function (width, height, x, y, options) {
        var i, settings;

        settings = {
            text: ''
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
            , startMode: 'VIEW'
            , class: 'composite-shape'
            , minWidth: 70
            , minHeight: 45
        };

        options = options || {};
        for (i in options)
            settings[i] = options[i];

        this.constructor.call(this, SVG.create('svg'));

        this.width(width);
        this.height(height);
        this.move(x, y);
        this.addClass(settings.class);

        this.minWidth = settings.minWidth;
        this.minHeight = settings.minHeight;
        this.fontSize = settings.fontSize;

        this.rect = this.rect(width, height, 0, 0);
        this.rect.fill({color: settings.fill, opacity: settings.opacity})
            .stroke({color: settings.stroke, width: settings.strokeWidth});

        if (settings.isNote) {
            this.isNote = true;
            this.createNotePolygon();
        }

        this.textNode = this.text(settings.text)
            .move(settings.padding, settings.padding)
            .fill(settings.fontColor)
            .attr('style', 'cursor:pointer;')
            .font({
                anchor: 'left'
                , size: settings.fontSize
                , family: settings.fontFamily
                , weight: '300'
            });

        this.registerAttrChangeEvent();
    };

    SVG.CompositeShape.prototype = new SVG.Nested();


    // Add methods
    SVG.extend(SVG.CompositeShape, {

        update: function () {
            this.rect.attr({
                width: this.width()
                , height: this.height()
            });

            if (this.isNote) {
                this.updateNotePolygon();
            }
        },

        showText: function () {
            this.textNode.show();
        },

        hideText: function () {
            this.textNode.hide();
        },

        setMinSizeIfSmaller: function () {
            if(this.width() < this.minWidth) {
                this.width(this.minWidth);
            }
            if(this.height() < this.minHeight) {
                this.height(this.minHeight);
            }
        },

        getText: function () {
            return this.textNode.text();
        },

        setText: function (text) {
            this.textNode.text(text);
        },

        edit: function (zoomFactor, offset) {
            zoomFactor = zoomFactor || 1;
            offset = offset || {x: 0, y: 0};

            var self = this,
                shapeEditNodeId = self.id() + '-edit',
                workspaceNode = $(self.node).closest('div');

            self.hideText();
            self.unSelect();

            var fontSize = this.fontSize * zoomFactor;

            $(workspaceNode).prepend('<div id="' + shapeEditNodeId + '" class="shapeTextWrapper">' +
                                        '<textarea class="shapeText" style="font-size:' + fontSize + 'px"></textarea>' +
                                    '</div>');

            $('#' + shapeEditNodeId).width(self.width() * zoomFactor)
                .height(self.height() * zoomFactor)
                .css('left', self.x() * zoomFactor + offset.x)
                .css('top', self.y() * zoomFactor + offset.y);


            $('#' + shapeEditNodeId + ' textarea').on('blur', function () {
                self.setText($(this).val());
                self.showText();
                //$(this).parent().remove();
            }).focus().val(self.getText());
        },

        view: function () {
            var shapeEditNodeId = this.id() + '-edit';
            this.setText($('#' + shapeEditNodeId + ' textarea').val());
            this.showText();
            $('#' + shapeEditNodeId).remove();
        },

        createNotePolygon: function () {
            this.polygon = this.polygon('0,0').fill('#f1c40f').opacity(0.8);
            this.polyline = this.polyline('0,0').fill('none').stroke({width: 1}).opacity(0.2);
        },

        updateNotePolygon: function () {
            this.polygon.plot([
                [this.rect.width() - 15, 0],
                [this.rect.width(), 15],
                [this.rect.width(), this.rect.height()],
                [0, this.rect.height()],
                [0, 0]
            ]);

            this.polyline.plot([
                [this.rect.width() - 15, 0],
                [this.rect.width() - 15, 15],
                [this.rect.width(), 15]
            ]);
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
                    observer = new MutationObserver(function () {
                        /** this is the callback where you
                         do what you need to do.
                         The argument is an array of MutationRecords where the affected attribute is
                         named "attributeName". There is a few other properties in a record
                         but I'll let you work it out yourself.
                         **/
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
        rectShape: function (width, height, x, y) {
            return this.put(new SVG.CompositeShape(width, height, x, y, {
                class: 'rect-shape'
            }));
        },
        borderShape: function (width, height, x, y) {
            return this.put(new SVG.CompositeShape(width, height, x, y, {
                opacity: 0
                , stroke: '#e74c3c'
                , strokeWidth: '5'
                , class: 'border-shape'
            }));
        },
        noteShape: function (width, height, x, y) {
            return this.put(new SVG.CompositeShape(width, height, x, y, {
                opacity: 0
                , strokeWidth: '0'
                , isNote: true
                , startMode: 'EDIT'
                , class: 'note-shape'
                , minWidth: 120
                , minHeight: 120
            }));
        },
        textShape: function (width, height, x, y) {
            return this.put(new SVG.CompositeShape(width, height, x, y, {
                opacity: 0
                , fill: '#fff'
                , strokeWidth: '0'
                , startMode: 'EDIT'
                , class: 'text-shape'
                , minWidth: 120
            }));
        },
        buttonShape: function (width, height, x, y) {
            return this.put(new SVG.CompositeShape(width, height, x, y, {
                fill: '#3498db'
                , strokeWidth: '0'
                , halign: 'center'
                , valign: 'middle'
                , text: 'abc'
                , class: 'button-shape'
            }));
        }

    });
}).call(this);

