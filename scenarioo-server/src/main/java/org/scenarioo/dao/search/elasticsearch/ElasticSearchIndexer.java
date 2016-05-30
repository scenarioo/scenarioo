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

package org.scenarioo.dao.search.elasticsearch;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.dao.search.dao.PageSearchDao;
import org.scenarioo.dao.search.dao.ScenarioSearchDao;
import org.scenarioo.dao.search.dao.StepSearchDao;
import org.scenarioo.dao.search.dao.UseCaseSearchDao;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

class ElasticSearchIndexer {
	private final static Logger LOGGER = Logger.getLogger(ElasticSearchIndexer.class);

	private TransportClient client;
    private String indexName;

    ElasticSearchIndexer(String indexName) {
        try {
            this.client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            this.indexName = indexName;

        } catch (UnknownHostException e) {
            LOGGER.info("no elasticsearch cluster running.");
        }

    }

    void setupCleanIndex(String indexName) {
        if(indexExists(indexName)) {
            client.admin().indices().prepareDelete(indexName).get();

            LOGGER.debug("Removed existing index " + indexName);
        }

        client.admin().indices().prepareCreate(indexName)
                .addMapping("scenario", createMappingForType("scenario"))
                .addMapping("page", createMappingForType("page"))
                .addMapping("step", createMappingForType("step"))
                .get();
        LOGGER.debug("Added new index " + indexName);
    }

    void indexUseCases(UseCaseScenariosList useCaseScenariosList) {
        for (UseCaseScenarios useCaseScenarios : useCaseScenariosList.getUseCaseScenarios()) {
            UseCaseSearchDao searchDao = new UseCaseSearchDao();
            searchDao.setUseCase(useCaseScenarios.getUseCase());

            indexUseCase(searchDao);

            for (ScenarioSummary scenario : useCaseScenarios.getScenarios()) {
                ScenarioSearchDao scenarioSearchDao = new ScenarioSearchDao(scenario.getScenario(), useCaseScenarios.getUseCase().getName());

                indexScenario(scenarioSearchDao);
            }
        }
    }

	void indexSteps(List<Step> stepsList, List<StepLink> stepLinksList, Scenario scenario, UseCase usecase) {
		for(int i = 0; i < stepsList.size(); i++) {
			Step step = stepsList.get(i);
			StepLink link = stepLinksList.get(i);

			StepSearchDao stepSearchDao = new StepSearchDao(step, link, scenario, usecase);
			indexDocument(FullTextSearch.STEP, stepSearchDao, step.getStepDescription().getTitle());
		}
	}

    private void indexUseCase(UseCaseSearchDao useCaseSearchDao) {
        indexDocument(FullTextSearch.USECASE, useCaseSearchDao, useCaseSearchDao.getUseCase().getName());
    }

    private void indexScenario(ScenarioSearchDao scenariosearchDao) {
        indexDocument(FullTextSearch.SCENARIO, scenariosearchDao, scenariosearchDao.getScenario().getName());
    }

    private <T> void indexDocument(String type, T document, String documentName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writer();

            client.prepareIndex(indexName, type).setSource(writer.writeValueAsBytes(document)).get();

            LOGGER.debug("Indexed use case " + documentName + " for index " + indexName);
        } catch (IOException e) {
            LOGGER.error("Could not index use case " + documentName + ". Will skip this one.", e);
        }
    }

    private boolean indexExists(String indexName) {
        return client.admin().indices()
                .prepareExists(indexName)
                .execute().actionGet().isExists();
    }

    private String createMappingForType(final String type) {

        return "{" +
                "	\"" + type + "\":	{" +
                "		\"dynamic_templates\": [" +
                "			{" +
                "				\"ignore_meta_data\": {" +
                "					\"path_match\": \"_meta.*\"," +
                "					\"mapping\": {" +
                "						\"index\": \"no\"" +
                "					}" +
                "				}" +
                "			}" +
                "		]" +
                "	}" +
                "}";
    }
}
