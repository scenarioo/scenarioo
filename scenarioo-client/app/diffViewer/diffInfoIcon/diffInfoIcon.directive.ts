import {Component, ElementRef, HostListener, Input, OnChanges, SimpleChanges} from "@angular/core";

@Component ({
    selector: 'sc-diff-info-icon',
    template: require('./diffInfoIcon.component.html')

})

export class DiffInfoIconDirective implements OnChanges{
    changedPercentage = '';
    addedPercentage = '';
    removedPercentage = '';
    unchangedPercentage = '';

    restrict;
    @Input()diffInfo; // TODO: Get values from data source -> Where?
    @Input()elementType;
    @Input()childElementType;

    // controller: DiffInfoIconController;
    // controllerAs: 'vm';
    // bindToController: true;

    constructor(private el: ElementRef){
    }

    ngOnChanges(changes: SimpleChanges): void {
        console.log('Change detected.\nCalling initValues() method of DiffInfoIconDirectitve');
        this.initValues();
    }

    @HostListener('mouseenter') onMouseEnter(){

    }

    private highlight(color: string) {
        this.el.nativeElement.style.backgroundColor = color;
    }

    displayPercentageChanged() {
        if(!this.diffInfo || this.diffInfo.isAdded || this.diffInfo.isRemoved) {
            return false;
        }
        return true;
    }

    private asPercentageString(value): string {
        return value + '%'
    }

    initValues(): void {
        if (!this.diffInfo) {
            return;
        }
        this.changedPercentage = this.asPercentageString(0);
        this.addedPercentage = this.asPercentageString(0);
        this.removedPercentage = this.asPercentageString(0);
        this.unchangedPercentage = this.asPercentageString(0);

        const roundedChangeRate = Math.ceil(this.diffInfo.changeRate);

        if (this.diffInfo.isAdded) {
            this.addedPercentage = this.asPercentageString(100);
            // TODO $sce -> $sce.trustAsHtml('This ' + vm.elementType + ' has been added');
        } else if (this.diffInfo.isRemoved) {
            this.removedPercentage = this.asPercentageString(100);
            // TODO $sce -> $sce.trustAsHtml('This ' + vm.elementType + ' has been removed');
        } else if (roundedChangeRate === 0) {
            this.unchangedPercentage = this.asPercentageString(100);
            // TODO $sce -> $sce.trustAsHtml('This ' + vm.elementType + ' has no changes');
        } else {
            const totalChangedChildElements = this.diffInfo.added + this.diffInfo.removed + this.diffInfo.changed;
            if (totalChangedChildElements && totalChangedChildElements > 0) {
                const addedPercentage = (this.diffInfo.added / totalChangedChildElements) * roundedChangeRate;
                const removedPercentage = (this.diffInfo.removed / totalChangedChildElements) * roundedChangeRate;
                const changedPercentage = roundedChangeRate - addedPercentage - removedPercentage;

                this.changedPercentage = this.asPercentageString(changedPercentage);
                this.addedPercentage = this.asPercentageString(addedPercentage);
                this.removedPercentage = this.asPercentageString(removedPercentage);
                this.unchangedPercentage = this.asPercentageString(100 - changedPercentage - addedPercentage - removedPercentage);
            }

            const changedInfoText = this.buildChangedInfoText(this.diffInfo, this.elementType, this.childElementType);
            // TODO $sce -> this.infoText = $sce.trustAsHtml(changedInfoText);
        }
    }

    private buildChangedInfoText(diffInfo, elementType, childElementType): string {
        let changedInfoText = '';
        // TODO -> let changedInfoText = $filter('scRoundUp')(diffInfo.changeRate) + '% of this ' + elementType + ' has changed:';

        if(diffInfo.changed > 0) {
            changedInfoText += '<br />';
            changedInfoText += '<span class="square changed"></span>';
            changedInfoText += diffInfo.changed + ' ' + childElementType + (diffInfo.changed === 1 ? '' : 's') + ' changed';
        }
        if(diffInfo.added > 0) {
            changedInfoText += '<br />';
            changedInfoText += '<span class="square added"></span>';
            changedInfoText += diffInfo.added + ' ' + childElementType + (diffInfo.added === 1 ? '' : 's') + ' added';
        }
        if(diffInfo.removed > 0) {
            changedInfoText += '<br />';
            changedInfoText += '<span class="square removed"></span>';
            changedInfoText += diffInfo.removed + ' ' + childElementType + (diffInfo.removed === 1 ? '' : 's') + ' removed';
        }
        return changedInfoText;
    }
}

