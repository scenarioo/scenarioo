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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.dao.search.model.SearchableScenario;
import org.scenarioo.dao.search.model.SearchableStep;
import org.scenarioo.dao.search.model.SearchableUseCase;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;

import java.io.IOException;
import java.util.List;

class ElasticSearchIndexer {
	private final static Logger LOGGER = Logger.getLogger(ElasticSearchIndexer.class);

	private final String indexName;
	private final RestHighLevelClient restClient;

	ElasticSearchIndexer(final String indexName, RestHighLevelClient restClient) {
		this.indexName = indexName;
		this.restClient = restClient;
	}

	void setupCleanIndex(final String indexName) {
		try {
			if (indexExists(indexName)) {
				restClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
				LOGGER.debug("Removed existing index " + indexName);
			}

			CreateIndexRequest request = new CreateIndexRequest(indexName)
				.settings(Settings.builder()
					.put("index.number_of_shards", 1)
					.put("index.number_of_replicas", 1))
				.mapping(createMapping(), XContentType.JSON);
			restClient.indices().create(request, RequestOptions.DEFAULT);
			LOGGER.debug("Added new index " + indexName);
		} catch (IOException e) {
			LOGGER.error("Could not remove index " + indexName, e);
		}
	}

	void indexUseCases(final UseCaseScenariosList useCaseScenariosList) {
		for (UseCaseScenarios useCaseScenarios : useCaseScenariosList.getUseCaseScenarios()) {
			indexUseCase(new SearchableUseCase(useCaseScenarios.getUseCase()));

			for (ScenarioSummary scenario : useCaseScenarios.getScenarios()) {
				indexScenario(new SearchableScenario(scenario.getScenario(), useCaseScenarios.getUseCase().getName()));
			}
		}
	}

	void indexSteps(final List<Step> stepsList, final List<StepLink> stepLinksList, final Scenario scenario, final UseCase usecase) {
		for (int i = 0; i < stepsList.size(); i++) {
			Step step = stepsList.get(i);
			StepLink link = stepLinksList.get(i);

			indexStep(new SearchableStep(step, link, scenario, usecase));
		}
	}

	private void indexUseCase(final SearchableUseCase searchableUseCase) {
		indexDocument(FullTextSearch.USECASE, searchableUseCase, searchableUseCase.getUseCase().getName());
	}

	private void indexScenario(final SearchableScenario scenariosearchDao) {
		indexDocument(FullTextSearch.SCENARIO, scenariosearchDao, scenariosearchDao.getScenario().getName());
	}

	private void indexStep(final SearchableStep stepSearchDao) {
		indexDocument(FullTextSearch.STEP, stepSearchDao, stepSearchDao.getStep().getStepDescription().getTitle());
	}

	private <T> void indexDocument(final String type, final T document, final String documentName) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectWriter writer = objectMapper.writer();

			IndexRequest indexRequest = new IndexRequest(indexName)
				.id(documentName)
				.source(writer.writeValueAsBytes(document), XContentType.JSON);
			restClient.index(indexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LOGGER.error("Could not index use case " + documentName + ". Will skip this one.", e);
		}
	}

	private boolean indexExists(final String indexName) throws IOException {
		return restClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
	}

	private String createMapping() {

		return "{" +
			"		\"dynamic_templates\": [" +
			"			{" +
			"				\"ignore_meta_data\": {" +
			"					\"path_match\": \"SearchableObjectContext.*\"," +
			"					\"mapping\": {" +
			"						\"index\": \"no\"" +
			"					}" +
			"				}" +
			"			}" +
			"		]," +
			"		\"properties\": {" +
			"			\"step\": {" +
			"				\"properties\": {" +
			"					\"html\": {" +
			"						\"type\": \"object\"" +
			"					}" +
			"				}" +
			"			}" +
			"		}" +
			"}";
	}
}
