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

import org.scenarioo.model.docu.entities.Scenario;

@SuppressWarnings("unused")
public class ScenarioSearchDao {

	private Scenario scenario;
	private MetaData _meta;


	public ScenarioSearchDao() {
	}

	public ScenarioSearchDao(final Scenario scenario, final String usecaseName) {
		this.scenario = scenario;
		this._meta = new MetaData(usecaseName);
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}

	public MetaData get_meta() {
		return _meta;
	}

	public void set_meta(final MetaData _meta) {
		this._meta = _meta;
	}

	public static class MetaData {
		private String usecase;

		public MetaData() {
		}

		public MetaData(final String usecase) {
			this.usecase = usecase;
		}

		public String getUsecase() {
			return usecase;
		}

	}
}