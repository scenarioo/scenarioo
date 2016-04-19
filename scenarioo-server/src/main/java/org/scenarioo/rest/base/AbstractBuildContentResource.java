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

package org.scenarioo.rest.base;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Base class for resources accessing a build using the {@link ScenarioDocuAggregationDao}.
 * 
 * This base class makes sure to initialize the DAO for proper object names resolving in case of generic objects access.
 */
public class AbstractBuildContentResource {
	
	private final static ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	
	/**
	 * Here we expect that the aliases in the buildIdentifier were already resolved before.
	 */
	public static AggregatedDocuDataReader getDAO(final BuildIdentifier buildIdentifier) {
		
		LongObjectNamesResolver longObjectNamesResolver = ScenarioDocuBuildsManager.INSTANCE
				.getLongObjectNameResolver(buildIdentifier);
		return new ScenarioDocuAggregationDao(configurationRepository.getDocumentationDataDirectory(),
				longObjectNamesResolver);
	}
	
}
