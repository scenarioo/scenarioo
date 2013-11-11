package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ngusd.model.docu.entities.generic.Details;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Call {
	
	private String type;
	
	private String name;
	
	private String request;
	
	private String response;
	
	private Details details = new Details();
	
}
