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
import org.scenarioo.model.docu.entities.UseCase;

@SuppressWarnings("unused")
public class PageSearchDao {

    private Page page;
    private MetaData _meta;

    public PageSearchDao() {
    }

    public PageSearchDao(Page page, Scenario scenario, UseCase usecase) {
        this.page = page;
        this._meta = new MetaData(scenario.getName(), usecase.getName());
    }

    public Page getPage() {
        return page;
    }

    public void set_meta(MetaData _meta) {
        this._meta = _meta;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public MetaData get_meta() {
        return _meta;
    }

    public static class MetaData {
        private String usecase;
        private String scenario;

        @SuppressWarnings("unused")
        public MetaData() {
        }

        MetaData(final String scenario, final String usecase) {
            this.scenario = scenario;
            this.usecase = usecase;
        }

        public String getScenario() {
            return scenario;
        }

        public String getUsecase() {
            return usecase;
        }
    }
}
