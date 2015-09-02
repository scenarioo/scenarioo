package org.scenarioo.model.configuration;

/**
 * These fields are stored in config.xml, but they must never be accessible via HTTP (i.e. also never shown in the
 * config GUI).
 */
public class SecretConfig {

	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

}
