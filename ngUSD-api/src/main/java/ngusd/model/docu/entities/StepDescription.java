package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepDescription {
	
	private int index = 0;
	private String title = "";
	private String status = "";
	
	/**
	 * Only the filename of the image, the directory is defined through the scenario, usecase etc.
	 */
	private String screenshotFileName;
	
	private final Details details = new Details();
	
	/**
	 * TODO the following data should be removed from here, this does not belong into the API for generating data
	 */
	private int occurence = 0;
	private int relativeIndex = 0;
	private int variantIndex = 0;
	private StepIdentification previousStepVariant;
	private StepIdentification nextStepVariant;
	
}
