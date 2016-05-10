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

package org.scenarioo.dao.search;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;

import com.carrotsearch.hppc.cursors.ObjectCursor;

// TODO Indexing and searching should not really be in the same class.
public class FullTextSearch {

	private final static Logger LOGGER = Logger.getLogger(FullTextSearch.class);

	private TransportClient client;
	private ObjectReader useCaseReader;
	private ObjectReader scenarioReader;

	public FullTextSearch() {
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

			useCaseReader = generateObjectReader(UseCase.class, UseCaseSearchDao.class);
			scenarioReader = generateObjectReader(Scenario.class, ScenarioSearchDao.class);

		} catch (UnknownHostException e) {
			LOGGER.info("no elasticsearch cluster running.");
		}
	}

	public List<String> search(final BuildIdentifier buildIdentifier, final String q) {
		String indexName = getIndexName(buildIdentifier);

		List<String> searchResults = searchData(indexName, q);

		if (searchResults.isEmpty()) {
			return Collections.singletonList("No result found.");
		}

		return searchResults;
	}

	public void indexUseCases(final UseCaseScenariosList useCaseScenariosList, final BuildIdentifier buildIdentifier) {
		// if we don't have a client, elasticsearch is most likely not running so just ignore it.
		if (client == null) {
			return;
		}

		String indexName = getIndexName(buildIdentifier);

		try {
			setupIndex(indexName);

			for (UseCaseScenarios useCaseScenarios : useCaseScenariosList.getUseCaseScenarios()) {
				indexUsecase(client, indexName, useCaseScenarios.getUseCase());

				for (ScenarioSummary scenario : useCaseScenarios.getScenarios()) {
					indexScenario(client, indexName, useCaseScenarios.getUseCase().getName(), scenario.getScenario());
				}
			}
		} catch (NoNodeAvailableException e) {
			LOGGER.info("No node available. Elasticsearch not running.");

		} catch (IOException e) {
			LOGGER.error("Could not create search data.", e);

		} finally {
			client.close();
		}
	}

	public void updateAvailableBuildsInIndex(List<BuildImportSummary> availableBuilds) {
		try {
			List<String> existingBuilds = getAvailableBuildNames(availableBuilds);
			List<String> existingIndices = getAvailableIndices();

			List<String> indicesToRemove = new ArrayList<String>();
			for(String index : existingIndices) {
				if (!existingBuilds.contains(index)) {
					deleteIndex(index);
				}
			}

			LOGGER.info("Indices without a corresponding build that are removed: " + indicesToRemove);
		} catch (NoNodeAvailableException e){
			LOGGER.info("Elasticsearch cluster not running, indexes won't be updated.");
		}
	}

	private String getIndexName(final BuildIdentifier buildIdentifier) {
		return buildIdentifier.getBranchName() + "-" + buildIdentifier.getBuildName();
	}

	private void setupIndex(final String indexName) {
		if(indexExists(indexName)) {
			client.admin().indices().prepareDelete(indexName).get();
		}

		client.admin().indices().prepareCreate(indexName)
				.addMapping("scenario", createMappingForType("scenario")).get();
	}

	private boolean indexExists(String indexName) {
		return client.admin().indices()
				.prepareExists(indexName)
				.execute().actionGet().isExists();
	}

	private String createMappingForType(final String type) {
		String mapping =
				"{" +
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

		return mapping;
	}

	private void indexUsecase(final Client client, final String indexName, final UseCase useCase) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter writer = objectMapper.writer();

		UseCaseSearchDao searchDao = new UseCaseSearchDao();
		searchDao.setUseCase(useCase);

		client.prepareIndex(indexName, "usecase")
				.setSource(writer.writeValueAsBytes(searchDao)).get();

		LOGGER.debug("Indexed use case " + useCase.getName() + " for index " + indexName);
	}

	private void indexScenario(final Client client, final String indexName, final String usecaseName,
			final Scenario scenario) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter writer = objectMapper.writer();

		ScenarioSearchDao scenarioSearchDao = new ScenarioSearchDao(scenario, usecaseName);

		client.prepareIndex(indexName, "scenario").setSource(writer.writeValueAsBytes(scenarioSearchDao)).get();

		LOGGER.debug("Indexed scenario " + scenario.getName() + " for index " + indexName);
	}

	private List<String> searchData(final String indexName, final String q) {
		SearchResponse searchResponse = executeSearch(indexName, q);

		if (searchResponse.getHits().getHits().length == 0) {
			// no results found
			return Collections.emptyList();
		}

		SearchHit[] hits = searchResponse.getHits().getHits();

		List<String> useCases = new ArrayList<String>();
		for (SearchHit searchHit : hits) {
			try {
				String type = searchHit.getType();
				if (type.equals("usecase")) {
					useCases.add(parseUseCase(searchHit));

				} else if (type.equals("scenario")) {
					useCases.add(parseScenario(searchHit));

				} else {
					throw new IllegalStateException("No type mapping for " + searchHit.getType() + " known.");
				}
			} catch (IOException e) {
				LOGGER.error("Could not parse result for query " + q);
				throw new RuntimeException("I give up.", e);
			}
		}

		return useCases;
	}

	private SearchResponse executeSearch(final String indexName, final String q) {
		LOGGER.debug("Search in index " + indexName + " for " + q);
		SearchRequestBuilder setQuery = client.prepareSearch()
				.setIndices(indexName)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.queryStringQuery("\"" + q + "\""));

		SearchResponse searchResponse = setQuery
				.execute().actionGet();
		return searchResponse;
	}

	private DeleteIndexResponse deleteIndex(final String indexName) {
		DeleteIndexResponse response = client.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
		return response;
	}

	private List<String> getAvailableBuildNames(List<BuildImportSummary> availableBuilds) {
		List<String> identifierList = new ArrayList<String>();
		for(BuildImportSummary buildSummary : availableBuilds) {
			String identifier = getIndexName(buildSummary.getIdentifier());

			identifierList.add(identifier);
		}


		return identifierList;
	}

	private List<String> getAvailableIndices() {
		ImmutableOpenMap<String, IndexMetaData> indices = client.admin().cluster()
				.prepareState().get().getState()
				.getMetaData().getIndices();

		List<String> ret = new ArrayList<String>(indices.keys().size());
		for (ObjectCursor<String> key : indices.keys()) {
			ret.add(key.value);
		}
		return ret;
	}

	private String parseUseCase(final SearchHit searchHit) throws IOException {
		UseCaseSearchDao useCaseResult = useCaseReader.readValue(searchHit.getSourceRef().streamInput());

		return useCaseResult.getUseCase().getName();
	}

	private String parseScenario(final SearchHit searchHit) throws IOException {
		ScenarioSearchDao scenarioSearchDao = scenarioReader.readValue(searchHit.getSourceRef()
				.streamInput());

		return scenarioSearchDao.getScenario().getName();

	}

	private ObjectReader generateObjectReader(final Class baseClass, final Class searchDao) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().addMixInAnnotations(baseClass,
				IgnoreUseCaseSetStatusMixIn.class);
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return objectMapper.reader(searchDao);
	}
}
