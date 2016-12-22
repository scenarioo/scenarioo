package org.scenarioo.rest.search;

import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.dao.search.SearchTree;

@XmlRootElement
public class SearchResponse {

	private SearchTree searchTree;
	private String errorMessage;

	public SearchResponse() {
		// for serializer
	}

	public SearchResponse(final SearchTree searchTree) {
		this.setSearchTree(searchTree);
	}

	public SearchResponse(final String errorMessage) {
		this.setErrorMessage(errorMessage);
	}

	public SearchTree getSearchTree() {
		return searchTree;
	}

	public void setSearchTree(SearchTree searchTree) {
		this.searchTree = searchTree;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
