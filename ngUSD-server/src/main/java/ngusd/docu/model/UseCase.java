package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UseCase {
	
	private String name;
	private String description;
	private String status;
	private Details details = new Details();
	
	public UseCase() {
		this("", "");
	}
	
	public UseCase(final String name, final String description) {
		super();
		this.name = name;
		this.description = description;
		this.status = "";
	}
}
