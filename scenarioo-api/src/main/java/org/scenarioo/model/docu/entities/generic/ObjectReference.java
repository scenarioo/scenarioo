package org.scenarioo.model.docu.entities.generic;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode
public class ObjectReference implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String type;
	
	public ObjectReference() {
	}
	
	public ObjectReference(final String type, final String name) {
		this.type = type;
		this.name = name;
	}
	
}
