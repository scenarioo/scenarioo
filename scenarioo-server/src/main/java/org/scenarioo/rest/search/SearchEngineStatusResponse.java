package org.scenarioo.rest.search;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchEngineStatusResponse {

	private boolean searchEngineRunning;

	public SearchEngineStatusResponse() {
		// for serializer
	}
	
	public SearchEngineStatusResponse(final boolean searchEngineRunning) {
		this.searchEngineRunning = searchEngineRunning;
	}

	public boolean isSearchEngineRunning() {
		return searchEngineRunning;
	}

	public void setSearchEngineRunning(final boolean searchEngineRunning) {
		this.searchEngineRunning = searchEngineRunning;
	}

}
