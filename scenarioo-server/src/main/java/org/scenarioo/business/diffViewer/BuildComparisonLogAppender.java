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

package org.scenarioo.business.diffViewer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * A special log appender to track logs only for one thread that currently compares a specific build.
 */
public class BuildComparisonLogAppender extends AppenderSkeleton {

	public static final String MDC_COMPARISON_IDENTIFIER_KEY = "scenarioo-comparison-id";

	private final static ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final String comparisonIdentifier;

	private FileWriter logWriter = null;

	public BuildComparisonLogAppender(final String comparisonIdentifier, final File comparisonLogFile) {
		this.comparisonIdentifier = comparisonIdentifier;
		try {
			logWriter = new FileWriter(comparisonLogFile);
		} catch (final IOException e) {
			throw new RuntimeException(
					"Could not write log file for comparison "
							+ comparisonIdentifier
							+ " (please check access rights and disk space for the configured scenarioo diffViewer content directory)",
					e);
		}
	}

	public void registerForComparisonInCurrentThread() {
		// Set current thread as registered for comparing given build.
		MDC.put(BuildComparisonLogAppender.MDC_COMPARISON_IDENTIFIER_KEY, comparisonIdentifier);
		// configure appender to receive logs.
		Logger.getRootLogger().addAppender(this);
	}

	@Override
	protected void append(final LoggingEvent event) {
		final String comparisonIdentifier = (String) event.getMDC(MDC_COMPARISON_IDENTIFIER_KEY);
		if (this.comparisonIdentifier.equals(comparisonIdentifier)) {
			try {
				logWriter.append(event.getRenderedMessage() + "\n");
				if (event.getThrowableInformation() != null) {
					logWriter.append("see following exception messages and stack trace: \n");
					for (final String s : event.getThrowableStrRep()) {
						logWriter.append("             > " + s + "\n");
					}
				}
			} catch (final IOException e) {
				throw new RuntimeException(
						"Could not write log file for comparison "
								+ comparisonIdentifier
								+ " (please check access rights and disk space for the configured scenarioo diffViewer content directory)",
						e);
			}
		}
	}

	public void unregisterAndFlush() {
		Logger.getRootLogger().removeAppender(this);
		MDC.remove(BuildComparisonLogAppender.MDC_COMPARISON_IDENTIFIER_KEY);
		try {
			logWriter.flush();
			logWriter.close();
		} catch (final IOException e) {
			throw new RuntimeException(
					"Could not write log file for compared build "
							+ comparisonIdentifier
							+ " (please check access rights and disk space for the configured scenarioo diffViewer content directory)",
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

	public static BuildComparisonLogAppender createAndRegisterForLogsOfBuild(final String baseBranchName,
			final String baseBuildName, final String comparisonName) {
		final DiffReader diffReader = new DiffReader(configurationRepository.getDiffViewerDirectory());
		final File buildComparisonLogFile = diffReader.getBuildComparisonLogFile(baseBranchName, baseBuildName,
				comparisonName);
		final String comparisonIdentifier = baseBranchName + "/" + baseBuildName + "/" + comparisonName;
		final BuildComparisonLogAppender buildComparisonLogAppender = new BuildComparisonLogAppender(
				comparisonIdentifier, buildComparisonLogFile);
		buildComparisonLogAppender.registerForComparisonInCurrentThread();
		return buildComparisonLogAppender;
	}

}
