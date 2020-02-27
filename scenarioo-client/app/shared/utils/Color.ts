export class Color {
    static isHexColorValid(color: string): boolean {
        // Color hexadecimal should have the format #000 or #0000000
        const hexadecimalColorRegexPattern = /^#([0-9A-F]{3}){1,2}$/i;
        return hexadecimalColorRegexPattern.test(color);
    }

    static getRandomHexColor(): string {
        const hexChars: string = '0123456789ABCDEF';
        const numberOfChars: number = 6;
        let randomHexColor: string = '#';
        for (let i = 0; i < numberOfChars; i++) {
            randomHexColor += hexChars[Math.floor(Math.random() * 16)];
        }
        return randomHexColor;
    }

    static getContrastingColor(hexColor: string): string {
        const lightColor = '#FFFFFF';
        const darkColor = '#000000';
        const [r, g, b] = [...Color.hexToRgb(hexColor)];
        // Determine the contrast of the color by calculating the brightness using YIQ values
        const brightness = (299 * r + 587 * g + 114 * b) / 1000;
        if (brightness > 127.5) {
            return darkColor;
        }
        return lightColor;
    }

    private static hexToRgb(hexColor: string): number[] {
        hexColor = Color.extendColorHex(hexColor);

        hexColor = hexColor.replace('#', '');

        const r: number = parseInt(hexColor.slice(0, 2), 16);
        const g: number = parseInt(hexColor.slice(2, 4), 16);
        const b: number = parseInt(hexColor.slice(4, 6), 16);
        return [r, g, b];
    }

    // Assumes that the hexColor parameter consists of 3 (#000) or 6 (#000000) values
    private static extendColorHex(hexColor: string): string {
        const hexGoalLength: number = 6;
        const hexColorValue: string = hexColor.replace('#', '');
        if (hexColorValue.length === hexGoalLength) {
            return hexColor;
        }

        // Convert e.g. #ABC to #AABBCC
        return '#' + hexColorValue
            .split('')
            .reduce((extendedHexColor: string[], currentHexValue: string) => {
                return extendedHexColor.concat([currentHexValue, currentHexValue]);
            }, [])
            .join('');
    }
}
