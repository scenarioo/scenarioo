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

package org.scenarioo.dao.search.dao;

import java.util.Collections;
import java.util.List;

public class SearchResultsDao {

	private final List<SearchDao> results;
	private final long hits;
	private long totalHits;

	public static SearchResultsDao noHits() {
		return new SearchResultsDao(Collections.<SearchDao>emptyList(), 0, 0);
	}

	public SearchResultsDao(List<SearchDao> results, long hits, long totalHits) {
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

	public List<SearchDao> getResults() {
		return results;
	}
}
