package org.scenarioo.rest.application;

public class ApplicationVersion {

	private final String version;
	private final String buildDate;

	public ApplicationVersion(final String version, final String buildDate) {
		super();
		this.version = version;
		this.buildDate = buildDate;
	}

	public String getVersion() {
		return version;
	}

	public String getBuildDate() {
		return buildDate;
	}

}
