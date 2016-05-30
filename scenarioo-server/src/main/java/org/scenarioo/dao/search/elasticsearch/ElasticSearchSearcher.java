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
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.dao.search.IgnoreUseCaseSetStatusMixIn;
import org.scenarioo.dao.search.dao.*;
import org.scenarioo.model.docu.entities.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ElasticSearchSearcher {
	private final static Logger LOGGER = Logger.getLogger(ElasticSearchSearcher.class);

	private String indexName;
    private TransportClient client;

    private ObjectReader useCaseReader;
    private ObjectReader scenarioReader;
    private ObjectReader pageReader;
    private ObjectReader stepReader;

    ElasticSearchSearcher(String indexName) {
        try {
            this.client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            this.indexName = indexName;

			useCaseReader = generateStandardReaders(UseCase.class, UseCaseSearchDao.class);
			scenarioReader = generateStandardReaders(Scenario.class, ScenarioSearchDao.class);
			pageReader = generateStandardReaders(Page.class, PageSearchDao.class);
			stepReader = generateStandardReaders(StepDescription.class, StepSearchDao.class);

        } catch (UnknownHostException e) {
            LOGGER.info("no elasticsearch cluster running.");
        }
    }

    List<SearchDao> search(String q) {
        SearchResponse searchResponse = executeSearch(q);

        if (searchResponse.getHits().getHits().length == 0) {
            LOGGER.debug("No results found for " + q);
            return Collections.emptyList();
        }

        SearchHit[] hits = searchResponse.getHits().getHits();

        List<SearchDao> results = new ArrayList<SearchDao>();
        for (SearchHit searchHit : hits) {
            try {
                String type = searchHit.getType();
                if (type.equals(FullTextSearch.USECASE)) {
                    results.add(parseUseCase(searchHit));

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

        return results;
    }

    private SearchResponse executeSearch(final String q) {
        LOGGER.debug("Search in index " + indexName + " for " + q);
        SearchRequestBuilder setQuery = client.prepareSearch()
                .setIndices(indexName)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.queryStringQuery("*" + q + "*"));

        return setQuery.execute().actionGet();
    }

    private SearchDao parseUseCase(final SearchHit searchHit) throws IOException {
        UseCaseSearchDao useCaseResult = useCaseReader.readValue(searchHit.getSourceRef().streamInput());

		return useCaseResult;
    }

    private SearchDao parseScenario(final SearchHit searchHit) throws IOException {
        ScenarioSearchDao scenarioSearchDao = scenarioReader.readValue(searchHit.getSourceRef()
                .streamInput());

		return scenarioSearchDao;
    }

    private SearchDao parsePage(final SearchHit searchHit) throws IOException {
        PageSearchDao pageSearchDao = pageReader.readValue(searchHit.getSourceRef()
                .streamInput());

		return pageSearchDao;
    }

    private SearchDao parseStep(final SearchHit searchHit) throws IOException {
        StepSearchDao stepSearchDao = stepReader.readValue(searchHit.getSourceRef()
                .streamInput());

		return stepSearchDao;
    }

	private ObjectReader generateStandardReaders(Class targetDao, Class targetSearchDao) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().addMixInAnnotations(targetDao,
			IgnoreUseCaseSetStatusMixIn.class);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


		return objectMapper.reader(targetSearchDao);
	}
}
