package org.scenarioo.rest.search;

import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.dao.search.FullTextSearch;

@XmlRootElement
public class SearchEngineStatusResponse {

	private boolean searchEngineRunning;
	private String searchEngineEndpoint;

	public SearchEngineStatusResponse() {
		// for serializer
	}
	
	public SearchEngineStatusResponse(final FullTextSearch fullTextSearch) {
		this.searchEngineRunning = fullTextSearch.isEngineRunning();
		this.searchEngineEndpoint = fullTextSearch.getEndpoint();
	}

	public boolean isSearchEngineRunning() {
		return searchEngineRunning;
	}

	public void setSearchEngineRunning(final boolean searchEngineRunning) {
		this.searchEngineRunning = searchEngineRunning;
	}

	public String getSearchEngineEndpoint() {
		return searchEngineEndpoint;
	}

	public void setSearchEngineEndpoint(final String searchEngineEndpoint) {
		this.searchEngineEndpoint = searchEngineEndpoint;
	}

}
