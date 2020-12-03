/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

export class Url {

    /**
     * Encode each variable uri component in the given template string.
     *
     * Usage example: Url.encodeComponents `foo/${parameter1}/bar/${parameter2}`
     *
     * This is a tag function to be used with template string, as explained here:
     * https://basarat.gitbook.io/typescript/future-javascript/template-strings#tagged-templates
     *
     * @param literals the string literals of the template string
     * @param components the uri components to be encoded
     */
    static encodeComponents(literals: TemplateStringsArray, ...components: string[]): string {
        let result = '';

        // interleave the literals with the placeholders
        for (let i = 0; i < components.length; i++) {
            result += literals[i];
            result += encodeURIComponent(components[i]);
        }

        // add the last literal
        result += literals[literals.length - 1];
        return result;
    }
}
