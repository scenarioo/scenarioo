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

import org.scenarioo.model.docu.entities.Scenario;

public class SearchableScenario implements SearchableObject {

	private Scenario scenario;
	private SearchableObjectContext searchableObjectContext;

	public SearchableScenario() {
	}

	public SearchableScenario(final Scenario scenario, final String usecaseName) {
		this.scenario = scenario;
		this.searchableObjectContext = new SearchableObjectContext(usecaseName);
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}

	public SearchableObjectContext getSearchableObjectContext() {
		return searchableObjectContext;
	}

	public void setSearchableObjectContext(final SearchableObjectContext searchableObjectContext) {
		this.searchableObjectContext = searchableObjectContext;
	}

	public static class SearchableObjectContext {
		private String usecase;

		public SearchableObjectContext() {
		}

		public SearchableObjectContext(final String usecase) {
			this.usecase = usecase;
		}

		public void setUsecase(final String usecase) {
			this.usecase = usecase;
		}

		public String getUsecase() {
			return usecase;
		}

	}
}
