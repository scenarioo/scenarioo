package ngusd.util.files;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLFileUtil {
	
	private static final Class<?>[] SUPPORTED_COLLECTION_CLASSES = new Class<?>[] { HashMap.class, ArrayList.class,
			HashSet.class, Array.class };
	
	/**
	 * List all files in the given directory sorted alphanumerically using a collator.
	 */
	public static File[] listFiles(final File directory) {
		File[] files = directory.listFiles();
		Arrays.sort(files, new AlphanumericFileComparator());
		return files;
	}
	
	/**
	 * TODO improve this marshal method to add the object's class per default to the classesToBind, such that everywhere
	 * where this method is called the third parameter can be removed (if it's the same class as the passed object).
	 */
	public static <T> void marshal(final T object,
			final File destFile, Class<?>... classesToBind) {
		JAXBContext contextObj;
		try {
			classesToBind = appendClasses(classesToBind, SUPPORTED_COLLECTION_CLASSES);
			contextObj = JAXBContext.newInstance(classesToBind);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshallerObj.marshal(object, new FileOutputStream(destFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not marshall Object of type " + object.getClass().getName()
					+ " into file: " + destFile.getAbsolutePath(), e);
		}
		
	}
	
	public static <T> List<T> unmarshalListOfFiles(final File directory, final Class<T> targetClass,
			final Class<?>... classesToBind) {
		List<T> result = new ArrayList<T>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File file : listFiles(directory)) {
			if (!file.isDirectory()) {
				result.add(unmarshal(file, targetClass, classesToBind));
			}
		}
		return result;
	}
	
	/**
	 * Read all files with given name from all subdirectories of 'directory' and unmarshall these to the given object
	 * type.
	 */
	public static <T> List<ObjectFromDirectory<T>> unmarshalListOfFilesFromSubdirsWithDirNames(final File directory,
			final String filename,
			final Class<T> targetClass, final Class<?>... classesToBind) {
		
		List<ObjectFromDirectory<T>> result = new ArrayList<ObjectFromDirectory<T>>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File subDir : listFiles(directory)) {
			if (subDir.isDirectory()) {
				File file = new File(subDir, filename);
				if (file.exists()) {
					result.add(new ObjectFromDirectory<T>(unmarshal(file, targetClass, classesToBind), subDir.getName()));
				}
			}
		}
		return result;
	}
	
	/**
	 * Read all files with given name from all subdirectories of 'directory' and unmarshall these to the given object
	 * type.
	 */
	public static <T> List<T> unmarshalListOfFilesFromSubdirs(final File directory,
			final String filename,
			final Class<T> targetClass, final Class<?>... classesToBind) {
		List<T> result = new ArrayList<T>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File subDir : listFiles(directory)) {
			if (subDir.isDirectory()) {
				File file = new File(subDir, filename);
				if (file.exists()) {
					result.add(unmarshal(file, targetClass, classesToBind));
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(final File srcFile, final Class<T> targetClass, Class<?>... classesToBind) {
		JAXBContext contextObj;
		if (!srcFile.exists()) {
			throw new ResourceNotFoundException(srcFile.getAbsolutePath());
		}
		try {
			classesToBind = appendClasses(classesToBind, targetClass);
			classesToBind = appendClasses(classesToBind, SUPPORTED_COLLECTION_CLASSES);
			contextObj = JAXBContext.newInstance(classesToBind);
			Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
			return (T) unmarshallerObj.unmarshal(srcFile);
		} catch (Exception e) {
			throw new RuntimeException("Could not unmarshall " + srcFile.getAbsolutePath(), e);
		}
	}
	
	private static Class<?>[] appendClasses(Class<?>[] classesToBind, final Class<?>... additionalClasses) {
		int index = classesToBind.length;
		classesToBind = Arrays.copyOf(classesToBind, classesToBind.length + additionalClasses.length);
		for (Class<?> additionalClass : additionalClasses) {
			classesToBind[index] = additionalClass;
			index++;
		}
		return classesToBind;
	}
	
}
