package org.scenarioo.model.docu.aggregates.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;

/**
 * Just a container for delivering all objects of one type
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ObjectsForType {
	
	@XmlElementWrapper(name = "objects")
	@XmlElement(name = "object")
	private ObjectList<ObjectDescription> objects;
	
	/**
	 * Needed for RestEasy/JAXB
	 */
	public ObjectsForType() {
	}
	
	public ObjectsForType(final ObjectList<ObjectDescription> objects) {
		this.objects = objects;
	}
	
}
