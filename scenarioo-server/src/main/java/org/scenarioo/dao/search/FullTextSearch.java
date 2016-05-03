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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;

/**
 * 
 */
public class FullTextSearch {

	private final static Logger LOGGER = Logger.getLogger(FullTextSearch.class);

	private TransportClient client;

	public FullTextSearch() {
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			LOGGER.info("no elasticsearch cluster running.");
		}
	}

	/**
	 * @param useCaseScenariosList
	 */
	public void indexUseCases(final UseCaseScenariosList useCaseScenariosList) {
		if (client == null) {
			return;
		}

		addMetaDataMappingToType("scenario");

		try {
			for (UseCaseScenarios useCaseScenarios : useCaseScenariosList.getUseCaseScenarios()) {
				indexUsecase(client, useCaseScenarios.getUseCase());
				for (ScenarioSummary scenario : useCaseScenarios.getScenarios()) {
					indexScenario(client, useCaseScenarios.getUseCase().getName(), scenario.getScenario());
				}
			}
		} catch (NoNodeAvailableException e) {
			System.out.println("No node available. Elasticsearch not running.");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	private void addMetaDataMappingToType(final String type) {
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

			client.admin().indices().prepareCreate("branch-build")
					.addMapping(type, mapping).get();
	}

	private void indexUsecase(final Client client, final UseCase useCase) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter writer = objectMapper.writer();

		UseCaseSearchDao searchDao = new UseCaseSearchDao();
		searchDao.setUseCase(useCase);
		client.prepareIndex("branch-build", "usecase").setSource(writer.writeValueAsBytes(searchDao)).get();
		LOGGER.debug("Indexed use case " + useCase.getName());
	}

	private void indexScenario(final Client client, final String usecaseName, final Scenario scenario)
			throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter writer = objectMapper.writer();

		ScenarioSearchDao scenarioSearchDao = new ScenarioSearchDao(scenario, usecaseName);

		client.prepareIndex("branch-build", "scenario").setSource(writer.writeValueAsBytes(scenarioSearchDao)).get();
		LOGGER.debug("Indexed scenario " + scenario.getName());
	}

	/**
	 * @param q
	 * @return
	 * @throws IOException
	 */
	public List<String> search(final String q) {
		List<String> searchResults = new ArrayList<>();
		searchResults.addAll(searchUsecases(q));
		searchResults.addAll(searchScenarios(q));
		
		if (searchResults.isEmpty()) {
			return Collections.singletonList("No result found.");
		}
		
		return searchResults;
	}

	/**
	 * @param q
	 * @return
	 */
	private Collection<? extends String> searchScenarios(final String q) {
		SearchRequestBuilder setQuery = client.prepareSearch().setTypes("scenario")
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.queryStringQuery("\"" + q + "\""));
		SearchResponse searchResponse = setQuery
				.execute().actionGet();

		SearchHit[] hits = searchResponse.getHits().getHits();
		if (hits.length > 0) {
			List<String> scenarios = new ArrayList<String>();

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getDeserializationConfig().addMixInAnnotations(Scenario.class,
					IgnoreUseCaseSetStatusMixIn.class);
			objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ObjectReader useCaseReader = objectMapper.reader(ScenarioSearchDao.class);
			ScenarioSearchDao scenarioSearchDao;
			for (SearchHit searchHit : hits) {
				try {
					LOGGER.debug(searchHit.getSourceAsString());
					scenarioSearchDao = useCaseReader.readValue(searchHit.getSourceRef().streamInput());

					scenarios.add(scenarioSearchDao.getScenario().getName());
				} catch (IOException e) {
					LOGGER.error("Could not parse result for query " + q);
					throw new RuntimeException("I give up.", e);
				}
			}

			return scenarios;
		} else {
			return Collections.emptyList();
		}
	}

	private List<String> searchUsecases(final String q) {
		SearchRequestBuilder setQuery = client.prepareSearch().setTypes("usecase")
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.queryStringQuery("\"" + q + "\""));
		SearchResponse searchResponse = setQuery
				.execute().actionGet();

		SearchHit[] hits = searchResponse.getHits().getHits();
		if (hits.length > 0) {
			List<String> useCases = new ArrayList<String>();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getDeserializationConfig().addMixInAnnotations(UseCase.class,
					IgnoreUseCaseSetStatusMixIn.class);
			objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ObjectReader useCaseReader = objectMapper.reader(UseCaseSearchDao.class);
			UseCaseSearchDao useCaseResult;
			for (SearchHit searchHit : hits) {
				try {
					LOGGER.debug(searchHit.getSourceAsString());
					useCaseResult = useCaseReader.readValue(searchHit.getSourceRef().streamInput());

					useCases.add(useCaseResult.getUseCase().getName());
				} catch (IOException e) {
					LOGGER.error("Could not parse result for query " + q);
					throw new RuntimeException("I give up.", e);
				}
			}

			return useCases;
		} else {
			return Collections.emptyList();
		}
	}
}