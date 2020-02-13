import * as contrast from 'contrast/index';

export const isHexadecimalColorValid = (color: string): boolean => {
    // Color hexadecimal should have the format #000 or #0000000
    const hexadecimalColorRegexPattern = /^#([0-9A-F]{3}){1,2}$/i;
    return hexadecimalColorRegexPattern.test(color);
};

export const getContrastingColor = (hexColor: string): string => {
    const lightForegroundColor = '#FFF';
    const darkForegroundColor = '#000';
    if (contrast(hexColor) === 'light') {
        return darkForegroundColor;
    }
    return lightForegroundColor;
};
