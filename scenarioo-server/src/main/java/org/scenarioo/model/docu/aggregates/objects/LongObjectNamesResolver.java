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

package org.scenarioo.model.docu.aggregates.objects;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class to manage object names that are too long for file names (only for
 * file names! inside the real data the full names are always used!). Contains
 * an index for long file names that is stored in an XML as index on disk.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LongObjectNamesResolver {

	/**
	 * Key is the first 100 characters of the name.
	 */
	private final Map<String, UniqueShortObjectNamesForSameNamePrefix> uniqueShortNames = new HashMap<String, UniqueShortObjectNamesForSameNamePrefix>();

	/**
	 * Returns either the name itself or a unique shorter name in case of the
	 * name beeing longer than 100 characters.
	 * 
	 * @param fullLengthName
	 *            the name in full length
	 */
	public synchronized String resolveObjectFileName(final String fullLengthName) {
		if (fullLengthName.length() > 100) {
			return resolveLongName(fullLengthName);
		} else {
			return fullLengthName;
		}
	}

	private String resolveLongName(final String fullLenghtName) {
		UniqueShortObjectNamesForSameNamePrefix uniqueShortNamesForHead = getUniqueShortNamesForHead(fullLenghtName);
		return uniqueShortNamesForHead.resolveLongName(fullLenghtName);
	}

	private UniqueShortObjectNamesForSameNamePrefix getUniqueShortNamesForHead(
			final String fullLenghtName) {
		String headKey = getHeadKey(fullLenghtName);
		UniqueShortObjectNamesForSameNamePrefix result = uniqueShortNames
				.get(headKey);
		if (result == null) {
			result = new UniqueShortObjectNamesForSameNamePrefix();
			uniqueShortNames.put(headKey, result);
		}
		return result;
	}

	static String getHeadKey(final String fullLenghtName) {
		return fullLenghtName.substring(0, 100);
	}

}
