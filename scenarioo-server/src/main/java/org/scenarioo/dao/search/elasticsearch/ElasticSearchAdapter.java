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

import com.carrotsearch.hppc.cursors.ObjectCursor;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.scenarioo.dao.context.ContextPathHolder;
import org.scenarioo.dao.search.SearchAdapter;
import org.scenarioo.dao.search.model.SearchResults;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.search.SearchRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchAdapter implements SearchAdapter {

    private final static Logger LOGGER = Logger.getLogger(ElasticSearchAdapter.class);

	// It's ok that this is static because the Elasticsearch endpoint config can not be changed
	// at runtime but only by restarting Scenarioo. If we ever make it possible to change
	// the endpoint config in the UI (i.e. at runtime) then the client can not be created only
	// once anymore but has to be recreated when changing the config.
	private static TransportClient client;

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	private final String endpoint = configurationRepository.getConfiguration().getElasticSearchEndpoint();

	private final String clusterName = configurationRepository.getConfiguration().getElasticSearchClusterName();

    public ElasticSearchAdapter() {
		if (client != null) {
			// already initialized
			return;
		}

		if (!isSearchEndpointConfigured()) {
			LOGGER.info("no valid elasticsearch endpoint configured.");
			return;
		}

		try {
			int portSeparator = endpoint.lastIndexOf(':');
			String host = endpoint.substring(0, portSeparator);
			int port = Integer.parseInt(endpoint.substring(portSeparator + 1), 10);
			client = new PreBuiltTransportClient(Settings.builder()
				.put("cluster.name", clusterName).build())
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		} catch (UnknownHostException e) {
			LOGGER.warn("No elasticsearch cluster running.");
		} catch (Throwable e) {
			// Silently log the error in any case to not let Scenarioo crash just because Easticsearch connection fails somehow.
			LOGGER.error("Could not connect to Elastic Search Engine", e);
		}
    }

	@Override
	public boolean isSearchEndpointConfigured() {
		return endpoint != null && endpoint.contains(":");
	}

    @Override
    public boolean isEngineRunning() {
        if(client == null) {
            return false;
        }

        try {
            client.admin().cluster().prepareHealth().get();

            return true;
        } catch (NoNodeAvailableException e) {
            return false;
        }
    }

	@Override
	public String getEndpoint() {
		return endpoint;
	}

    @Override
    public SearchResults searchData(final SearchRequest searchRequest) {
        final String indexName = getIndexName(searchRequest.getBuildIdentifier());

        ElasticSearchSearcher elasticSearchSearcher = new ElasticSearchSearcher(indexName, client);
        return elasticSearchSearcher.search(searchRequest);
    }

    @Override
    public void setupNewBuild(final BuildIdentifier buildIdentifier) {
        String indexName = getIndexName(buildIdentifier);

		new ElasticSearchIndexer(indexName, client).setupCleanIndex(indexName);
    }

    @Override
    public void indexUseCases(final UseCaseScenariosList useCaseScenariosList, final BuildIdentifier buildIdentifier) {
		String indexName = getIndexName(buildIdentifier);

		ElasticSearchIndexer elasticSearchIndexer = new ElasticSearchIndexer(indexName, client);
        elasticSearchIndexer.indexUseCases(useCaseScenariosList);
    }

	@Override
	public void indexSteps(final List<Step> steps, final List<StepLink> stepLinks, final Scenario scenario, final UseCase usecase, final BuildIdentifier buildIdentifier) {
		String indexName = getIndexName(buildIdentifier);

		ElasticSearchIndexer elasticSearchIndexer = new ElasticSearchIndexer(indexName, client);
		elasticSearchIndexer.indexSteps(steps, stepLinks, scenario, usecase);
	}

	@Override
    public void updateAvailableBuilds(final List<BuildIdentifier> availableBuilds) {
		List<String> existingIndices = getAvailableIndicesOfCurrentContext();
        List<String> availableBuildNames = getAvailableBuildNames(availableBuilds);

        for(String index : existingIndices) {
            if (!availableBuildNames.contains(index)) {
                deleteIndex(index);
                LOGGER.debug("Removed index " + index);
            }
        }
    }

	/**
	 * It's important to only get the indices that belong to the current context. Otherwise
	 * we also delete indices of other contexts.
	 */
	private List<String> getAvailableIndicesOfCurrentContext() {
        ImmutableOpenMap<String, IndexMetaData> indices = client.admin().cluster()
                .prepareState().get().getState()
                .getMetaData().getIndices();

        List<String> indicesOfCurrentContext = new ArrayList<>(indices.keys().size());
        for (ObjectCursor<String> key : indices.keys()) {
			if (key.value.startsWith(getContextPrefix())) {
				indicesOfCurrentContext.add(key.value);
			}
        }
        return indicesOfCurrentContext;
    }

    private List<String> getAvailableBuildNames(final List<BuildIdentifier> existingBuilds) {
        List<String> buildNames = new ArrayList<>();

        for(BuildIdentifier identifier : existingBuilds) {
            buildNames.add(getIndexName(identifier));
        }

        return buildNames;
    }

    private DeleteIndexResponse deleteIndex(final String indexName) {
        return client.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
    }

    private String getIndexName(final BuildIdentifier buildIdentifier) {
		String index = getContextPrefix() + buildIdentifier.getBranchName() + "-" + buildIdentifier.getBuildName();
		// index for elastic search must be lowercase
		return index.toLowerCase();
	}

	private String getContextPrefix() {
		// using "---" in order to avoid name collisions with contexts that have the same prefix.
		// e.g. "scenarioo-develop" should not include indices of "scenarioo-develop-pr" so that
		// they are not deleted during cleanup of indices.
		String contextPath = ContextPathHolder.INSTANCE.getContextPath();
		if(StringUtils.isBlank(contextPath)) {
			//Elasticsearch indexes may not start with -, _ or +
			contextPath = "rootContext";
		}
		return contextPath + "---";
    }
}
