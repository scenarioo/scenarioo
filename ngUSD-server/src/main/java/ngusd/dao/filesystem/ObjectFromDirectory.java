package ngusd.dao.filesystem;

import lombok.Getter;

public class ObjectFromDirectory<T> {
	
	@Getter
	private final T object;
	@Getter
	private final String directoryName;
	
	public ObjectFromDirectory(final T object, final String directoryName) {
		this.object = object;
		this.directoryName = directoryName;
	}
	
}
