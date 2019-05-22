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

package org.scenarioo.dao.search.model;

import java.util.Collections;
import java.util.List;

public class SearchResults {

	private final List<SearchableObject> results;
	private final long hits;
	private long totalHits;

	public static SearchResults noHits() {
		return new SearchResults(Collections.<SearchableObject> emptyList(), 0, 0);
	}

	public SearchResults(List<SearchableObject> results, long hits, long totalHits) {
		this.results = results;
		this.totalHits = totalHits;
		this.hits = hits;
	}

	public long getHits() {
		return hits;
	}

	public long getTotalHits() {
		return totalHits;
	}

	public List<SearchableObject> getResults() {
		return results;
	}
}
