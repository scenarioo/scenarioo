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
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.configuration.LabelConfiguration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestController
@RequestMapping("/rest/labelconfigurations")
public class LabelConfigurationsResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	@GetMapping
	public Map<String, LabelConfiguration> listLabelConfigurations() {
		Configuration configuration = configurationRepository.getConfiguration();
		return configuration.getLabelConfigurations();
	}

	/**
	 * Creates a flat list of the label configurations map. This service exists for easier handling in angular.
	 *
	 * @return map as list
	 */
	@GetMapping("list")
	public List<FlatLabelConfiguration> getLabelConfigurationsAsList() {
		Configuration configuration = configurationRepository.getConfiguration();
		Map<String, LabelConfiguration> labelConfigurations = configuration.getLabelConfigurations();

		List<FlatLabelConfiguration> flatLabelConfigurations = new LinkedList<FlatLabelConfiguration>();
		for (Entry<String, LabelConfiguration> labelConfiguration : labelConfigurations.entrySet()) {
			flatLabelConfigurations.add(new FlatLabelConfiguration(labelConfiguration.getKey(), labelConfiguration
					.getValue()));
		}

		return flatLabelConfigurations;
	}

	@PostMapping
	public void updateLabelConfigurations(@RequestBody final Map<String, LabelConfiguration> labelConfigurations) {
		Configuration configuration = configurationRepository.getConfiguration();
		configuration.setLabelConfigurations(labelConfigurations);
		configurationRepository.updateConfiguration(configuration);
		ScenarioDocuBuildsManager.INSTANCE.refreshBranchAliases();
	}

	@XmlRootElement
	static class FlatLabelConfiguration extends LabelConfiguration {
		private String name;

		public FlatLabelConfiguration() {
		}

		public FlatLabelConfiguration(final String name, final LabelConfiguration configuration) {
			this.name = name;
			setForegroundColor(configuration.getForegroundColor());
			setBackgroundColor(configuration.getBackgroundColor());
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}
	}

}
