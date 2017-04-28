/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you 
 * permission to link this library with independent modules, according 
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.scenarioo.api.configuration;

import org.scenarioo.api.ScenarioDocuWriter;

public class ScenarioDocuGeneratorConfiguration {
	
	public static final ScenarioDocuGeneratorConfiguration INSTANCE = new ScenarioDocuGeneratorConfiguration();
	
	/**
	 * Use {@link #INSTANCE} to access the configuration.
	 */
	private ScenarioDocuGeneratorConfiguration() {
	}
	
	private int asyncWriteBufferSize = 13;
	
	private int timeoutWaitingForWritingFinishedInSeconds = 3600;
	
	/**
	 * Configure the maximum number of files to buffer for saving them asynchronously. As soon as the buffer capacity is
	 * full, any further calls to save methods on the {@link ScenarioDocuWriter} will block the execution of the
	 * calling thread, and therefore probably also execution of further webtest execution will be blocked.
	 * 
	 * Pay attention, that on the other hand increasing the buffer size may lead to out of memory problems.
	 */
	public void setAsyncWriteBufferSize(final int asyncWriteBufferSize) {
		this.asyncWriteBufferSize = asyncWriteBufferSize;
	}
	
	public int getAsyncWriteBufferSize() {
		return asyncWriteBufferSize;
	}
	
	/**
	 * Configure the number of seconds to wait in method {@link ScenarioDocuWriter#flush()} until writing has
	 * finished. If writing has not finished after this time, the method will throw a timeout exception.
	 */
	public void setTimeoutWaitingForWritingFinishedInSeconds(final int timeoutWaitingForWritingFinishedInSeconds) {
		this.timeoutWaitingForWritingFinishedInSeconds = timeoutWaitingForWritingFinishedInSeconds;
	}
	
	public int getTimeoutWaitingForWritingFinishedInSeconds() {
		return timeoutWaitingForWritingFinishedInSeconds;
	}
	
}
