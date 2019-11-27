export default function encodeUri(elements: string[]): string {
    return elements.map((element) => encodeURIComponent(element)).join('/');
}
