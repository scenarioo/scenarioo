package ngusd.dao;

public class ObjectFromDirectory<T> {
	
	private final T object;
	
	private final String directoryName;
	
	public ObjectFromDirectory(final T object, final String directoryName) {
		this.object = object;
		this.directoryName = directoryName;
	}
	
	public T getObject() {
		return object;
	}
	
	public String getDirectoryName() {
		return directoryName;
	}
	
}
