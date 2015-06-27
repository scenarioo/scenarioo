SVG.Note = function(width, height, options) {
    var i, settings

    /* set defaults */
    settings = {
        padding:        10
        , fontsize:     14
        , fontsizeta:   12
        , fontcolor:    '#000'
        , font:         'sans-serif'
        , bgcolor:      '#ddd'
    }

    /* merge options */
    options = options || {}
    for (i in options)
        settings[i] = options[i]


    /* create nested svg element */
    this.constructor.call(this, SVG.create('svg'))

    /* set attributes */
    this.size(width, height)

    /* create background (note sheet) */
    this.background = this.rect(width, height)
        .fill(settings.bgcolor)


    /* add note text */
    var notetxt = 'Das ist der Inhalt meiner Notiz. Hier kann einiges stehen. Wieviel genau, muss noch entschieden werden.';
    var lines = convertTextToLines(notetxt);

    var textNode = null;
    var fobjNode = null;

    textNode = this.notetxt = this.text(function(add) {
            convertTextToTextNode(add, notetxt)
        })
        .move(settings.padding, settings.padding)
        .fill(settings.fontcolor)
        .attr('style', 'cursor:pointer;')
        .font({
            anchor: 'left'
            , size:   settings.fontsize
            , family: settings.font
            , weight: '300'
        })
        .dblclick(function() {
            this.hide()
            fobjNode.show()
        })

    fobjNode = this.fobj = this.foreignObject()
        .front()
        .hide()
        .attr({
            width: width - 2 * settings.padding
            , height: height - 2 * settings.padding
            , class: 'noteToolText'
        })
        .move(settings.padding, settings.padding)
        .appendChild('textarea', {
            textContent: convertLinesToText(textNode)
            , style: 'font-size:' + settings.fontsizeta
        })
        .appendChild('button', {
            id: 'noteOK'
            , textContent: 'OK'
        })

    /*document.getElementById('noteOK').on('click', function(){
        console.log('ok button clicked!')
        fobjNode.hide()
        console.log(fobjNode.getChild(0).innerText)

        textNode.text(function(add) {
            convertTextToTextNode(add, fobjNode.getChild(0).innerText)
        }).show()

    });*/

    function convertTextToLines(text) {
        var charsPerLine = Math.floor(width / settings.fontsize * 1.6);
        console.log(charsPerLine);
        var re = new RegExp('(.|[\r\n]){1,' + charsPerLine + '}', 'g');

        return text.match(re);
    }

    function convertLinesToText(textNode) {
        var tspans = textNode.lines().members;
        var text = '';

        for(i in tspans) {
            text += tspans[i].content;
        }
        return text;
    }

    function convertTextToTextNode(add, text) {
        var lines = convertTextToLines(text)
        if(lines != null) {
            for(i in lines) {
                add.tspan(lines[i]).newLine()
            }
        }
    }

}

SVG.Note.prototype = new SVG.Container

// Add methods
SVG.extend(SVG.Note, {

})

// Extend SVG container
SVG.extend(SVG.Container, {
    // Add note method
    note: function(width, height) {
        return this.put(new SVG.Note(width, height))
    }

})