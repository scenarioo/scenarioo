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
import org.scenarioo.dao.search.IgnoreUseCaseSetStatusMixIn;
import org.scenarioo.dao.search.ScenarioSearchDao;
import org.scenarioo.dao.search.UseCaseSearchDao;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;

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

    ElasticSearchSearcher(String indexName) {
        try {
            this.client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            this.indexName = indexName;

            useCaseReader = generateObjectReader(UseCase.class, UseCaseSearchDao.class);
            scenarioReader = generateObjectReader(Scenario.class, ScenarioSearchDao.class);

        } catch (UnknownHostException e) {
            LOGGER.info("no elasticsearch cluster running.");
        }
    }

    private SearchResponse executeSearch(final String q) {
        LOGGER.debug("Search in index " + indexName + " for " + q);
        SearchRequestBuilder setQuery = client.prepareSearch()
                .setIndices(indexName)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.queryStringQuery("\"" + q + "\""));

        return setQuery.execute().actionGet();
    }

    List<String> search(String q) {
        SearchResponse searchResponse = executeSearch(q);

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
                    LOGGER.error("No type mapping for " + searchHit.getType() + " known.");
                }
            } catch (IOException e) {
                LOGGER.error("Could not parse entry " + searchHit);
            }
        }

        return useCases;
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
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.reader(searchDao);
    }
}
