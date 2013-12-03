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
public class StepMetadata implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String visibleText;
	
	private Details details = new Details();
	
	public void addDetail(final String key, final Object value) {
		details.put(key, value);
	}
	
}
