SVG.extend(SVG.Element, {

    isSelected: false,
    selectOptions: {rotationPoint: false},

    selectToggle: function() {
        this.isSelected = !this.isSelected;
        this.select(this.isSelected, this.selectOptions);

        if(this.type !== 'svg') {
            this.draggable(this.isSelected);
            if (!this.isSelected) {
                this.resize('stop');
            } else {
                this.resize();
            }
        }
    },

    unSelect: function() {
        this.isSelected = false;
        this.select(false);

        if(this.type !== 'svg') {
            this.draggable(false);
            this.resize('stop');
        }
    }

});