package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.generic.Details;

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
