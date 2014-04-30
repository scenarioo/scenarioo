package org.scenarioo.rest.application;

import java.util.Properties;

/**
 * Contains the information from the version.properties file.
 */

public enum ApplicationVersionHolder {

	INSTANCE;

	private ApplicationVersion applicationVersion;

	public void initialize(final String version, final String buildDate) {
		applicationVersion = new ApplicationVersion(version, buildDate);
	}

	public void initializeFromProperties(final Properties versionProperties) {
		String version = versionProperties.getProperty("version");
		String buildDate = versionProperties.getProperty("build-date");
		initialize(version, buildDate);
	}

	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}

}
