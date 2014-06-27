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

package org.scenarioo.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utility class with helper methods for resource handling.
 */
public class ResourceUtils {
	
	private ResourceUtils() {
	}
	
	/**
	 * Close the passed resource and care about possible problems.
	 * 
	 * Is null pointer safe, by handling passed null parameter. Will throw a runtime exception if resource is not
	 * closeable.
	 * 
	 * @param resource
	 *            the resource to close (or null)
	 * @param resourceId
	 *            a string to identify the resource, will used in case of error for the error message only.
	 */
	public static void close(final Closeable resource, final String resourceId) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				throw new RuntimeException(
						"Could not close resource properly for unknown reason (unexpected exception): " + resourceId, e);
			}
		}
	}
	
}
