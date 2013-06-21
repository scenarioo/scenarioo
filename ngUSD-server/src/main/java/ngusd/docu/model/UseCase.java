package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(final Details details) {
		this.details = details;
	}
	
}
