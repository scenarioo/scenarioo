package ngusd.api.configuration;

import ngusd.api.ScenarioDocuWriter;

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
