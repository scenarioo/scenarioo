package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UseCase {
	
	private String name;
	private String description;
	private String status;
	private Details details = new Details();
	
	public UseCase() {
	}
	
	public UseCase(final String name, final String description) {
		this();
		this.name = name;
		this.description = description;
		this.status = "";
	}
}
