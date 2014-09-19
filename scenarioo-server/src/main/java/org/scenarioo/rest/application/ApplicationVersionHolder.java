package org.scenarioo.rest.application;

import java.util.Properties;

/**
 * Contains the information from the version.properties file.
 */

public enum ApplicationVersionHolder {
	
	INSTANCE;
	
	private ApplicationVersion applicationVersion;
	
	public void initialize(final String version, final String buildDate, final String apiVersion,
			final String aggregatedDataFormatVersion) {
		applicationVersion = new ApplicationVersion(version, buildDate, apiVersion,
				aggregatedDataFormatVersion);
	}
	
	public void initializeFromProperties(final Properties versionProperties) {
		String version = versionProperties.getProperty("version");
		String buildDate = versionProperties.getProperty("build-date");
		String apiVersion = versionProperties.getProperty("apiVersion");
		String aggregatedDataFormatVersion = versionProperties.getProperty("aggregatedDataFormatVersion");
		initialize(version, buildDate, apiVersion, aggregatedDataFormatVersion);
	}
	
	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}
	
}
