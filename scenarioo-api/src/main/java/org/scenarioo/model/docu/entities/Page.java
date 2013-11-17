package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.generic.Details;

import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode(exclude = { "details" })
public class Page {
	
	private String name;
	private final Details details = new Details();
	
	public Page() {
		this.name = "";
	}
	
	public Page(final String name) {
		this.name = name;
	}
	
}
