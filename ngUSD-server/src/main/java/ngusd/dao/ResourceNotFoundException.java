package ngusd.dao;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -8246746281046270932L;
	
	private final String resource;
	
	public ResourceNotFoundException(final String resource, final Throwable throwable) {
		super("Resource not found: " + resource, throwable);
		this.resource = resource;
	}
	
	public ResourceNotFoundException(final String resource) {
		super("Resource not found: " + resource);
		this.resource = resource;
	}
	
	public String getResource() {
		return resource;
	}
	
}
