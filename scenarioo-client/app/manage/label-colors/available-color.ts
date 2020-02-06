export class AvailableColor {

    constructor(private _backgroundColor: string, private _foregroundColor: string) {
    }

    get foregroundColor(): string {
        return this._foregroundColor;
    }

    get backgroundColor(): string {
        return this._backgroundColor;
    }
}
