package org.scenarioo.rest.search;

import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.dao.search.FullTextSearch;

@XmlRootElement
public class SearchEngineStatusResponse {

	private boolean running;
	private boolean endpointConfigured;
	private String endpoint;

	public SearchEngineStatusResponse() {
		// for serializer
	}
	
	public SearchEngineStatusResponse(final FullTextSearch fullTextSearch) {
		this.running = fullTextSearch.isEngineRunning();
		this.setEndpointConfigured(fullTextSearch.isSearchEngineEndpointConfigured());
		this.endpoint = fullTextSearch.getEndpoint();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(final boolean searchEngineRunning) {
		this.running = searchEngineRunning;
	}

	public boolean isEndpointConfigured() {
		return endpointConfigured;
	}

	public void setEndpointConfigured(final boolean searchEngineEndpointConfigured) {
		this.endpointConfigured = searchEngineEndpointConfigured;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(final String searchEngineEndpoint) {
		this.endpoint = searchEngineEndpoint;
	}

}
