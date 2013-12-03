package org.scenarioo.model.docu.entities;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import org.scenarioo.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Build implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String revision;
	private Date date;
	private String status;
	private Details details = new Details();
	
	public Build() {
	}
	
	public Build(final String name) {
		this();
	}
}
