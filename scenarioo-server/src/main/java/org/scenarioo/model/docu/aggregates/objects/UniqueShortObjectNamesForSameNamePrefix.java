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

/**
 * Just an internal helper class for {@link LongObjectNamesResolver} that stores all unique object names for all long
 * names starting with the same name prefix (the head of the name consisting of the first 100 characters). This object
 * stores only the rest of the names (the tail) as a key.
 */
class UniqueShortObjectNamesForSameNamePrefix {
	
	/**
	 * Key is the rest of the name (after first 100 characters). The value in the second map is the unique short name to
	 * use for filenames for this object name.
	 */
	private Map<String, String> uniqueShortNames = new HashMap<String, String>();
	
	public String resolveLongName(final String fullLenghtName) {
		String tailKey = getTailKey(fullLenghtName);
		String uniqueShortName = uniqueShortNames.get(tailKey);
		if (uniqueShortName == null) {
			return createNewUniqueShortName(fullLenghtName);
		}
		else {
			return uniqueShortName;
		}
		
	}
	
	private String createNewUniqueShortName(final String fullLenghtName) {
		int indexOfCollision = uniqueShortNames.size();
		String result = LongObjectNamesResolver.getHeadKey(fullLenghtName) + "-" + indexOfCollision;
		uniqueShortNames.put(getTailKey(fullLenghtName), result);
		return result;
	}
	
	private String getTailKey(final String fullLenghtName) {
		return fullLenghtName.substring(100);
	}
	
}