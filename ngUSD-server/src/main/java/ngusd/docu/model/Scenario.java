package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Scenario {
	
	private String name;
	private String description;
	private String status = "";
	private Details details = new Details();
	private ScenarioCalculatedData calculatedData;
	
	public Scenario() {
		this("", "", 0, 0);
	}
	
	public Scenario(final String name, final String description, final int numberOfPages, final int numberOfSteps) {
		super();
		this.name = name;
		this.description = description;
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
	
	public ScenarioCalculatedData getCalculatedData() {
		return calculatedData;
	}
	
	public void setCalculatedData(final ScenarioCalculatedData calculatedData) {
		this.calculatedData = calculatedData;
	}
	
}
