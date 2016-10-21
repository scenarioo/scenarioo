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

import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;

public class StepSearchDao implements SearchDao {

	private Step step;
	private MetaData _meta;

    public StepSearchDao() {
    }

    public StepSearchDao(final Step step, final StepLink stepLink, final Scenario scenario, final UseCase usecase) {
        this.step = step;
        this._meta = new MetaData(stepLink, scenario.getName(), usecase.getName());
    }

    public Step getStep() {
        return step;
    }

    public void setStep(final Step step) {
        this.step = step;
    }

	public void set_meta(final MetaData _meta) {
        this._meta = _meta;
    }

    public MetaData get_meta() {
        return _meta;
    }

	public static class MetaData {
		private StepLink stepLink;
        private String usecase;
        private String scenario;

        public MetaData() {
        }

        MetaData(final StepLink stepLink, final String scenario, final String usecase) {
			this.stepLink = stepLink;
            this.scenario = scenario;
            this.usecase = usecase;
        }

		public StepLink getStepLink() {
			return stepLink;
		}

		public void setStepLink(final StepLink stepLink) {
			this.stepLink = stepLink;
		}

		public String getUsecase() {
			return usecase;
		}

		public void setUsecase(final String usecase) {
			this.usecase = usecase;
		}

		public String getScenario() {
			return scenario;
		}

		public void setScenario(final String scenario) {
			this.scenario = scenario;
		}
	}
}
