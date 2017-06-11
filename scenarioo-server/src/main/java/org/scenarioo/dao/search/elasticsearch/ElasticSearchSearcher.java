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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.dao.search.IgnoreFeatureSetStatusMixIn;
import org.scenarioo.dao.search.model.SearchResults;
import org.scenarioo.dao.search.model.SearchableObject;
import org.scenarioo.dao.search.model.SearchableScenario;
import org.scenarioo.dao.search.model.SearchableStep;
import org.scenarioo.dao.search.model.SearchableFeature;
import org.scenarioo.model.docu.entities.Feature;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.rest.search.SearchRequest;

class ElasticSearchSearcher {
	private final static Logger LOGGER = Logger.getLogger(ElasticSearchSearcher.class);
	private final static int MAX_SEARCH_RESULTS = 200;

	private String indexName;
    private TransportClient client;

    private ObjectReader featureReader;
    private ObjectReader scenarioReader;
    private ObjectReader stepReader;

    ElasticSearchSearcher(final String indexName) {
        try {
            this.client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            this.indexName = indexName;

			featureReader = generateStandardReaders(Feature.class, SearchableFeature.class);
			scenarioReader = generateStandardReaders(Scenario.class, SearchableScenario.class);
			stepReader = generateStandardReaders(StepDescription.class, SearchableStep.class);

        } catch (UnknownHostException e) {
            LOGGER.info("no elasticsearch cluster running.");
        }
    }

    SearchResults search(final SearchRequest searchRequest) {
        SearchResponse searchResponse = executeSearch(searchRequest);

        if (searchResponse.getHits().getHits().length == 0) {
            LOGGER.debug("No results found for " + searchRequest);
            return SearchResults.noHits();
        }

        SearchHit[] hits = searchResponse.getHits().getHits();

        List<SearchableObject> results = new ArrayList<SearchableObject>();
        for (SearchHit searchHit : hits) {
            try {
                String type = searchHit.getType();
                if (type.equals(FullTextSearch.FEATURE)) {
                    results.add(parseFeature(searchHit));

                } else if (type.equals(FullTextSearch.SCENARIO)) {
                    results.add(parseScenario(searchHit));

                } else if (type.equals(FullTextSearch.STEP)) {
                    results.add(parseStep(searchHit));

                } else {
                    LOGGER.error("No type mapping for " + searchHit.getType() + " known.");
                }
            } catch (IOException e) {
                LOGGER.error("Could not parse entry " + searchHit.getSourceAsString(), e);
            }
        }

        return new SearchResults(results, hits.length, searchResponse.getHits().getTotalHits());
    }

    private SearchResponse executeSearch(final SearchRequest searchRequest) {
        LOGGER.debug("Search in index " + indexName + " for " + searchRequest.getQ());

		SearchRequestBuilder setQuery = client.prepareSearch()
                .setIndices(indexName)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
				.setSize(MAX_SEARCH_RESULTS)
				.setQuery(QueryBuilders.multiMatchQuery(searchRequest.getQ(), getFieldNames(searchRequest))
					.fuzziness(Fuzziness.AUTO)
					.operator(MatchQueryBuilder.Operator.AND));

        return setQuery.execute().actionGet();
    }

	private String[] getFieldNames(SearchRequest searchRequest) {
		if(searchRequest.includeHtml()) {
			return new String[]{"_all", "step.html.htmlSource"};
		} else {
			return new String[]{"_all"};
		}
	}

	private SearchableObject parseFeature(final SearchHit searchHit) throws IOException {
        SearchableFeature searchableFeature = featureReader.readValue(searchHit.getSourceRef().streamInput());

		return searchableFeature;
    }

    private SearchableObject parseScenario(final SearchHit searchHit) throws IOException {
        SearchableScenario scenarioSearchDao = scenarioReader.readValue(searchHit.getSourceRef()
                .streamInput());

		return scenarioSearchDao;
    }

    private SearchableObject parseStep(final SearchHit searchHit) throws IOException {
        SearchableStep stepSearchDao = stepReader.readValue(searchHit.getSourceRef()
                .streamInput());

		return stepSearchDao;
    }

	// TODO #552 Remove the IgnoreFeatureSetStatusMixIn and the FAIL_ON_UNKNOWN_PROPERTIES setting
	// as soon as we change the Scenarioo format to JSON. Then add the @JsonIgnore attribute
	// to the all setStatus(Status value) helper methods.
	private ObjectReader generateStandardReaders(final Class<?> targetDao, final Class<?> targetSearchDao) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().addMixInAnnotations(targetDao,
			IgnoreFeatureSetStatusMixIn.class);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return objectMapper.reader(targetSearchDao);
	}
}
