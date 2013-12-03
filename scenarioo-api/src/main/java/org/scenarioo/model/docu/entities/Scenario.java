package org.scenarioo.model.docu.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import org.scenarioo.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Scenario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private String status = "";
	private Details details = new Details();
	private ScenarioCalculatedData calculatedData;
	
	public Scenario() {
		this("", "", 0, 0);
	}
	
	public Scenario(final String name, final String description, final int numberOfPages, final int numberOfSteps) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public void addDetail(final String key, final Object value) {
		details.addDetail(key, value);
	}
	
}
