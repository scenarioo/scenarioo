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

package org.scenarioo.rest.configuration;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.configuration.BranchAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/branchaliases")
public class BranchAliasesResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	@GetMapping
	public List<BranchAlias> listBranchAliases() {
		Configuration configuration = configurationRepository.getConfiguration();
		return configuration.getBranchAliases();
	}

	@PostMapping
	public void updateBranchAliases(final List<BranchAlias> branchAliases) {
		Configuration configuration = configurationRepository.getConfiguration();
		configuration.setBranchAliases(branchAliases);
		configurationRepository.updateConfiguration(configuration);
		ScenarioDocuBuildsManager.INSTANCE.refreshBranchAliases();
	}

}
