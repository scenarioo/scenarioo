/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.business.builds;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * A special log appender to track logs only for one thread that currently imports a specific build.
 */
public class BuildImportLogAppender extends AppenderSkeleton {
	
	public static final String MDC_BUILD_IDENTIFIER_KEY = "scenarioo-build-id";
	
	private final static ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	
	private final BuildIdentifier buildIdentifier;
	
	private FileWriter logWriter = null;
	
	public BuildImportLogAppender(final BuildIdentifier buildIdentifier, final File buildLogFile) {
		this.buildIdentifier = buildIdentifier;
		try {
			logWriter = new FileWriter(buildLogFile);
		} catch (IOException e) {
			throw new RuntimeException(
					"Could not write log file for build "
							+ buildIdentifier
							+ " (please check access rights and disk space for the configured scenarioo documentation content directory)",
					e);
		}
	}
	
	public void registerForBuildInCurrentThread() {
		// Set current thread as registered for importing given build.
		MDC.put(BuildImportLogAppender.MDC_BUILD_IDENTIFIER_KEY, buildIdentifier);
		// configure appender to receive logs.
		Logger.getRootLogger().addAppender(this);
	}
	
	@Override
	protected void append(final LoggingEvent event) {
		BuildIdentifier buildIdentifier = (BuildIdentifier) event.getMDC(MDC_BUILD_IDENTIFIER_KEY);
		if (buildIdentifier != null && this.buildIdentifier.equals(buildIdentifier)) {
			try {
				logWriter.append(event.getRenderedMessage() + "\n");
				if (event.getThrowableInformation() != null) {
					logWriter.append("           see following exception messages and stack trace: \n");
					for (String s : event.getThrowableStrRep()) {
						logWriter.append("             > " + s + "\n");
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(
						"Could not write log file for build "
								+ buildIdentifier
								+ " (please check access rights and disk space for the configured scenarioo documentation content directory)",
						e);
			}
		}
	}
	
	public void unregisterAndFlush() {
		Logger.getRootLogger().removeAppender(this);
		MDC.remove(BuildImportLogAppender.MDC_BUILD_IDENTIFIER_KEY);
		try {
			logWriter.flush();
			logWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Could not write log file for imported build "
							+ buildIdentifier
							+ " (please check access rights and disk space for the configured scenarioo documentation content directory)",
					e);
		}
	}
	
	@Override
	public void close() {
		// CLosing is managed when unregistering the appender, which is done programmatically
		// see unregisterAndFlush()
	}
	
	@Override
	public boolean requiresLayout() {
		return false;
	}
	
	public static BuildImportLogAppender createAndRegisterForLogsOfBuild(final BuildIdentifier buildIdentifier) {
		ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
				configurationRepository.getDocumentationDataDirectory());
		File buildImportLogFile = dao.getBuildImportLogFile(buildIdentifier);
		BuildImportLogAppender buildImportLogAppender = new BuildImportLogAppender(buildIdentifier, buildImportLogFile);
		buildImportLogAppender.registerForBuildInCurrentThread();
		return buildImportLogAppender;
	}
	
}
