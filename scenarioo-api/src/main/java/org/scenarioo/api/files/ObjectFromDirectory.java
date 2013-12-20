/* scenarioo-api
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

package org.scenarioo.api.files;

import lombok.Getter;

/**
 * Contains any Scenario Docu object entity read from a directory containing also the name of the directory.
 */
public class ObjectFromDirectory<T> {
	
	@Getter
	private final T object;
	@Getter
	private final String directoryName;
	
	public ObjectFromDirectory(final T object, final String directoryName) {
		this.object = object;
		this.directoryName = directoryName;
	}
	
}
