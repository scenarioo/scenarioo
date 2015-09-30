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

package org.scenarioo.model.design.entities;

public enum IssueStatus {

	/**
	 * Status for new and not yet solved issues.
	 */
	OPEN("open"),

	/**
	 * Status for resolved issues.
	 */
	CLOSED("closed");

	IssueStatus(final String keyword) {
		this.keyword = keyword;
	}

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public static String toKeywordNullSafe(final IssueStatus status) {
		if (status == null) {
			return null;
		}
		else {
			return status.getKeyword();
		}
	}

}