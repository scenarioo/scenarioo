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

	/**
	 * Use this to simply initialize in spring boot app from resource files in class path
	 */
	public void initializeFromClassContext() {
		final InputStream inputStream = ApplicationVersionHolder.class.getResourceAsStream("/version.properties");
		ApplicationVersionHolder.INSTANCE.initializeFromVersionPropertiesInputStream(inputStream);
	}

	/**
	 * Use this to initialize from any input stream (e.g. from servlet context resource in a different web server scenario, like a tomcat war deployment)
	 */
	public void initializeFromVersionPropertiesInputStream(InputStream inputStream) {
		if (inputStream == null) {
			LOGGER.warn("version.properties not found, no real version information available. Continue with unknown version.");
			ApplicationVersionHolder.INSTANCE.initialize("unknown", "unknown", "unknown", "unknown", "develop");
		} else {
			try {
				final Properties properties = new Properties();
				properties.load(inputStream);
				ApplicationVersionHolder.INSTANCE.initializeFromProperties(properties);
			} catch (final Exception e) {
				LOGGER.warn("Could not load version.properties - no real version information available.", e);
				ApplicationVersionHolder.INSTANCE.initialize("unknown", "unknown", "unknown", "unknown", "develop");
			}
		}
		LOGGER.info("Version info loaded from version.properties:");
		LOGGER.info("  Version: " + ApplicationVersionHolder.INSTANCE.getApplicationVersion().getVersion());
		LOGGER.info("  Build date: " + ApplicationVersionHolder.INSTANCE.getApplicationVersion().getBuildDate());
	}

	private void initializeFromProperties(final Properties versionProperties) {
		String version = versionProperties.getProperty("version");
		String buildDate = versionProperties.getProperty("build-date");
		String apiVersion = versionProperties.getProperty("apiVersion");
		String aggregatedDataFormatVersion = versionProperties.getProperty("aggregatedDataFormatVersion");
		String documentationVersion = versionProperties.getProperty("documentationVersion");
		initialize(version, buildDate, apiVersion, aggregatedDataFormatVersion, documentationVersion);
	}

	private void initialize(final String version, final String buildDate, final String apiVersion,
							final String aggregatedDataFormatVersion, String documentationVersion) {
		applicationVersion = new ApplicationVersion(version, buildDate, apiVersion,
				aggregatedDataFormatVersion, documentationVersion);
	}

	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}

}
