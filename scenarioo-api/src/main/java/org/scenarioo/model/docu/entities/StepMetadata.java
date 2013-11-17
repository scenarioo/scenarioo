package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.generic.Details;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepMetadata {
	
	private String visibleText;
	
	private Details details = new Details();
	
	public void addDetail(final String key, final Object value) {
		details.put(key, value);
	}
	
}
