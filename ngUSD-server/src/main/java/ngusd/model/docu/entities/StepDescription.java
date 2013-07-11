package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepDescription {
	
	private int index = 0;
	private String title = "";
	private String screenshotURL;
	private final Details details = new Details();
	
}
