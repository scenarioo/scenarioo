package org.scenarioo.rest.search;

import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

@XmlRootElement
public class SearchResponse {

	private ObjectTreeNode<ObjectReference> results;
	private String errorMessage;

	public SearchResponse() {
		// for serializer
	}
	
	public SearchResponse(final ObjectTreeNode<ObjectReference> results) {
		this.setResults(results);
	}

	public SearchResponse(final String errorMessage) {
		this.setErrorMessage(errorMessage);
	}

	public ObjectTreeNode<ObjectReference> getResults() {
		return results;
	}

	public void setResults(ObjectTreeNode<ObjectReference> results) {
		this.results = results;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
