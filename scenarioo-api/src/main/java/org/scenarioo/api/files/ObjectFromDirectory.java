package org.scenarioo.api.files;

import lombok.Getter;

/**
 * Contains any Scenario Docu object entity read from a directory containing also the name of the directory.
 */
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
