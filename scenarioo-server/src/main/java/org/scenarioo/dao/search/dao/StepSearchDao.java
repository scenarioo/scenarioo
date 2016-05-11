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

import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.UseCase;

@SuppressWarnings("unused")
public class StepSearchDao {

    private StepDescription step;
    private MetaData _meta;

    public StepSearchDao() {
    }

    public StepSearchDao(StepDescription step, Page page, Scenario scenario, UseCase usecase) {
        this.step = step;
        this._meta = new MetaData(page.getName(), scenario.getName(), usecase.getName());
    }

    public StepDescription getStep() {
        return step;
    }

    public void setStep(StepDescription step) {
        this.step = step;
    }

    public void set_meta(MetaData _meta) {
        this._meta = _meta;
    }

    public MetaData get_meta() {
        return _meta;
    }

    private static class MetaData {
        private String page;
        private String usecase;
        private String scenario;

        public MetaData() {
        }

        MetaData(String page, final String scenario, final String usecase) {
            this.page = page;
            this.scenario = scenario;
            this.usecase = usecase;
        }

        public String getPage() {
            return page;
        }

        public String getScenario() {
            return scenario;
        }

        public String getUsecase() {
            return usecase;
        }
    }
}
