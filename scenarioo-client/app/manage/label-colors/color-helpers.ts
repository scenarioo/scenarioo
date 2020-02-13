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

const contrast = (hexColor: string) => {
    const [r, g, b] = [...hexToRgb(hexColor)];
    console.log(r + ' ' + g + ' ' + b);
    const hsp = Math.sqrt(0.299 * r * r + 0.587 * g * g + 0.114 * b * b);
    if (hsp > 127.5) {
        return 'light';
    }
    return 'dark';
};

const hexToRgb = (hexColor: string): number[] => {
    if (hexColor.startsWith('#')) {
        hexColor = hexColor.replace('#', '');
    }

    if (hexColor.length === 3) {
        hexColor = hexColor
            .split('')
            .reduce((accum, value) => {
                return accum.concat([value, value])
            }, [])
            .join('');
    }

    console.log(hexColor);

    const r: number = parseInt(hexColor.slice(0, 2), 16);
    const g: number = parseInt(hexColor.slice(2, 4), 16);
    const b: number = parseInt(hexColor.slice(4, 6), 16);
    return [r, g, b];
};
