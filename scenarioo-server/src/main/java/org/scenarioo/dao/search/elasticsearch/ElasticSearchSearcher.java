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
import org.scenarioo.dao.search.dao.PageSearchDao;
import org.scenarioo.dao.search.dao.ScenarioSearchDao;
import org.scenarioo.dao.search.dao.StepSearchDao;
import org.scenarioo.dao.search.dao.UseCaseSearchDao;
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

            useCaseReader = generateUseCaseReader();
            scenarioReader = generateScenarioReader();
            pageReader = generatePageReader();
            stepReader = generateStepReader();

        } catch (UnknownHostException e) {
            LOGGER.info("no elasticsearch cluster running.");
        }
    }

    List<String> search(String q) {
        SearchResponse searchResponse = executeSearch(q);

        if (searchResponse.getHits().getHits().length == 0) {
            LOGGER.debug("No results found for " + q);
            return Collections.emptyList();
        }

        SearchHit[] hits = searchResponse.getHits().getHits();

        List<String> results = new ArrayList<String>();
        for (SearchHit searchHit : hits) {
            try {
                String type = searchHit.getType();
                if (type.equals("usecase")) {
                    results.add("UseCase: " + parseUseCase(searchHit));

                } else if (type.equals("scenario")) {
                    results.add("Scenario: " + parseScenario(searchHit));

                } else if (type.equals("page")) {
                    results.add("Page: " + parsePage(searchHit));

                } else if (type.equals("step")) {
                    results.add("Step: " + parseStep(searchHit));

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

    private String parseUseCase(final SearchHit searchHit) throws IOException {
        UseCaseSearchDao useCaseResult = useCaseReader.readValue(searchHit.getSourceRef().streamInput());

        return useCaseResult.getUseCase().getName();
    }

    private String parseScenario(final SearchHit searchHit) throws IOException {
        ScenarioSearchDao scenarioSearchDao = scenarioReader.readValue(searchHit.getSourceRef()
                .streamInput());

        return scenarioSearchDao.getScenario().getName();
    }

    private String parsePage(final SearchHit searchHit) throws IOException {
        PageSearchDao pageSearchDao = pageReader.readValue(searchHit.getSourceRef()
                .streamInput());

        return pageSearchDao.getPage().getName();
    }

    private String parseStep(final SearchHit searchHit) throws IOException {
        StepSearchDao stepSearchDao = stepReader.readValue(searchHit.getSourceRef()
                .streamInput());

        return stepSearchDao.getStep().getStepDescription().getTitle();
    }

    private ObjectReader generateUseCaseReader() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getDeserializationConfig().addMixInAnnotations(UseCase.class,
                IgnoreUseCaseSetStatusMixIn.class);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        return objectMapper.reader(UseCaseSearchDao.class);
    }

	private ObjectReader generateScenarioReader() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().addMixInAnnotations(Scenario.class,
			IgnoreUseCaseSetStatusMixIn.class);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


		return objectMapper.reader(ScenarioSearchDao.class);
	}

	private ObjectReader generatePageReader() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


		return objectMapper.reader(PageSearchDao.class);
	}

	private ObjectReader generateStepReader() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().addMixInAnnotations(StepDescription.class,
			IgnoreUseCaseSetStatusMixIn.class);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


		return objectMapper.reader(StepSearchDao.class);
	}
}
