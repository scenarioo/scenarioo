SVG.extend(SVG.Element, {

    isSelected: false,

    selectToggle: function() {
        this.isSelected = !this.isSelected;
        this.select(this.isSelected);
    },

    unSelect: function() {
        this.isSelected = false;
        this.select(false);
    }

});