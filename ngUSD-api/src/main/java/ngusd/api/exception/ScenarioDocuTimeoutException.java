package ngusd.api.exception;

/**
 * Exception that occurs when generating and writing docu content files takes too long.
 */
public class ScenarioDocuTimeoutException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ScenarioDocuTimeoutException(final String message) {
		super(message);
	}
	
	public ScenarioDocuTimeoutException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
