export const isHexadecimalColorValid = (color: string): boolean => {
    // Color hexadecimal should have the format #000 or #0000000
    const hexadecimalColorRegexPattern = /^#([0-9A-F]{3}){1,2}$/i;
    return hexadecimalColorRegexPattern.test(color);
};

export const getRandomHexadecimalColor = (): string => {
    const hexChars: string = '0123456789ABCDEF';
    const numberOfChars: number = 6;
    let randomHexColor: string = '#';
    for (let i = 0; i < numberOfChars; i++) {
        randomHexColor += hexChars[Math.floor(Math.random() * 16)];
    }
    return randomHexColor;
};

export const getContrastingColor = (hexColor: string): string => {
    const lightColor = '#FFF';
    const darkColor = '#000';
    const [r, g, b] = [...hexToRgb(hexColor)];
    // Determine the contrast of the color by calculating the brightness using YIQ values
    const luminance = (299 * r + 587 * g + 114 * b) / 1000;
    if (luminance > 127.5) {
        return darkColor;
    }
    return lightColor;
};

const hexToRgb = (hexColor: string): number[] => {
    hexColor = extendColorHex(hexColor);

    hexColor = hexColor.replace('#', '');

    const r: number = parseInt(hexColor.slice(0, 2), 16);
    const g: number = parseInt(hexColor.slice(2, 4), 16);
    const b: number = parseInt(hexColor.slice(4, 6), 16);
    return [r, g, b];
};

const extendColorHex = (hexColor: string): string => {
    const hexGoalLength: number = 6;
    const hexColorValue: string = hexColor.replace('#', '');
    if (hexColorValue.length === hexGoalLength) {
      return hexColor;
    }

    return '#' + hexColorValue
        .split('')
        .reduce((fullHexColor: string[], currentValue: string) => {
            return fullHexColor.concat([currentValue, currentValue]);
        }, [])
        .join('');
};
