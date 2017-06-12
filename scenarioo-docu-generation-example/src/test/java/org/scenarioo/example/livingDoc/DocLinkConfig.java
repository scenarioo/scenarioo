package org.scenarioo.example.livingDoc;

/**
 * Link configuration for creating links with given base URL for documents stored / linked in the documentation
 */
public class DocLinkConfig {

	private String name;
	private String baseUrl;

	public DocLinkConfig(String name, String baseUrl) {
		this.name = name;
		this.baseUrl = baseUrl;
	};

	public String getName() {
		return name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
}
