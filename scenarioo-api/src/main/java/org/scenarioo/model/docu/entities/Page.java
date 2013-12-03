package org.scenarioo.model.docu.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.scenarioo.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode(exclude = { "details" })
public class Page implements Serializable {
	
	private String name;
	private final Details details = new Details();
	
	public Page() {
		this.name = "";
	}
	
	public Page(final String name) {
		this.name = name;
	}
	
}
