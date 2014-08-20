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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.configuration.LabelConfiguration;

@Path("/rest/labelconfigurations/")
public class LabelConfigurationsResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Map<String, LabelConfiguration> listLabelConfigurations() {
		Configuration configuration = ConfigurationDAO.getConfiguration();
		return configuration.getLabelConfigurations();
	}
	
	/**
	 * Creates a flat list of the label configurations map. This service exists for easier handling in angular.
	 * 
	 * @return map as list
	 */
	@Path("list")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<FlatLabelConfiguration> getLabelConfigurationsAsList() {
		Configuration configuration = ConfigurationDAO.getConfiguration();
		Map<String, LabelConfiguration> labelConfigurations = configuration.getLabelConfigurations();
		
		List<FlatLabelConfiguration> flatLabelConfigurations = new LinkedList<FlatLabelConfiguration>();
		for (Entry<String, LabelConfiguration> labelConfiguration : labelConfigurations.entrySet()) {
			flatLabelConfigurations.add(new FlatLabelConfiguration(labelConfiguration.getKey(), labelConfiguration
					.getValue()));
		}
		
		return flatLabelConfigurations;
	}
	
	@POST
	@Consumes({ "application/json", "application/xml" })
	public void updateLabelConfigurations(final Map<String, LabelConfiguration> labelConfigurations) {
		Configuration configuration = ConfigurationDAO.getConfiguration();
		configuration.setLabelConfigurations(labelConfigurations);
		ConfigurationDAO.updateConfiguration(configuration);
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
