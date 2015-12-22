/* scenarioo-server
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

package org.scenarioo.rest.design.stepSketch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SvgSanitizer {
	
	private static final Logger LOGGER = Logger.getLogger(SvgSanitizer.class);
	
	/**
	 * Sanitize the SVG, removing browser-specific crap.
	 * @param svg The SVG to be sanitized as a string.
	 * @return The sanitized SVG string.
	 */
	public static String sanitize(String svg){
		svg = removeXMLNS(svg);
		svg = removeDuplicateXmlnsXlink(svg);
		svg = removeNS1(svg);
		return svg;
	}

	/**
	 * Removes the extraneous XMLNS declarations that IE introduces.
	 */
	private static String removeXMLNS(String svg){
		String xmlns = "xmlns=\"http://www.w3.org/2000/svg\"";
		int count = 0, lastIndex = 0;
		while ((lastIndex = svg.indexOf(xmlns, lastIndex)) != -1) {
	        count++;
	        lastIndex += xmlns.length() - 1;
	    }
		if (count == 2){
			svg = svg.replaceFirst("xmlns=.*?\".*?\" ", "");
		}
		if (count > 2) {
			LOGGER.error("Unexpected number of xmlns declarations");
		}
		return svg;
	}
	
	/**
	 * Remove duplicate xmlns:xlink in the image tag (introduced by chrome)
	 */
	private static String removeDuplicateXmlnsXlink(final String svg) {
		String split = "<image";
		String[] svgParts = svg.split(split);
		String secondPart = svgParts[1];
		
		Pattern pattern = Pattern.compile("xmlns:xlink=\"[^\"]*\"");
		Matcher matcher = pattern.matcher(secondPart);
		
		int count = 0;
		while (matcher.find()) {
			count++;
		}

		if (count == 2) {
			secondPart = secondPart.replaceFirst("xmlns:xlink=\"[^\"]*\" ", "");
		}
		if (count > 2) {
			LOGGER.error("Unexpected number of xmlns:xlink declarations");
		}
		return svgParts[0] + split + secondPart;
	}

	/**
	 * Removes the NS1 declarations that IE introduces.
	 */
	private static String removeNS1(String svg){
		svg = svg.replaceAll("NS1:xmlns:xlink[^ ]* ", "");
		svg = svg.replaceAll("xmlns:NS1=[^ ]* ", "");
		return svg;
	}
}