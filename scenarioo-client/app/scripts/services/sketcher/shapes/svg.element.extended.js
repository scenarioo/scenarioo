/* global SVG:false */
/* eslint no-extra-semi:0*/

;(function () {
    SVG.extend(SVG.Nested, SVG.Shape, {

        isSelected: false,
        selectOptions: {rotationPoint: false},

        selectToggle: function () {
            this.isSelected = !this.isSelected;
            this.select(this.isSelected, this.selectOptions);

            this.draggable(this.isSelected);

            if (!this.isSelected) {
                this.resize('stop');
                this.fire('unselected');
            } else {
                this.resize();
                this.fire('selected');
            }

            return this;
        },

        unSelect: function () {
            this.isSelected = false;
            this.select(false);

            this.draggable(false);
            this.resize('stop');
            this.fire('unselected');
        }

    });
}).call(this);
