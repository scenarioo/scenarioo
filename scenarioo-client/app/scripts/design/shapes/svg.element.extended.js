/* global SVG:false */
/* eslint no-extra-semi:0*/

;(function () {
    SVG.extend(SVG.Nested, SVG.Shape, {

        isSelected: false,
        selectOptions: {rotationPoint: false, deepSelect: true},

        selectToggle: function () {
            this.isSelected = !this.isSelected;

            if(this instanceof SVG.ArrowShape) {
                this.line.select(this.selectOptions);
            } else {
                this.select(this.selectOptions);
            }

            this.draggable(this.isSelected);

            if (this.isSelected) {
                this.resize();
                this.fire('selected');
            } else {
                this.resize('stop');
                this.fire('unselected');
            }

            return this;
        },

        unSelect: function () {
            this.isSelected = false;

            if(this instanceof SVG.ArrowShape) {
                this.line.select(false, this.selectOptions);
            } else {
                this.select(false, this.selectOptions);
            }

            this.draggable(false);
            this.resize('stop');
            this.fire('unselected');
        }

    });
}).call(this);
