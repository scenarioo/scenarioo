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

package org.scenarioo.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A special log appender to track logs only for one active thread. Use it to write special log information in one
 * separate log file.
 */
public class ThreadLogAppender extends AppenderSkeleton {

	public static final String MDC_IDENTIFIER_KEY = "scenarioo-id";

	private final Object identifier;

	private FileWriter logWriter = null;

	public ThreadLogAppender(final Object identifier, final File logFile) {
		this.identifier = identifier;
		try {
			logWriter = new FileWriter(logFile);
		} catch (final IOException e) {
			throw new RuntimeException(
					"Could not write log file for identifier "
							+ identifier
							+ " (please check access rights and disk space for the configured scenarioo content directory)",
					e);
		}
	}

	public void registerForBuildInCurrentThread() {
		// Set current thread as registered.
		MDC.put(ThreadLogAppender.MDC_IDENTIFIER_KEY, identifier);
		// configure appender to receive logs.
		Logger.getRootLogger().addAppender(this);
	}

	@Override
	protected void append(final LoggingEvent event) {
		final Object identifier = event.getMDC(MDC_IDENTIFIER_KEY);
		if (identifier != null && this.identifier.equals(identifier)) {
			try {
				logWriter.append(event.getRenderedMessage() + "\n");
				if (event.getThrowableInformation() != null) {
					logWriter.append("           see following exception messages and stack trace: \n");
					for (final String s : event.getThrowableStrRep()) {
						logWriter.append("             > " + s + "\n");
					}
				}
			} catch (final IOException e) {
				throw new RuntimeException(
						"Could not write log file for identifier "
								+ identifier
								+ " (please check access rights and disk space for the configured scenarioo content directory)",
						e);
			}
		}
	}

	public void unregisterAndFlush() {
		Logger.getRootLogger().removeAppender(this);
		MDC.remove(ThreadLogAppender.MDC_IDENTIFIER_KEY);
		try {
			logWriter.flush();
			logWriter.close();
		} catch (final IOException e) {
			throw new RuntimeException(
					"Could not write log file for imported identifier "
							+ identifier
							+ " (please check access rights and disk space for the configured scenarioo content directory)",
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

	public static ThreadLogAppender createAndRegisterForLogs(final Object identifier, final File logFile) {
		final ThreadLogAppender threadLogAppender = new ThreadLogAppender(identifier, logFile);
		threadLogAppender.registerForBuildInCurrentThread();
		return threadLogAppender;
	}

}
