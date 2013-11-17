package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.generic.Details;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepDescription {
	
	private int index = 0;
	private String title = "";
	private String status = "";
	
	/**
	 * Only the filename of the image, the directory is defined through the scenario, usecase etc.
	 * 
	 * TODO: may be we should remove this from the model and replace by a convention ({stepIndex}.png: 001.png, 002.png
	 * etc.)
	 */
	private String screenshotFileName;
	
	private final Details details = new Details();
	
	/**
	 * TODO the following data should be removed from here, this does not belong into the API for generating data
	 */
	
	// TODO: name should be pageIndex
	private int occurence = 0;
	
	// TODO: name should be pageStepIndex
	private int relativeIndex = 0;
	
	private int variantIndex = 0;
	private StepIdentification previousStepVariant;
	private StepIdentification nextStepVariant;
	
	public void addDetails(final String key, final Object value) {
		details.addDetail(key, value);
	}
	
}
