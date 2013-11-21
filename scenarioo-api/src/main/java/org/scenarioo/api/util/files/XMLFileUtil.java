package org.scenarioo.api.util.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

public class XMLFileUtil {
	
	private static final Class<?>[] SUPPORTED_COLLECTION_CLASSES = new Class<?>[] { HashMap.class, ArrayList.class };
	
	private static final Class<?>[] SUPPORTED_GENERIC_CLASSES = new Class<?>[] { ObjectDescription.class,
			ObjectReference.class, ObjectList.class, ObjectTreeNode.class, Details.class };
	
	public static <T> void marshal(final T object,
			final File destFile, Class<?>... classesToBind) {
		JAXBContext contextObj;
		try {
			classesToBind = appendClasses(classesToBind, object.getClass());
			classesToBind = appendClasses(classesToBind, SUPPORTED_COLLECTION_CLASSES);
			classesToBind = appendClasses(classesToBind, SUPPORTED_GENERIC_CLASSES);
			contextObj = JAXBContext.newInstance(classesToBind);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshallerObj.marshal(object, new FileOutputStream(destFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not marshall Object of type " + object.getClass().getName()
					+ " into file: " + destFile.getAbsolutePath(), e);
		}
		
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
			classesToBind = appendClasses(classesToBind, SUPPORTED_GENERIC_CLASSES);
			contextObj = JAXBContext.newInstance(classesToBind);
			Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
			return (T) unmarshallerObj.unmarshal(new FileInputStream(srcFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not unmarshall " + srcFile.getAbsolutePath(), e);
		}
	}
	
	public static <T> List<T> unmarshalListOfFiles(final List<File> files, final Class<T> targetClass,
			final Class<?>... classesToBind) {
		List<T> result = new ArrayList<T>();
		for (File file : files) {
			result.add(unmarshal(file, targetClass, classesToBind));
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
			result.add(new ObjectFromDirectory<T>(unmarshal(file, targetClass, classesToBind), file
					.getParentFile().getName()));
		}
		return result;
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
