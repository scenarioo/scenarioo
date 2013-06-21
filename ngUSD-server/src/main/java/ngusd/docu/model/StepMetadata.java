package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepMetadata {
	
	private String visibleText;
	
	private Details details = new Details();
	
	public String getVisibleText() {
		return visibleText;
	}
	
	public void setVisibleText(String visibleText) {
		this.visibleText = visibleText;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(Details details) {
		this.details = details;
	}
	
}
