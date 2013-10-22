package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * Contains all the data collected from a webtest for one step of the webtest.
 * 
 * The data is processed by the ngUSd webapplication to transform into webapps
 * internal structure.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Step {
	
	private Page page;
	
	private StepDescription step;
	
	private StepHtml html;
	
	private CallTree callTree = new CallTree();
	
	private StepMetadata metadata = new StepMetadata();
	
}
