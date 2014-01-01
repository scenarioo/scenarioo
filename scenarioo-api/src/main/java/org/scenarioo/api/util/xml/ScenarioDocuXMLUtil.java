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


package org.scenarioo.api.util.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

/**
 * Streaming of all ScenarioDocu entities to XML and back from input streams or to output streams.
 */
public class ScenarioDocuXMLUtil {
	
	private static final Class<?>[] SUPPORTED_COLLECTION_CLASSES = new Class<?>[] { HashMap.class, ArrayList.class };
	
	private static final Class<?>[] SUPPORTED_GENERIC_CLASSES = new Class<?>[] { ObjectDescription.class,
			ObjectReference.class, ObjectList.class, ObjectTreeNode.class, Details.class };
	
	public static <T> void marshal(final T object, final OutputStream outStream) {
		
		try {
			JAXBContext contextObj = createJAXBContext(object.getClass());
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshallerObj.marshal(object, outStream);
		} catch (Exception e) {
			throw new RuntimeException("Could not marshall Object of type " + object.getClass().getName(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(final Class<T> targetClass, final InputStream inStream) {
		try {
			JAXBContext contextObj = createJAXBContext(targetClass);
			Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
			return (T) unmarshallerObj.unmarshal(inStream);
		} catch (Exception e) {
			throw new RuntimeException("Could not unmarshall object of type " + targetClass.getName(), e);
		}
	}
	
	public static JAXBContext createJAXBContext(Class<?>... classesToBind) throws JAXBException {
		classesToBind = appendClasses(classesToBind, SUPPORTED_COLLECTION_CLASSES);
		classesToBind = appendClasses(classesToBind, SUPPORTED_GENERIC_CLASSES);
		return JAXBContext.newInstance(classesToBind);
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
