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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.scenarioo.dao.search.IgnoreUseCaseSetStatusMixIn;
import org.scenarioo.dao.search.model.*;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.search.SearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ElasticSearchSearcher {
	private final static Logger LOGGER = Logger.getLogger(ElasticSearchSearcher.class);
	private final static int MAX_SEARCH_RESULTS = 200;

	private final String indexName;
	private final RestHighLevelClient restClient;

	private final ObjectReader useCaseReader;
	private final ObjectReader scenarioReader;
	private final ObjectReader stepReader;

	ElasticSearchSearcher(final String indexName, RestHighLevelClient restClient) {
		this.indexName = indexName;
		this.restClient = restClient;

		useCaseReader = generateStandardReaders(UseCase.class, SearchableUseCase.class);
		scenarioReader = generateStandardReaders(Scenario.class, SearchableScenario.class);
		stepReader = generateStandardReaders(StepDescription.class, SearchableStep.class);
	}

	SearchResults search(final SearchRequest searchRequest) {
		SearchResponse searchResponse;
		try {
			searchResponse = executeSearch(searchRequest);
		} catch (IOException e) {
			LOGGER.error("Search failed", e);
			return SearchResults.noHits();
		}

		if (searchResponse.getHits().getHits().length == 0) {
			LOGGER.debug("No results found for " + searchRequest);
			return SearchResults.noHits();
		}

		SearchHit[] hits = searchResponse.getHits().getHits();

		List<SearchableObject> results = new ArrayList<>();
		for (SearchHit searchHit : hits) {
			try {
				String stringRepresentation = searchHit.toString();
				if (stringRepresentation.contains("\"type\" : \"scenario\"")) {
					results.add(parseScenario(searchHit));
				} else if (stringRepresentation.contains("\"type\" : \"usecase\"")) {
					results.add(parseUseCase(searchHit));
				} else if (stringRepresentation.contains("\"type\" : \"step\"")) {
					results.add(parseStep(searchHit));
				}
			} catch (IOException e) {
				LOGGER.error("Could not parse entry " + searchHit.getSourceAsString(), e);
			}
		}

		return new SearchResults(results, hits.length, searchResponse.getHits().getTotalHits().value);
	}

	private SearchResponse executeSearch(final SearchRequest searchRequest) throws IOException {
		LOGGER.debug("Search in index " + indexName + " for " + searchRequest.getQ());

		org.elasticsearch.action.search.SearchRequest elasticSearchRequest = Requests.searchRequest(indexName)
				.searchType(SearchType.QUERY_THEN_FETCH)
				.source(new SearchSourceBuilder()
						.size(MAX_SEARCH_RESULTS)
						.query(QueryBuilders.multiMatchQuery(searchRequest.getQ(), getFieldNames(searchRequest))
								.fuzziness(Fuzziness.AUTO)
								.operator(Operator.AND)
						)
				);

		return restClient.search(elasticSearchRequest, RequestOptions.DEFAULT);
	}

	private String[] getFieldNames(SearchRequest searchRequest) {
		if (searchRequest.includeHtml()) {
			return new String[]{"catch_all", "step.html.htmlSource"};
		} else {
			return new String[]{"catch_all"};
		}
	}

	private SearchableObject parseUseCase(final SearchHit searchHit) throws IOException {
		return useCaseReader.<SearchableUseCase>readValue(searchHit.getSourceRef().streamInput());
	}

	private SearchableObject parseScenario(final SearchHit searchHit) throws IOException {
		return scenarioReader.<SearchableScenario>readValue(searchHit.getSourceRef()
				.streamInput());
	}

	private SearchableObject parseStep(final SearchHit searchHit) throws IOException {
		return stepReader.<SearchableStep>readValue(searchHit.getSourceRef()
				.streamInput());
	}

	// TODO #552 Remove the IgnoreUseCaseSetStatusMixIn and the FAIL_ON_UNKNOWN_PROPERTIES setting
	// as soon as we change the Scenarioo format to JSON. Then add the @JsonIgnore attribute
	// to the all setStatus(Status value) helper methods.
	private ObjectReader generateStandardReaders(final Class<?> targetDao, final Class<?> targetSearchDao) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.addMixIn(targetDao, IgnoreUseCaseSetStatusMixIn.class);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return objectMapper.readerFor(targetSearchDao);
	}
}
