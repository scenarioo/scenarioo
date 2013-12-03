package org.scenarioo.model.docu.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Branch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String description;
	
	public Branch() {
		this("", "");
	}
	
	public Branch(final String name) {
		this(name, "");
	}
	
	public Branch(final String name, final String description) {
		this.name = name;
		this.description = description;
	}
}
