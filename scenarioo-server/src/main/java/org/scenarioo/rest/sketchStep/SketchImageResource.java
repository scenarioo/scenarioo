/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
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

package org.scenarioo.rest.sketchStep;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.design.aggregates.ScenarioSketchAggregationDAO;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.sketchStep.logic.ProposalLoader;
import org.scenarioo.rest.sketchStep.logic.SketchStepIndexResolver;
import org.scenarioo.rest.sketchStep.logic.SketchStepLoader;
import org.scenarioo.rest.step.logic.LabelsQueryParamParser;
import org.scenarioo.utils.design.readers.DesignReader;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/sketchstep/{sketchStepIndex}/image/{sketchFileName}")
public class SketchImageResource {

	private static final Logger LOGGER = Logger.getLogger(SketchImageResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();
	private final ScenarioSketchAggregationDAO aggregatedDataReader = new ScenarioSketchAggregationDAO(
			configurationRepository.getDesignDataDirectory(), longObjectNamesResolver);

	private final LabelsQueryParamParser labelsQueryParamParser = new LabelsQueryParamParser();
	private final ProposalLoader proposalLoader = new ProposalLoader(aggregatedDataReader);
	private final SketchStepIndexResolver sketchStepIndexResolver = new SketchStepIndexResolver();
	private final SketchStepLoader sketchStepLoader = new SketchStepLoader(proposalLoader, sketchStepIndexResolver);

	// todo: ProposalReader (?)
	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
			configurationRepository.getDesignDataDirectory());

	private final DesignFiles files = new DesignFiles(configurationRepository.getDesignDataDirectory());
	private final DesignReader reader = new DesignReader(configurationRepository.getDesignDataDirectory());

	/**
	 * Get a step with all its data (meta data, html, ...) together with additional calculated navigation data
	 */
	@GET
	@Produces({ "image/svg+xml" })
	public Object loadSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("sketchStepIndex") final int sketchStepIndex) {

		LOGGER.info("Loading a sketch image");
		return files.getSVGFile(branchName, issueId, scenarioSketchId, "sketch.svg");

	}
}