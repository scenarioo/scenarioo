package org.scenarioo.dao.version;

import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Contains the information from the version.properties file.
 */

public enum ApplicationVersionHolder {

	INSTANCE;

	private static final Logger LOGGER = Logger.getLogger(ApplicationVersionHolder.class);

	private ApplicationVersion applicationVersion;

	public void initializeFromClassContext() {

		final Properties properties = new Properties();
		final InputStream inputStream = ApplicationVersionHolder.class.getResourceAsStream("/version.properties");
		if (inputStream == null) {
			LOGGER.warn("version.properties not found, no real version information available.");
			ApplicationVersionHolder.INSTANCE.initialize("unknown", "unknown", "unknown", "unknown", "develop");
			return;
		}

		try {
			properties.load(inputStream);
			ApplicationVersionHolder.INSTANCE.initializeFromProperties(properties);
		} catch (final Exception e) {
			LOGGER.warn("version.properties not found, no real version information available.", e);
			ApplicationVersionHolder.INSTANCE.initialize("unknown", "unknown", "unknown", "unknown", "develop");
		}

	}

	public void initialize(final String version, final String buildDate, final String apiVersion,
			final String aggregatedDataFormatVersion, String releaseBranch) {
		applicationVersion = new ApplicationVersion(version, buildDate, apiVersion,
				aggregatedDataFormatVersion, releaseBranch);
	}

	public void initializeFromProperties(final Properties versionProperties) {
		String version = versionProperties.getProperty("version");
		String buildDate = versionProperties.getProperty("build-date");
		String apiVersion = versionProperties.getProperty("apiVersion");
		String aggregatedDataFormatVersion = versionProperties.getProperty("aggregatedDataFormatVersion");
		String releaseBranch = versionProperties.getProperty("releaseBranch");
		initialize(version, buildDate, apiVersion, aggregatedDataFormatVersion, releaseBranch);
	}

	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}

}
