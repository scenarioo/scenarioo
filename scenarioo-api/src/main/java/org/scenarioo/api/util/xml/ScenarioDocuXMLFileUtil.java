package org.scenarioo.api.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.api.files.ObjectFromDirectory;

/**
 * Writing or reading of all ScenarioDocu entities to XML files and back
 */
public class ScenarioDocuXMLFileUtil {
	
	public static <T> void marshal(final T object, final File destFile) {
		try {
			ScenarioDocuXMLUtil.marshal(object, new FileOutputStream(destFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not marshall Object of type " + object.getClass().getName()
					+ " into file: " + destFile.getAbsolutePath(), e);
		}
		
	}
	
	public static <T> T unmarshal(final Class<T> targetClass, final File srcFile) {
		if (!srcFile.exists()) {
			throw new ResourceNotFoundException(srcFile.getAbsolutePath());
		}
		try {
			return ScenarioDocuXMLUtil.unmarshal(targetClass, new FileInputStream(srcFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not unmarshall " + srcFile.getAbsolutePath(), e);
		}
	}
	
	public static <T> List<T> unmarshalListOfFiles(final Class<T> targetClass, final List<File> files) {
		List<T> result = new ArrayList<T>();
		for (File file : files) {
			result.add(unmarshal(targetClass, file));
		}
		return result;
	}
	
	/**
	 * Read all passed files and unmarshall these to the given object type, also return the name of the directory the
	 * object was loaded from.
	 */
	public static <T> List<ObjectFromDirectory<T>> unmarshalListOfFilesWithDirNames(
			final List<File> files, final Class<T> targetClass, final Class<?>... classesToBind) {
		List<ObjectFromDirectory<T>> result = new ArrayList<ObjectFromDirectory<T>>();
		for (File file : files) {
			result.add(new ObjectFromDirectory<T>(unmarshal(targetClass, file), file
					.getParentFile().getName()));
		}
		return result;
	}
	
}
