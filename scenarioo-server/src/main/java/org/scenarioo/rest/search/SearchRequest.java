package org.scenarioo.rest.search;

import org.scenarioo.rest.base.BuildIdentifier;

public class SearchRequest {
	private final BuildIdentifier buildIdentifier;
	private final String q;
	private final boolean includeHtml;

    public SearchRequest(BuildIdentifier buildIdentifier, String q, boolean includeHtml) {
        this.q = q;
        this.buildIdentifier = buildIdentifier;
		this.includeHtml = includeHtml;
    }

    public String getQ() {
        return q;
    }

    public BuildIdentifier getBuildIdentifier() {
        return buildIdentifier;
    }

	public boolean includeHtml() {
		return includeHtml;
	}
}
